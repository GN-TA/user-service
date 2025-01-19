package site.iotify.userservice.domain.tenant.dto;

import lombok.*;
import site.iotify.userservice.domain.tenant.entity.Tenant;
import site.iotify.userservice.domain.tenant.entity.TenantTag;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class TenantInfo {
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

    public static Tenant getEntity(TenantInfo tenantInfo) {
        Tenant tenant = Tenant.builder()
                .id(tenantInfo.getId())
                .name(tenantInfo.getName())
                .description(tenantInfo.getDescription())
                .canHaveGateway(tenantInfo.isCanHaveGateways())
                .privateGatewaysUp(tenantInfo.isPrivateGatewaysUp())
                .privateGatewaysDown(tenantInfo.isPrivateGatewaysDown())
                .maxDeviceCount(tenantInfo.getMaxDeviceCount())
                .ip(tenantInfo.getIp())
                .build();
        if (!tenantInfo.getTags().isEmpty()) {
            tenant.setTags(
                    tenantInfo.getTags()
                            .entrySet()
                            .stream()
                            .map(tag -> new TenantTag(0, tag.getKey(), tag.getValue(), tenant))
                            .collect(Collectors.toList())
            );
        }
        return tenant;
    }

    public static TenantInfo getDto(Tenant tenant) {
        return new TenantInfo(
                tenant.getId(),
                tenant.getName(),
                tenant.getDescription(),
                tenant.isCanHaveGateway(),
                tenant.isPrivateGatewaysUp(),
                tenant.isPrivateGatewaysDown(),
                tenant.getMaxGatewayCount(),
                tenant.getMaxDeviceCount(),
                tenant.getIp(),
                tenant.getTags().stream()
                        .collect(Collectors.toMap(TenantTag::getKey, TenantTag::getValue))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantInfo that = (TenantInfo) o;
        return isCanHaveGateways() == that.isCanHaveGateways()
                && isPrivateGatewaysUp() == that.isPrivateGatewaysUp()
                && isPrivateGatewaysDown() == that.isPrivateGatewaysDown()
                && getMaxGatewayCount() == that.getMaxGatewayCount()
                && getMaxDeviceCount() == that.getMaxDeviceCount()
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), isCanHaveGateways(), isPrivateGatewaysUp(), isPrivateGatewaysDown(), getMaxGatewayCount(), getMaxDeviceCount());
    }
}
