package a9.util;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
public class DesensitizationUtils {
    /**
     * [手机固话] 电话中间隐藏，前面保留3位明文，后面保留4位明文（例子：176****6506）
     *
     * @param phone 电话号码
     * @param index 3
     * @param end   4
     * @return 脱敏后的密码
     */
    public String mobileEncrypt(String phone, int index, int end) {
        if (Strings.isBlank(phone)) {
            return "";
        }

        return phone.replaceAll(String.format("(^\\d{%s})\\d.*(\\d{%s})", index, end), "$1****$2");
    }

    /**
     * [电子邮箱] 邮箱前缀隐藏，用星号代替，第一个字符@与@后面的地址显示（例子:1******@163.com）
     *
     * @param email 电子邮箱
     * @return 脱敏后的电子邮箱
     */
    public String emailEncrypt(String email) {
        if (Strings.isBlank(email)) {
            return "";
        }

        return email.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
    }
}
