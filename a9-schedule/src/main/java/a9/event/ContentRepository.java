package a9.event;

import a9.event.entity.Contents;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends MongoRepository<Contents, ObjectId> {
    Contents findFirstByOrderByIdDesc();
}
