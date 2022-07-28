package a9.repository;

import a9.entity.article.Article;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, ObjectId> {

    Optional<Article> findByIdAndDeletedFalse(ObjectId id);

    List<Article> findAllByAuthorIdAndDeletedFalse(ObjectId id);

}
