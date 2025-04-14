package site.iotify.userservice.domain.user.dto.response;

import lombok.Getter;
import site.iotify.userservice.domain.user.dto.ChirpstackUserInfo;

import java.time.LocalDateTime;

public class ChirpstackUserResponseDto {
    @Getter
    public static class UserGet {
        private ChirpstackUserInfo user;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
