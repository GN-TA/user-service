package site.iotify.userservice.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import site.iotify.userservice.domain.EmailVerificationToken;
import java.util.Set;

@Repository
public class EmailVerificationTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public EmailVerificationTokenRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String token, EmailVerificationToken emailToken) {
        redisTemplate.opsForValue().set(token, emailToken);
    }

    public EmailVerificationToken findByToken(String token) {
        Object value = redisTemplate.opsForValue().get(token);
        if (value instanceof EmailVerificationToken) {
            return (EmailVerificationToken) value;
        } else {
            throw new IllegalStateException();
        }
    }

    public void delete(String token) {
        redisTemplate.delete(token);
    }

    public EmailVerificationToken findByEmail(String email) {
        Set<String> keys = redisTemplate.keys("*");

        if (keys != null) {
            for (String key : keys) {
                Object value = redisTemplate.opsForValue().get(key);
                if (value instanceof EmailVerificationToken) {
                    EmailVerificationToken token = (EmailVerificationToken) value;
                    if (token.getEmail().equals(email)) {
                        return token;
                    }
                }
            }
        }
        return null;
    }
}
