package site.iotify.userservice.domain.tenant.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChirpstackTenantListResponse {
    private int totalCount;
    private List<TenantInfo> result;
}
