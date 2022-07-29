package a9.event.entity;

import lombok.AllArgsConstructor;
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
public class KeyDoc {
    private String title;
    private String url;
    private List<String> tags;
    private String publishtime;
    private Long publishTime;
    private List<String> content;
    private String summary;
    private List<KeyPhrase> keyphrase;
}
