package site.iotify.userservice.domain.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.iotify.userservice.domain.tenant.entity.Tenant;
import site.iotify.userservice.domain.tenant.entity.TenantTag;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class TenantDto {
    private TenantInfo tenant;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TenantDto toDto(Tenant tenant) {
        return new TenantDto(new TenantInfo(
                tenant.getId(),
                tenant.getName(),
                tenant.getDescription(),
                tenant.isCanHaveGateway(),
                tenant.isPrivateGatewaysUp(),
                tenant.isPrivateGatewaysDown(),
                tenant.getMaxGatewayCount(),
                tenant.getMaxDeviceCount(),
                tenant.getIp(),
                tenant.getTags().stream().collect(Collectors.toMap(TenantTag::getKey, TenantTag::getValue))
        ), tenant.getCreatedAt(), tenant.getUpdatedAt());
    }

    public static Tenant toEntity(TenantDto tenantDto) {
        TenantInfo tenantInfo = tenantDto.getTenant();
        Tenant tenantEntity = Tenant.builder()
                .id(tenantInfo.getId())
                .name(tenantInfo.getName())
                .description(tenantInfo.getDescription())
                .canHaveGateway(tenantInfo.isPrivateGatewaysUp())
                .privateGatewaysUp(tenantInfo.isPrivateGatewaysUp())
                .privateGatewaysDown(tenantInfo.isPrivateGatewaysDown())
                .maxGatewayCount(tenantInfo.getMaxGatewayCount())
                .maxDeviceCount(tenantInfo.getMaxDeviceCount())
                .ip(tenantInfo.getIp())
                .build();
        if (!tenantInfo.getTags().isEmpty()) {
            tenantEntity.setTags(
                    tenantInfo.getTags()
                            .entrySet()
                            .stream()
                            .map(tag -> new TenantTag(0, tag.getKey(), tag.getValue(), tenantEntity))
                            .collect(Collectors.toList())
            );
        }
        return tenantEntity;
    }
}
