package a9.manager;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TextGenerationManager {

    private final String titleGenerationServiceUrl;

    private final String abstractGenerationServiceUrl;

    private final RestTemplate restTemplate;

    public TextGenerationManager(RestTemplate restTemplate,
                                 @Value("${tg.url}") String textGenerationServiceUrl) {
        this.restTemplate = restTemplate;
        this.titleGenerationServiceUrl = String.format("%s/title_generation", textGenerationServiceUrl);
        this.abstractGenerationServiceUrl = String.format("%s/abstract_generation", textGenerationServiceUrl);
    }

    public Optional<String> titleGenerate(String text) {
        if (Strings.isNullOrEmpty(text)) {
            throw new RuntimeException("Text cannot be null or empty");
        }

        return Optional.ofNullable(restTemplate.postForObject(titleGenerationServiceUrl, text, String.class))
                .map(title -> title.substring(1, title.length() - 1));
    }

    public Optional<String> abstractGenerate(String text) {
        if (Strings.isNullOrEmpty(text)) {
            throw new RuntimeException("Text cannot be null or empty");
        }

        return Optional.ofNullable(restTemplate.postForObject(abstractGenerationServiceUrl, text, String.class))
                .map(title -> title.substring(1, title.length() - 1));
    }

}
