package site.iotify.userservice.domain.tenant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @Getter
    public static class TenantUserUpdate {
        private TenantRequestDto.TenantUser tenantUser;
    }

    @Getter
    public static class TenantUser {
        private String email;
        @JsonProperty("isAdmin")
        private boolean isAdmin;
        @JsonProperty("isDeviceAdmin")
        private boolean isDeviceAdmin;
        @JsonProperty("isGatewayAdmin")
        private boolean isGatewayAdmin;
    }

    @Getter
    public static class TenantUserAdd {
        private String email;
    }

    @Getter
    @ToString
    public static class ChirpstackTenantUpdate {
        private boolean canHaveGateways;
        private String description;
        private int maxDeviceCount;
        private int maxGatewayCount;
        private String name;
        private boolean privateGatewaysDown;
        private boolean privateGatewaysUp;
        private Map<String, String> tags;
        private String ip;
    }

    @ToString
    @Getter
    public static class TenantUpdate {
        private TenantRequestDto.ChirpstackTenantUpdate tenant;
    }
}
