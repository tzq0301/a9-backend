package a9.entity.article;

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
public class ArticleTags {
    private List<String> tags;
}
