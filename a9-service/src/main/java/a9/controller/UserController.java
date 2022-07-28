package a9.controller;

import a9.entity.result.Result;
import a9.entity.user.User;
import a9.entity.user.UserInfo;
import a9.entity.user.UserTags;
import a9.service.UserService;
import a9.util.DesensitizationUtils;
import a9.util.FileUtils;
import a9.util.JWTUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

import static a9.entity.result.ResultEnum.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    private final JWTUtils jwtUtils;

    private final FileUtils fileUtils;

    private final DesensitizationUtils desensitizationUtils;

    public UserController(UserService userService, JWTUtils jwtUtils, FileUtils fileUtils,
                          DesensitizationUtils desensitizationUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.fileUtils = fileUtils;
        this.desensitizationUtils = desensitizationUtils;
    }

    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                       HttpServletRequest request) throws IOException {
        ObjectId authorId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));

        String filepath = fileUtils.upload(authorId + "/avatar.jpg", file.getInputStream());
        return Result.success(filepath);
    }

    @GetMapping("/info")
    public Result<UserInfo> info(HttpServletRequest request) {
        ObjectId userId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        Optional<User> userById = userService.findUserById(userId);
        if (userById.isEmpty()) {
            return Result.error(NO_SUCH_USER);
        }

        User user = userById.get();
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .phone(desensitizationUtils.mobileEncrypt(user.getPhone(), 3, 4))
                .email(desensitizationUtils.emailEncrypt(user.getEmail()))
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .tags(user.getTags())
                .articleIds(user.getArticleIds())
                .build();
        return Result.success(userInfo);
    }

    @PutMapping("/tags")
    public Result<?> updateTags(@RequestBody UserTags tags, HttpServletRequest request) {
        if (tags == null || tags.getTags() == null) {
            return Result.error(ILLEGAL_REQUEST);
        }

        ObjectId userId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        Optional<User> userById = userService.findUserById(userId);
        if (userById.isEmpty()) {
            return Result.error(NO_SUCH_USER);
        }

        userService.updateTags(userId, tags.getTags());

        return Result.success();
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        String token = jwtUtils.getToken(request.getHeader(AUTHORIZATION));
        jwtUtils.delete(token);
        return Result.success();
    }

    @PatchMapping("/")
    public Result<?> update(@RequestBody User user, HttpServletRequest request) {
        ObjectId userId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        if (user.getId() != null && userId != user.getId()) {
            return Result.error(NO_PERMISSION);
        }

        Optional<User> userById = userService.findUserById(userId);
        if (userById.isEmpty()) {
            return Result.error(NO_SUCH_USER);
        }

        User userInDatabase = userById.get();

        if (!Strings.isNullOrEmpty(user.getUsername())) {
            userInDatabase.setUsername(user.getUsername());
        }

        if (!Strings.isNullOrEmpty(user.getPhone())) {
            userInDatabase.setPhone(user.getPhone());
        }

        if (!Strings.isNullOrEmpty(user.getEmail())) {
            userInDatabase.setEmail(user.getEmail());
        }

        if (!Strings.isNullOrEmpty(user.getAvatarUrl())) {
            userInDatabase.setAvatarUrl(user.getAvatarUrl());
        }

        if (!Strings.isNullOrEmpty(user.getPassword())) {
            userInDatabase.setPassword(user.getPassword());
            userService.update(userInDatabase);
        } else {
            userService.updateWithoutPassword(userInDatabase);
        }

        return Result.success();
    }
}
