package a9.entity.baidu;

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
public class Sentiment {
    private Integer cnt;
    private Double per;
    private String sentiment;
}
