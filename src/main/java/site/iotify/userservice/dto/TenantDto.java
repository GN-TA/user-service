package site.iotify.userservice.dto;

import lombok.*;
import site.iotify.userservice.entity.Tenant;

@Data
@AllArgsConstructor
public class TenantDto {
    private int tenantId;
    private String tenantName;
    private String description;
    private boolean hasGateway;
    private int gatewayMaxCount;
    private int deviceMaxCount;
    private String ip;

    public Tenant getEntity(TenantDto tenantDto) {
        return Tenant.builder()
                .id(tenantDto.getTenantId())
                .name(tenantDto.getTenantName())
                .description(tenantDto.getDescription())
                .hasGateway(tenantDto.isHasGateway())
                .deviceMaxCount(tenantDto.getDeviceMaxCount())
                .ip(tenantDto.getIp())
                .build();
    }

    public static TenantDto getDto(Tenant tenant) {
        return new TenantDto(
                tenant.getId(),
                tenant.getName(),
                tenant.getDescription(),
                tenant.isHasGateway(),
                tenant.getGatewayMaxCount(),
                tenant.getDeviceMaxCount(),
                tenant.getIp()
        );
    }
}
