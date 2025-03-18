package site.iotify.userservice.domain.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

public class TenantRequestDto {
    @Getter
    @AllArgsConstructor
    public static class ChirpstackTenantCreate {
        private TenantRequestDto.TenantCreate tenant;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    public static class TenantCreate {
        private String id;
        private String name;
        private String description;
        private boolean canHaveGateways;
        private boolean privateGatewaysUp;
        private boolean privateGatewaysDown;
        private int maxGatewayCount;
        private int maxDeviceCount;
        private String ip;
        private Map<String, String> tags;
    }
}
