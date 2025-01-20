package site.iotify.userservice.domain.gateway.dto;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class GatewayListResponse {
    private List<GatewayInfoDto> result;
    private int totalCount;

    public static class GatewayInfoDto {
        private ZonedDateTime createdAt;
        private String description;
        private String gatewayId;
        private ZonedDateTime lastUpdatedAt;
        private Map<String, Object> location;
        private String name;
        private Map<String, Object> properties;
        private String state;
        private String tenantId;
        private ZonedDateTime updatedAt;
    }

}
