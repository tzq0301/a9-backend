package a9.entity.user;

import a9.entity.article.Article;
import a9.repository.ArticleRepository;
import a9.repository.UserRepository;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tzq0301
 * @version 1.0
 */
@Document("user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @Size(min = 11, max = 11)
    @NotBlank
    @NotNull
    private String phone;

    @Indexed(unique = true)
    @Email
    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String password;

    @NotBlank
    @NotNull
    private String username;

    private String avatarUrl;

    private List<String> tags;

    private List<ObjectId> articleIds;

    public void saveArticle(UserRepository userRepository, ObjectId articleId) {
        if (articleIds == null) {
            articleIds = Lists.newArrayList();
        }

        if (articleIds.contains(articleId)) {
            return;
        }

        articleIds.add(articleId);
        userRepository.save(this);
    }

    public void deleteArticle(UserRepository userRepository, ObjectId articleId) {
        if (articleIds == null || articleIds.isEmpty()) {
            return;
        }

        articleIds.remove(articleId);
        userRepository.save(this);
    }

    public List<Article> listArticles(ArticleRepository articleRepository) {
        if (articleIds == null || articleIds.isEmpty()) {
            articleIds = Lists.newArrayList();
        }

        return articleIds.stream()
                .map(articleRepository::findByIdAndDeletedFalse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void setTags(List<String> tags, UserRepository userRepository, ArticleRepository articleRepository) {
        List<Article> articles = listArticles(articleRepository);
        // 级联删除
        this.tags.stream()
                .filter(tag -> !tags.contains(tag))
                .forEach(tag -> articles.forEach(article -> article.removeTagByUser(tag, articleRepository)));

        this.tags = tags;
        userRepository.save(this);
    }

    public void removeUnusedTags(UserRepository userRepository, ArticleRepository articleRepository) {
        List<String> tagsOfArticlesOfUser = getArticleIds().stream()
                .map(articleRepository::findByIdAndDeletedFalse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Article::getTags)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
        List<String> tagsToBeDeleted = tags.stream()
                .filter(tag -> !tagsOfArticlesOfUser.contains(tag))
                .collect(Collectors.toList());
        tags.removeAll(tagsToBeDeleted);
        userRepository.save(this);
    }

    public User save(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
        userRepository.save(this);
        return this;
    }

    public User save(UserRepository userRepository) {
        userRepository.save(this);
        return this;
    }
}
