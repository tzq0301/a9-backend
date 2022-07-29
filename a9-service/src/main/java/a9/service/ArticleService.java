package a9.service;

import a9.entity.article.Article;
import a9.entity.user.User;
import a9.manager.TencentCloudManager;
import a9.repository.ArticleRepository;
import a9.repository.UserRepository;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final TencentCloudManager tencentCloudManager;

    public ArticleService(ArticleRepository articleRepository,
                          UserRepository userRepository,
                          TencentCloudManager tencentCloudManager) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.tencentCloudManager = tencentCloudManager;
    }

    public ObjectId save(User user, Article article) {
        article.save(articleRepository);
        user.saveArticle(userRepository, article.getId());
        return article.getId();
    }

    public Optional<Article> findArticleById(ObjectId id) {
        return articleRepository.findByIdAndDeletedFalse(id);
    }

    public Optional<Article> findArticleById(String id) {
        return findArticleById(new ObjectId(id));
    }

    public Optional<ObjectId> addTag(Article article, String tag) {
        boolean addTagSuccessfully = article.addTag(articleRepository, tag);

        if (!addTagSuccessfully) {
            return Optional.empty();
        }

        return Optional.of(article.getId());
    }

    public Optional<ObjectId> delete(User author, Article article) {
        boolean deleteArticleSuccessfully = article.delete(articleRepository);

        if (!deleteArticleSuccessfully) {
            return Optional.empty();
        }

        author.deleteArticle(userRepository, article.getId());

        return Optional.of(article.getId());
    }

    public List<Article> listArticlesByAuthorId(ObjectId id) {
        return articleRepository.findAllByAuthorIdAndDeletedFalse(id);
    }

    public ObjectId update(Article article) {
        article.setTags(article.getTags(), articleRepository, userRepository);
        return articleRepository.save(article).getId();
    }

    public void updateTags(Article article, List<String> tags) {
        article.setTags(tags, articleRepository, userRepository);
    }

    public String checkTextCorrection(String text) throws TencentCloudSDKException {
        return tencentCloudManager.checkTextCorrection(text).getResultText();
    }


}
