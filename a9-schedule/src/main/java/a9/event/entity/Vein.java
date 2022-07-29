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
public class Vein {
    private String platform_node_id;
    private String src;
    private String name;
    private String first_online_time;
    private KeyDoc key_doc;
    private Integer name_score;
    private String node_id;
    private String create_time;
    private Integer is_online;
    private Integer is_review_ok;
    private List<KeyPhrase> keyphrase;
}
