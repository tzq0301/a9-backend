package a9.repository;

import a9.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findUserByPhone(String phone);

    User findUserByEmail(String email);

    User findUserByPhoneOrEmail(String phone, String email);

}
