package site.iotify.userservice.domain.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewayRequest {
    private GatewayRegisterDto gateway;

    @Getter
    public static class GatewayRegisterDto {
        private String description;

        @NotBlank(message="gateway Id 입력해주세요")
        private String gatewayId;

        private Map<String, Object> location;

        private Map<String, Object> metadata;

        @NotBlank(message="name을 입력해주세요")
        private String name;

        private String statsInterval;

        private Map<String, Object> tags;

        private String tenantId;
    }
}
