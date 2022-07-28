package a9.entity.result;

public enum ResultEnum {
    SUCCESS(0, "Success"), // 请求成功
    ERROR(1, "Error"), // 请求失败
    ILLEGAL_REQUEST(2, "Illegal request"), // 请求不正常

    // User
    ILLEGAL_USER_INFORMATION(100, "User's information are illegal"),
    USER_REGISTER_ERROR(101, "Error while saving user (duplicate phone or email)"),
    ILLEGAL_LOGIN_REQUEST(102, "Illegal login request"),
    LOGIN_FAILURE(103, "No such a user or password is wrong"),
    NO_PERMISSION(104, "No permission"),
    NO_SUCH_USER(105, "No such a user"),

    // Article
    NO_SUCH_ARTICLE(200, "No such an article"),
    TAG_DUPLICATE(201, "The article already has this tag"),

    // Template
    NO_SUCH_TEMPLATE(300, "No such a template"),
    ILLEGAL_TEMPLATE(301, "Template must have name and content"),
    ;

    private final Integer code;

    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
