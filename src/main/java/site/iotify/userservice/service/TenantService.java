package site.iotify.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.iotify.userservice.adaptor.ChirpstackAdaptor;
import site.iotify.userservice.dto.TenantDto;
import site.iotify.userservice.entity.Tenant;
import site.iotify.userservice.exception.TenantNotFoundException;
import site.iotify.userservice.repository.TenantRepository;

import java.util.List;
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
     * @param tenantDto
     * @return
     */
    public TenantDto registerTenant(TenantDto tenantDto) {
        return TenantDto.getDto(
                tenantRepository.save(tenantDto.getEntity(tenantDto))
        );
    }

    /**
     * 조직을 반환합니다.
     *
     * @param ids
     * @return
     */
    public List<TenantDto> getTenants(List<Integer> ids) {
        List<Tenant> tenants = tenantRepository.findAllById(ids);
        if (tenants.isEmpty()) {
            throw new TenantNotFoundException("Tenant with Id " + ids + " not found");
        }

        return tenants.stream()
                .map(TenantDto::getDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 조직의 정보를 수정합니다.
     *
     * @param tenantDto
     * @return
     */
    public TenantDto updateTenant(TenantDto tenantDto) {
        Tenant tenant = tenantRepository.findById(tenantDto.getTenantId()).orElseThrow(() ->
                new TenantNotFoundException("id : " + tenantDto.getTenantId() + " not found"));

        return TenantDto.getDto(
                tenantRepository.save(TenantDto.getDto(tenant).getEntity(tenantDto))
        );
    }
}
