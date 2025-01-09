package site.iotify.userservice.dto.tenant;

import lombok.*;
import site.iotify.userservice.entity.Tenant;
import site.iotify.userservice.entity.TenantTag;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class TenantDto {
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

    public static Tenant getEntity(TenantDto tenantDto) {
        Tenant tenant = Tenant.builder()
                .id(tenantDto.getId())
                .name(tenantDto.getName())
                .description(tenantDto.getDescription())
                .canHaveGateway(tenantDto.isCanHaveGateways())
                .privateGatewaysUp(tenantDto.isPrivateGatewaysUp())
                .privateGatewaysDown(tenantDto.isPrivateGatewaysDown())
                .maxDeviceCount(tenantDto.getMaxDeviceCount())
                .ip(tenantDto.getIp())
                .build();
        if (!tenantDto.getTags().isEmpty()) {
            tenant.setTags(
                    tenantDto.getTags()
                            .entrySet()
                            .stream()
                            .map(tag -> new TenantTag(0, tag.getKey(), tag.getValue(), tenant))
                            .collect(Collectors.toList())
            );
        }
        return tenant;
    }

    public static TenantDto getDto(Tenant tenant) {
        return new TenantDto(
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
}
