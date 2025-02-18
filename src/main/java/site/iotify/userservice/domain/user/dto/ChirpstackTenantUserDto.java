package site.iotify.userservice.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ChirpstackTenantUserDto {
    private ChirpstackUserInfo tenantUser;
    private String tenantId;
}
