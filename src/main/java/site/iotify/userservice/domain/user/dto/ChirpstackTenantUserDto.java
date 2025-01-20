package site.iotify.userservice.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChirpstackTenantUserDto {
    private ChirpstackUserInfo tenantUser;
    private String tenantId;
}
