package a9.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author tzq0301
 * @version 1.0
 */
@Component
@Slf4j
public class JWTUtils {

    public static final String PREFIX_OF_AUTHORIZATION = "Bearer ";

    public static final int LENGTH_OF_PREFIX_OF_AUTHORIZATION = PREFIX_OF_AUTHORIZATION.length();

    private final Algorithm algorithm;

    private final JWTCreator.Builder jwtBuilder;

    private final JWTVerifier verifier;

    private final Long expiration;

    private final RedisTemplate<String, String> redisTemplate;

    public JWTUtils(
            @Value("${jwt.algo.secret}") String secretOfAlgorithm,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.expiration}") Long expiration, RedisTemplate<String, String> redisTemplate) {
        this.algorithm = Algorithm.HMAC256(secretOfAlgorithm);
        this.jwtBuilder = JWT.create()
                .withIssuer(issuer);
        this.verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        this.expiration = expiration;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据给定信息创建 JWT
     *
     * @return JWT String
     * @throws JWTCreationException Invalid Signing configuration / Couldn't convert Claims.
     */
    public String create(final String id) throws JWTCreationException {
        var token = jwtBuilder
                .withSubject(id)
                .withExpiresAt(Date.from(Instant.now().plusSeconds(expiration)))
                .sign(algorithm);

        redisTemplate.opsForValue().set(token, token, expiration, TimeUnit.SECONDS);

        return token;
    }

    /**
     * @param token JWT
     * @throws JWTVerificationException Invalid signature/claims
     */
    public DecodedJWT verify(final String token) throws JWTVerificationException {
        if (redisTemplate.opsForValue().get(token) == null) {
            log.info("Token has not been loaded into cache yet -> {}", token);
            throw new JWTVerificationException("The token is not created by current application");
        }

        return verifier.verify(token);
    }

    public ObjectId getSubject(final String header) {
        return new ObjectId(verify(header.substring(LENGTH_OF_PREFIX_OF_AUTHORIZATION)).getSubject());
    }

    public String getToken(final String header) {
        return verify(header.substring(LENGTH_OF_PREFIX_OF_AUTHORIZATION)).getToken();
    }

    public void delete(final String token) {
        redisTemplate.delete(token);
    }
}
