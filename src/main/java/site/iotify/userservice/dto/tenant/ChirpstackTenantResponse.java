package site.iotify.userservice.dto.tenant;

import lombok.Getter;
import lombok.Setter;
import site.iotify.userservice.entity.Tenant;

import java.util.List;

@Getter
@Setter
public class ChirpstackTenantResponse {
    private int totalCount;
    private List<Tenant> result;
}
