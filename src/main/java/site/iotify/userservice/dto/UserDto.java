package site.iotify.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import site.iotify.userservice.entity.User;

@Getter
public class UserDto {

    private Long id;

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
