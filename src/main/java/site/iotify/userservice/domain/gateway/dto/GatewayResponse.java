package site.iotify.userservice.domain.gateway.dto;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
public class GatewayResponse {
    private ZonedDateTime createdAt;
    private Gateway gateway;
    private ZonedDateTime lastUpdatedAt;
    private ZonedDateTime updatedAt;

    @Getter
    public static class Gateway {
        private String description;
        private String gatewayId;
        private Map<String, Object> location;
        private Map<String, Object> metadata;
        private String name;
        private String stateInterval;
        private Map<String, Object> tags;
        private String tenantId;
    }

}
