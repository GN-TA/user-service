package site.iotify.userservice.domain.tenant.service;

import org.springframework.stereotype.Service;
import site.iotify.userservice.adaptor.ChirpstackAdaptor;
import site.iotify.userservice.domain.tenant.dto.TenantDto;
import site.iotify.userservice.domain.tenant.dto.TenantInfo;
import site.iotify.userservice.domain.tenant.entity.Tenant;
import site.iotify.userservice.domain.tenant.repository.TenantRepository;
import site.iotify.userservice.dto.ChirpstackUserDto;
import site.iotify.userservice.exception.CreationFailedException;
import site.iotify.userservice.exception.TenantNotFoundException;
import site.iotify.userservice.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TenantService {
    private final TenantRepository tenantRepository;
    private final ChirpstackAdaptor chirpstackAdaptor;

    public TenantService(TenantRepository tenantRepository, ChirpstackAdaptor chirpstackAdaptor) {
        this.tenantRepository = tenantRepository;
        this.chirpstackAdaptor = chirpstackAdaptor;
    }

    /**
     * 조직을 생성합니다.
     *
     * @param tenantInfo
     * @return
     */
    public TenantInfo registerTenant(TenantInfo tenantInfo) {
        TenantInfo result = TenantInfo.getDto(
                tenantRepository.save(TenantInfo.getEntity(tenantInfo))
        );
        Tenant tenant = tenantRepository.findById(result.getId()).orElseThrow(() -> new TenantNotFoundException("tenant couldn't be registered"));
        String tenantId = chirpstackAdaptor.createTenant(TenantDto.toDto(tenant));
        if (Objects.isNull(tenantId)) {
            throw new CreationFailedException();
        }
        return result;
    }

    /**
     * 조직을 반환합니다.
     *
     * @param ids
     * @return
     */
    public List<TenantInfo> getTenants(List<String> ids) {
        List<Tenant> tenants = tenantRepository.findAllById(ids);
        if (tenants.isEmpty()) {
            throw new TenantNotFoundException("Tenant with Id " + ids + " not found");
        }

        Map<String, TenantInfo> tenantInfoMap = chirpstackAdaptor
                .getTenants(Integer.MAX_VALUE, 0, null, null)
                .stream()
                .collect(Collectors.toMap(TenantInfo::getId, tenantInfo -> tenantInfo));

        return tenants.stream()
                .map(tenant -> {
                    TenantInfo tenantInfo = tenantInfoMap.get(tenant.getId());
                    if (tenantInfo == null) {
                        throw new TenantNotFoundException("Tenant with Id " + tenant.getId() + " not found (synchronization failed)");
                    } else {
                        return tenantInfo;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 사용자가 속한 조직을 반환합니다.
     *
     * @param userId
     * @return
     */
    public List<TenantInfo> getTenantsByUser(String userId) {
        ChirpstackUserDto user = chirpstackAdaptor.getUser(userId);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException(String.format("user {%s} not found in chirpstack DB", userId));
        }
        return chirpstackAdaptor.getTenants(100, 0, null, user.getUser().getUserId());
    }

    /**
     * 특정 조직의 정보를 수정합니다.
     *
     * @param tenantInfo
     * @return
     */
    public TenantInfo updateTenant(TenantInfo tenantInfo) {
        Tenant tenant = tenantRepository.findById(tenantInfo.getId()).orElseThrow(() ->
                new TenantNotFoundException("id : " + tenantInfo.getId() + " not found"));

        chirpstackAdaptor.updateTenant(new TenantDto(tenantInfo, tenant.getCreatedAt(), LocalDateTime.now()));

        return TenantInfo.getDto(
                tenantRepository.save(TenantDto.toEntity(chirpstackAdaptor.getTenant(tenantInfo.getId())))
        );
    }

    // todo user add
    public void addUserInTenant() {

    }
}
