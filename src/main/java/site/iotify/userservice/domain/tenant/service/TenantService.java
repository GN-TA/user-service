package site.iotify.userservice.domain.tenant.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import site.iotify.userservice.domain.tenant.dto.TenantDto;
import site.iotify.userservice.domain.tenant.dto.TenantInfo;
import site.iotify.userservice.domain.tenant.entity.Tenant;
import site.iotify.userservice.domain.user.dto.ChirpstackTenantUserDto;
import site.iotify.userservice.domain.user.dto.ChirpstackUserDto;
import site.iotify.userservice.domain.user.dto.ChirpstackUserInfo;
import site.iotify.userservice.domain.user.entity.User;
import site.iotify.userservice.domain.user.repository.UserRepository;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;
import site.iotify.userservice.global.exception.CreationFailedException;
import site.iotify.userservice.global.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TenantService {
    private final UserRepository userRepository;
    private final ChirpstackAdaptor chirpstackAdaptor;

    public TenantService(ChirpstackAdaptor chirpstackAdaptor, UserRepository userRepository) {
        this.chirpstackAdaptor = chirpstackAdaptor;
        this.userRepository = userRepository;
    }

    /**
     * 조직을 생성합니다. 생성한 유저는 해당 조직의 admin 입니다.
     *
     * @param tenantInfo
     * @param userId
     * @return
     */
    public TenantInfo registerTenant(TenantInfo tenantInfo, String userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("userId %s not found", userId));
        }
        String tenantId = chirpstackAdaptor.createTenant(
                new TenantDto(tenantInfo, LocalDateTime.now(), LocalDateTime.now())
        );
        if (Objects.isNull(tenantId)) {
            throw new CreationFailedException();
        }
        tenantInfo.setId(tenantId);
        addUserInTenant(userId, tenantId, true);

        return tenantInfo;
    }

    /**
     * 조직을 반환합니다. ids = nullable
     * ids가 null이 아니라면 해당 조직을 반환합니다.
     * ids가 null이면 전체 조직을 반환합니다.
     *
     * @param ids
     * @return List<TenantInfo>
     */
    public List<TenantInfo> getTenants(List<String> ids) {
        Map<String, TenantInfo> tenantInfoMap = chirpstackAdaptor
                .getTenants(Integer.MAX_VALUE, 0, null, null)
                .stream()
                .collect(Collectors.toMap(TenantInfo::getId, tenantInfo -> tenantInfo));
        if (ids != null) {
            return tenantInfoMap.values().stream()
                    .filter(tenantInfo -> ids.contains(tenantInfo.getId()))
                    .toList();
        } else {
            return tenantInfoMap.values().stream().toList();
        }
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
        return chirpstackAdaptor.getTenants(100, 0, null, user.getUser().getId());
    }

    /**
     * 특정 조직의 정보를 수정합니다.
     *
     * @param tenantInfo
     * @return
     */
    public void updateTenant(TenantInfo tenantInfo) {
        Tenant tenant = TenantDto.toEntity(chirpstackAdaptor.getTenant(tenantInfo.getId()));
        chirpstackAdaptor.updateTenant(new TenantDto(tenantInfo, tenant.getCreatedAt(), LocalDateTime.now()));
    }

    /**
     * 조직에 관리자/사용자를 추가합니다.
     *
     * @param userId
     * @param tenantId
     */
    public void addUserInTenant(String userId, String tenantId, boolean isAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user {%s} not found", userId)));

        chirpstackAdaptor.addUserInTenant(new ChirpstackTenantUserDto(
                new ChirpstackUserInfo(
                        user.getEmail(),
                        isAdmin,
                        true,
                        user.getEmail(),
                        null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true,
                        true
                ), tenantId));
    }
}
