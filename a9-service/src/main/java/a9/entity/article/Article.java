package a9.entity.article;

import a9.entity.user.User;
import a9.repository.ArticleRepository;
import a9.repository.UserRepository;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document("article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    private ObjectId id;

    private String title;

    private String abstraction;

    private String content;

    private ObjectId authorId;

    private List<String> tags;

    private boolean deleted;

    @LastModifiedDate
    private Date lastModifiedDate;

    public void save(final ArticleRepository articleRepository) {
        articleRepository.save(this);
    }

    public boolean addTag(final ArticleRepository articleRepository, final String tag) {
        if (Objects.isNull(tags)) {
            tags = Lists.newArrayList();
        }

        if (tags.contains(tag)) {
            return false;
        }

        tags.add(tag);
        articleRepository.save(this);

        return true;
    }

    public boolean delete(final ArticleRepository articleRepository) {
        if (deleted) {
            return false;
        }

        deleted = true;

        articleRepository.save(this);

        return true;
    }

    public Article cleanAndSubContent() {
        content = Jsoup.clean(content, Whitelist.none());
        content = content.replaceAll("&nbsp;", "");
        content = content.substring(0, Math.min(150, content.length()));
        return this;
    }

    public void setTags(List<String> tags, ArticleRepository articleRepository, UserRepository userRepository) {
        Optional<User> authorById = userRepository.findById(authorId);

        if (authorById.isEmpty()) {
            return;
        }

        User author = authorById.get();

        if (tags == null) {
            tags = Lists.newArrayList();
        }

        // 处理新增 tags：级联增加用户 tags
        List<String> tagsToBeAdded = tags.stream()
                .filter(tag -> !author.getTags().contains(tag))
                .collect(Collectors.toList());
        author.getTags().addAll(tagsToBeAdded);
        author.setTags(author.getTags(), userRepository, articleRepository);

        this.tags = tags;
        articleRepository.save(this);

        // 处理删除 tags：如果该文章是用户所有文章中唯一拥有该 tag 的文章，此时需要删除用户相应的 tag
        author.removeUnusedTags(userRepository, articleRepository);
        userRepository.save(author);
    }

    public void removeTagByUser(final String tag, ArticleRepository articleRepository) {
        tags.remove(tag);
        articleRepository.save(this);
    }

}
