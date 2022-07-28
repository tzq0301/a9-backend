package a9.service;

import a9.entity.User;
import a9.repository.UserRepository;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_USER_AVATAR_URL = "https://tzq-oos-1.oss-cn-hangzhou.aliyuncs.com/fhapp/personal/330302200103011610/SCU%20SE%20TZQ/front/test.png";

    private static final List<String> DEFAULT_USER_TAGS = Lists.newArrayList("经济", "体育", "政治", "娱乐");

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByPhone(String phone) {
        return userRepository.findUserByPhone(phone);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public Optional<User> saveUser(User user) {
        if (userRepository.findUserByPhoneOrEmail(user.getPhone(), user.getEmail()) != null) {
            return Optional.empty();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (Strings.isNullOrEmpty(user.getAvatarUrl())) {
            user.setAvatarUrl(DEFAULT_USER_AVATAR_URL);
        }

        if (user.getTags() == null || user.getTags().isEmpty()) {
            user.setTags(DEFAULT_USER_TAGS);
        }

        return Optional.of(userRepository.save(user));
    }

    public Optional<ObjectId> login(String phoneOrEmail, String password) {
        User userInDatabase = userRepository.findUserByPhoneOrEmail(phoneOrEmail, phoneOrEmail);

        if (userInDatabase == null) {
            return Optional.empty();
        }

        if (!passwordEncoder.matches(password, userInDatabase.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(userInDatabase.getId());
    }
}
