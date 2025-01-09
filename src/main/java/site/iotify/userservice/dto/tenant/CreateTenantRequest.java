package site.iotify.userservice.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateTenantRequest {
    private TenantDto tenant;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
