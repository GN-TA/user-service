package site.iotify.userservice.domain.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cglib.core.Local;
import site.iotify.userservice.domain.tenant.entity.Tenant;
import site.iotify.userservice.domain.tenant.entity.TenantTag;
import site.iotify.userservice.domain.user.entity.User;

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
    public static class TenantUser {
        private String tenantId;
        private String userId;
        private LocalDateTime creatdAt;
        private LocalDateTime updatedAt;
        private String email;
        private boolean isAdmin;
        private boolean isDeviceAdmin;
        private boolean isGatewayAdmin;
    }
}
