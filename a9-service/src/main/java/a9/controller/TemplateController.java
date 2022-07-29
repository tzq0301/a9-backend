package a9.controller;

import a9.entity.result.Result;
import a9.entity.template.CleanedTemplate;
import a9.entity.template.Template;
import a9.service.TemplateService;
import a9.util.JWTUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static a9.entity.result.ResultEnum.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/template")
@Slf4j
public class TemplateController {
    private final TemplateService templateService;

    private final JWTUtils jwtUtils;

    public TemplateController(TemplateService templateService, JWTUtils jwtUtils) {
        this.templateService = templateService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/save")
    public Result<Template> save(@RequestBody @Valid Template template,
                                 BindingResult bindingResult,
                                 HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error(error.getDefaultMessage()));
            return Result.error(ILLEGAL_TEMPLATE);
        }

        template.setAuthorId(jwtUtils.getSubject(request.getHeader(AUTHORIZATION)));
        return Result.success(templateService.save(template));
    }

    @GetMapping("/template_name/{template_name}")
    public Result<Template> findTemplateByName(@PathVariable("template_name") String templateName) {
        Optional<Template> template = templateService.findByName(templateName);
        if (template.isEmpty()) {
            return Result.error(NO_SUCH_TEMPLATE);
        }
        return Result.success(template.get());
    }

    @GetMapping("/template_id/{template_id}")
    public Result<Template> findTemplateById(@PathVariable("template_id") ObjectId templateId) {
        Optional<Template> template = templateService.findById(templateId);
        if (template.isEmpty()) {
            return Result.error(NO_SUCH_TEMPLATE);
        }
        return Result.success(template.get());
    }

    @GetMapping("/templates")
    public Result<List<CleanedTemplate>> listTemplatesOfUser(HttpServletRequest request) {
        ObjectId authorId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        return Result.success(templateService.findAllByAuthorId(authorId)
                .stream().map(CleanedTemplate::new).collect(Collectors.toList()));
    }

    @PatchMapping("/template_id/{template_id}")
    public Result<Template> update(@PathVariable("template_id") ObjectId templateId, @RequestBody Template template) {
        Optional<Template> templateById = templateService.findById(templateId);
        if (templateById.isEmpty()) {
            return Result.error(NO_SUCH_TEMPLATE);
        }

        Template templateInDB = templateById.get();

        if (!Strings.isNullOrEmpty(template.getName())) {
            templateInDB.setName(template.getName());
        }

        if (!Strings.isNullOrEmpty(template.getContent())) {
            templateInDB.setContent(template.getContent());
        }

        return Result.success(templateService.save(templateInDB));
    }

    @DeleteMapping("/template_id/{template_id}")
    public Result<?> delete(@PathVariable("template_id") ObjectId templateId, HttpServletRequest request) {
        ObjectId requesterId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        Optional<Template> templateOptional = templateService.findById(templateId);

        if (templateOptional.isEmpty()) {
            return Result.error(NO_SUCH_TEMPLATE);
        }

        Template template = templateOptional.get();
        if (!Objects.equals(template.getAuthorId(), requesterId)) {
            return Result.error(NO_PERMISSION);
        }

        templateService.deleteById(templateId);

        return Result.success();
    }
}
