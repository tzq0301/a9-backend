package a9.config;

import a9.filter.AuthFilter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
public class AuthConfig {
    private final RedisTemplate<String, String> redisTemplate;

    public AuthConfig(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public GlobalFilter globalFilter() {
        return new AuthFilter(redisTemplate);
    }
}
