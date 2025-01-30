package site.iotify.userservice.domain.user.dto.request;

import lombok.*;
import site.iotify.userservice.domain.user.dto.ChirpstackUserInfo;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class ChirpstackCreateUserRequestDto {
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
}
