package site.iotify.userservice.dto;

import lombok.Getter;
import site.iotify.userservice.entity.User;

@Getter
public class UserDto {

    private String id;
    private String username;
    private String password;
    private String email;
    private String auth;
    private String provider;

    public UserDto fromEntity(User userEntity) {
        if (userEntity == null) {
            return null;
        }
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.email = userEntity.getEmail();
        this.auth = userEntity.getAuth();
        this.provider = userEntity.getProvider();
        return this;
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .auth(auth)
                .provider(provider)
                .build();
    }
}
