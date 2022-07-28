package a9.controller;

import a9.service.UserService;
import a9.entity.LoginRequest;
import a9.entity.User;
import a9.entity.result.Result;
import a9.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static a9.entity.result.ResultEnum.*;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    private final JWTUtils jwtUtils;

    public UserController(UserService userService, JWTUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error(error.getDefaultMessage()));
            return Result.error(ILLEGAL_USER_INFORMATION);
        }

        var result = userService.saveUser(user);

        if (result.isEmpty()) {
            return Result.error(USER_REGISTER_ERROR);
        }

        return Result.success(user.getUsername(), SUCCESS.getCode(), "Successful to register");
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error(error.getDefaultMessage()));
            return Result.error(ILLEGAL_LOGIN_REQUEST);
        }

        Optional<ObjectId> id = userService.login(loginRequest.getPhoneOrEmail(), loginRequest.getPassword());

        if (id.isEmpty()) {
            return Result.error(LOGIN_FAILURE);
        }

        String token = jwtUtils.create(id.get().toString());
        log.info("Create token for user {}", token);

        return Result.success(token, SUCCESS.getCode(), "Login Success");
    }
}
