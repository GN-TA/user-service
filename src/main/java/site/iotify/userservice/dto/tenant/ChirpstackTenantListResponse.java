package site.iotify.userservice.dto.tenant;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChirpstackTenantListResponse {
    private int totalCount;
    private List<TenantInfo> result;
}
