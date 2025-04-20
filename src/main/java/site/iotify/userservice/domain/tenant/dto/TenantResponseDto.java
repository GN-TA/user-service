package site.iotify.userservice.domain.tenant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cglib.core.Local;
import site.iotify.userservice.domain.tenant.entity.Tenant;
import site.iotify.userservice.domain.tenant.entity.TenantTag;
import site.iotify.userservice.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TenantResponseDto {

    private TenantResponseDto() {
    }

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
    @AllArgsConstructor
    public static class TenantWithUsersGet {
        private TenantResponseDto.TenantGetWrapped tenantInfo;
        private TenantResponseDto.TenantUsersGet tenantUsersInfo;
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

    @Getter
    public static class TenantUsersGet {
        private long totalCount;
        private List<TenantResponseDto.TenantUser> result;
    }

    @Getter
    @ToString
    public static class TenantUser {
        private String tenantId;
        private String userId;
        private LocalDateTime creatdAt;
        private LocalDateTime updatedAt;
        @JsonProperty("isAdmin")
        private boolean isAdmin;
        @JsonProperty("isDeviceAdmin")
        private boolean isDeviceAdmin;
        @JsonProperty("isGatewayAdmin")
        private boolean isGatewayAdmin;
        private String email;
    }

    @Getter
    @ToString
    public static class TenantUserGet {
        private TenantResponseDto.TenantUser tenantUser;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
