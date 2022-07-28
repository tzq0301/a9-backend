package a9.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    private ObjectId id;

    private String phone;

    private String email;

    private String username;

    private String avatarUrl;

    private List<String> tags;

    private List<ObjectId> articleIds;

}
