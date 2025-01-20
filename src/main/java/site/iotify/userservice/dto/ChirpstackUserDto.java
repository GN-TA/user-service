package site.iotify.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChirpstackUserDto {
    private ChirpstackUserInfo user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
