package a9.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyPhrase {
    private String word;
    private Double score;
    private String ner;
    private String pos;
    private Integer is_attention;
}
