package site.iotify.userservice.domain.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.iotify.userservice.domain.tenant.entity.Tenant;
import site.iotify.userservice.domain.tenant.entity.TenantTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TenantResponseDto {
    @Getter
    public static class ChirpstackTenantListGet {
        private int totalCount;
        private List<TenantGet> result;
    }

    @Getter
    @AllArgsConstructor
    public static class TenantGetWrapped {
        private TenantResponseDto.TenantGet tenant;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    public static class TenantGet {
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
