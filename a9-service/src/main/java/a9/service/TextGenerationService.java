package a9.service;

import a9.manager.TextGenerationManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TextGenerationService {

    private final TextGenerationManager textGenerationManager;

    public TextGenerationService(TextGenerationManager textGenerationManager) {
        this.textGenerationManager = textGenerationManager;
    }

    public Optional<String> titleGenerate(String text) {
        return textGenerationManager.titleGenerate(text);
    }

    public Optional<String> abstractGenerate(String text) {
        return textGenerationManager.abstractGenerate(text);
    }
}
