package a9.controller;

import a9.entity.result.Result;
import a9.service.TextGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static a9.entity.result.ResultEnum.ERROR;


@RestController
@RequestMapping("/editor")
@Slf4j
public class EditorController {

    private final TextGenerationService titleGenerationService;

    public EditorController(TextGenerationService titleGenerationService) {
        this.titleGenerationService = titleGenerationService;
    }

    @PostMapping("/title_generation")
    public Result<String> titleGenerate(@RequestBody @NotNull String text) {
        var title = titleGenerationService.titleGenerate(text);

        if (title.isEmpty()) {
            return Result.error(ERROR.getCode(), "Error occurred while title generation");
        }

        return Result.success(title.get());
    }

    @PostMapping("/abstract_generation")
    public Result<String> abstractGenerate(@RequestBody @NotNull String text) {
        var title = titleGenerationService.abstractGenerate(text);

        if (title.isEmpty()) {
            return Result.error(ERROR.getCode(), "Error occurred while abstract generation");
        }

        return Result.success(title.get());
    }

}
