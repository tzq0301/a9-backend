package a9.entity;

import a9.repository.UserRepository;
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
