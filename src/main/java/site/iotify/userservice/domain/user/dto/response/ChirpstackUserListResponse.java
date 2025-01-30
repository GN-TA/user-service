package site.iotify.userservice.domain.user.dto.response;

import lombok.Getter;
import lombok.Setter;
import site.iotify.userservice.domain.user.dto.ChirpstackUserInfo;

import java.util.List;

@Getter
@Setter
public class ChirpstackUserListResponse {
    private int totalCount;
    private List<ChirpstackUserInfo> result;
}
