package a9.service;

import a9.entity.baidu.Content;
import a9.entity.baidu.Event;
import a9.repository.ContentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
public class BaiduService {
    private final ContentRepository contentRepository;

    public BaiduService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<Event> listEvents() {
        return contentRepository.findFirstByOrderByIdDesc().getContent().stream()
                .map(Event::new)
                .collect(Collectors.toList());
    }

    public Content findContentByEventId(String eventId) {
        Optional<Content> contentOptional = contentRepository.findFirstByOrderByIdDesc().getContent().stream()
                .filter(content -> Objects.equals(content.getEvent_id(), eventId))
                .findAny();

        if (contentOptional.isEmpty()) {
            throw new RuntimeException("Data has not been fetched");
        }

        return contentOptional.get();
    }
}
