package a9.event.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    private String event_id;
    private String event_name;
    private List<String> vein_tag;

    public Event(Content content) {
        this.event_id = content.getEvent_id();
        this.event_name = content.getEvent_name();
        this.vein_tag = content.getVein_tag();
    }
}
