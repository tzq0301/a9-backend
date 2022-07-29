package a9.repository;

import a9.entity.template.Template;
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
public interface TemplateRepository extends MongoRepository<Template, ObjectId> {
    Optional<Template> findByName(String name);

    List<Template> findAllByAuthorId(final ObjectId authorId);
}
