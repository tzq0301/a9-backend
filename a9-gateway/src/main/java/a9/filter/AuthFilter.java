package a9.filter;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author tzq0301
 * @version 1.0
 */
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    private static final String BEARER = "Bearer ";

    private final RedisTemplate<String, String> redisTemplate;

    public AuthFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();

        final String path = request.getPath().toString();
        log.info("Request for {}", path);
        if (isLoginPath(path) || isRegisterPath(path)) {
            return chain.filter(exchange);
        }

        final String authorizationHeader = request.getHeaders().getFirst(AUTHORIZATION);
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(BEARER)) {
            log.info("Invalid Header");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(UNAUTHORIZED);
            return response.setComplete();
        }

        final String token = authorizationHeader.substring(BEARER.length());
        String tokenInRedis = redisTemplate.opsForValue().get(token);

        if (Strings.isNullOrEmpty(tokenInRedis)) {
            log.info("Invalid Token");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    private boolean isLoginPath(final String path) {
        return "/user/login".equals(path);
    }

    private boolean isRegisterPath(final String path) {
        return "/user/register".equals(path);
    }
}
