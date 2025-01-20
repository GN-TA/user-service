package site.iotify.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChirpstackUserInfo {
    private String userId;
    private boolean isAdmin;
    private boolean isActive;
    private String email;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeviceAdmin;
    private boolean isGatewayAdmin;
}
