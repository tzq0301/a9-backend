package a9.entity.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CleanedTemplate extends Template {
    private String cleanedContent;

    public CleanedTemplate(Template template) {
        super(template.getId(), template.getName(), template.getContent(),
                template.getAuthorId(), template.getLastModifiedDate());
        this.cleanedContent = Jsoup.clean(this.getContent(), Whitelist.none());
        this.cleanedContent = this.cleanedContent.replaceAll("&nbsp;", "");
        this.cleanedContent = this.cleanedContent.substring(0, Math.min(150, this.cleanedContent.length()));
    }
}
