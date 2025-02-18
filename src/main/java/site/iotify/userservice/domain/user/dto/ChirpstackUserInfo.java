package site.iotify.userservice.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ChirpstackUserInfo {
    private String id;
    private boolean isAdmin;
    private boolean isActive;
    private String email;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeviceAdmin;
    private boolean isGatewayAdmin;
}
