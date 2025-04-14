package site.iotify.userservice.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;
import site.iotify.userservice.domain.user.dto.ChirpstackUserInfo;
import site.iotify.userservice.domain.user.dto.UserDto;
import site.iotify.userservice.domain.user.entity.User;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class ChirpstackUserRequestDto {
    private ChirpstackUserInfo user;
    private String password;
    private List<UserTenant> tenants;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserTenant {
        private String tenantId;
        private boolean isAdmin;
        private boolean isDeviceAdmin;
        private boolean isGatewayAdmin;
    }

    @Getter
    @AllArgsConstructor
    public static class UserUpdateWrapper {
        ChirpstackUserRequestDto.UserUpdate user;
    }

    @Getter
    @AllArgsConstructor
    public static class UserUpdate {
        @Setter
        private String id;
        private String email;
        private boolean isActive;
        private boolean isAdmin;
        private String note;
        private String username;
        private String password;
        private String auth;
        private String provider;

        @JsonSetter
        public void setAdmin(boolean isAdmin) {
            this.isAdmin = false;
        }

        public static ChirpstackUserRequestDto.UserUpdate fromEntity(User userEntity) {
            if (userEntity == null) {
                return null;
            }
            return new ChirpstackUserRequestDto.UserUpdate(
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
    public static class UserPasswordUpdate {
        private String password;

    }
}
