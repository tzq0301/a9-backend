package a9.service;

import a9.entity.template.Template;
import a9.repository.TemplateRepository;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {
    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public Template save(Template template) {
        return template.save(templateRepository);
    }

    public Optional<Template> findByName(String name) {
        return templateRepository.findByName(name);
    }

    public Optional<Template> findById(ObjectId templateId) {
        return templateRepository.findById(templateId);
    }

    public List<Template> findAllByAuthorId(ObjectId authorId) {
        List<Template> templatesOfAuthor = templateRepository.findAllByAuthorId(authorId);
        if (templatesOfAuthor == null) {
            templatesOfAuthor = Lists.newArrayList();
        }
        return templatesOfAuthor;
    }

    public void deleteById(ObjectId templateId) {
        templateRepository.deleteById(templateId);
    }
}
