package a9.entity.template;

import a9.repository.TemplateRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    @Id
    private ObjectId id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String content;

    private ObjectId authorId;

    @LastModifiedDate
    private Date lastModifiedDate;

    public Template save(TemplateRepository templateRepository) {
        return templateRepository.save(this);
    }

    public Template cleanAndSubContent() {
        content = Jsoup.clean(content, Whitelist.none());
        content = content.substring(0, Math.min(150, content.length()));
        return this;
    }
}
