package site.iotify.userservice.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import site.iotify.userservice.domain.user.entity.User;

@Getter
@ToString
public class UserDto {

    @Setter
    private String id;

    @NotBlank
    private String email;

    @Setter
    private String username;

    @Setter
    private String password;

    @Setter
    private String auth;

    private String provider;

    public UserDto fromEntity(User userEntity) {
        if (userEntity == null) {
            return null;
        }
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.auth = userEntity.getAuth();
        this.provider = userEntity.getProvider();
        return this;
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .email(email)
                .username(username)
                .password(password)
                .auth(auth)
                .provider(provider)
                .build();
    }
}
