package a9.repository;

import a9.entity.baidu.Contents;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author tzq0301
 * @version 1.0
 */
@Repository
public interface ContentRepository extends MongoRepository<Contents, ObjectId> {
    Contents findFirstByOrderByIdDesc();
}
