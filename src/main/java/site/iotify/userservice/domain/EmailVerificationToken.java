package site.iotify.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailVerificationToken implements Serializable {

    private String token;
    private String email;
    private LocalDateTime expireAt;

    public EmailVerificationToken(String token, String email, LocalDateTime expireAt) {
        this.token = token;
        this.email = email;
        this.expireAt = expireAt;
    }

    public boolean isExpired() {
        return expireAt.isBefore(LocalDateTime.now());
    }
}
