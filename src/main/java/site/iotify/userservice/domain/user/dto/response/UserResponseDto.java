package site.iotify.userservice.domain.user.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import site.iotify.userservice.domain.user.dto.UserDto;
import site.iotify.userservice.domain.user.entity.User;

public class UserResponseDto {
    private UserResponseDto() {
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class UserGet {

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
        private boolean isAdmin;
        private boolean isActive;
        private String note;

        public static UserResponseDto.UserGet fromEntity(User userEntity) {
            if (userEntity == null) {
                return null;
            }
            return new UserResponseDto.UserGet(
                    userEntity.getId(),
                    userEntity.getEmail(),
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.getAuth(),
                    userEntity.getProvider(),
                    userEntity.isAdmin(),
                    userEntity.isActive(),
                    userEntity.getNote()
            );
        }

        public User toEntity() {
            return User.builder()
                    .id(id)
                    .email(email)
                    .username(username)
                    .password(password)
                    .auth(auth)
                    .provider(provider)
                    .note(note)
                    .isAdmin(isAdmin)
                    .isActive(isActive)
                    .build();
        }
    }
}
