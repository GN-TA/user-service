package site.iotify.userservice.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import site.iotify.userservice.domain.user.entity.User;

public class UserRequestDto {
    private UserRequestDto() {
    }

    @Getter
    @ToString
    public static class UserPasswordChange {
        @NotBlank
        private String id;
        @NotBlank
        private String email;
        @NotBlank
        private String currentPassword;
        @NotBlank
        @Setter
        private String newPassword;
        @NotBlank
        private String confirmPassword;

    }

    @Getter
    @AllArgsConstructor
    public static class UserSave {
        @Setter
        private String id;
        private String email;
        private boolean isActive;
        private boolean isAdmin;
        private String note;
        private String username;
        @Setter
        private String password;
        private String auth;
        private String provider;

        @JsonSetter
        public void setAdmin(boolean isAdmin) {
            this.isAdmin = false;
        }

        public static UserRequestDto.UserSave fromEntity(User userEntity) {
            if (userEntity == null) {
                return null;
            }
            return new UserRequestDto.UserSave(
                    userEntity.getId(),
                    userEntity.getEmail(),
                    userEntity.isActive(),
                    userEntity.isAdmin(),
                    userEntity.getNote(),
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.getAuth(),
                    userEntity.getProvider()
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
                    .isActive(isActive)
                    .isAdmin(isAdmin)
                    .note(note)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class UserUpdate {
        @Setter
        private String id;
        private String email;
        private boolean isActive;
        private String note;
        private String username;
        private String auth;
    }
}
