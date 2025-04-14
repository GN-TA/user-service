package site.iotify.userservice.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        @JsonProperty("isAdmin")
        private boolean isAdmin;
        @JsonProperty("isActive")
        private boolean isActive;
        private String note;
        @Setter
        private String profileImageUrl;

        public static UserResponseDto.UserGet fromEntity(User userEntity) {
            if (userEntity == null) {
                return null;
            }
            String profileImageUrl = null;
            if (userEntity.getProfileImage() != null) {
                profileImageUrl = userEntity.getProfileImage();
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
                    userEntity.getNote(),
                    profileImageUrl
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
