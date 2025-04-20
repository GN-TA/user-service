package site.iotify.userservice.domain.tenant.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.iotify.userservice.domain.tenant.dto.TenantRequestDto;
import site.iotify.userservice.domain.tenant.dto.TenantResponseDto;
import site.iotify.userservice.domain.user.dto.ChirpstackTenantUserDto;
import site.iotify.userservice.domain.user.dto.ChirpstackUserInfo;
import site.iotify.userservice.domain.user.dto.response.ChirpstackUserResponseDto;
import site.iotify.userservice.domain.user.entity.User;
import site.iotify.userservice.domain.user.repository.UserRepository;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;
import site.iotify.userservice.global.exception.CreationFailedException;
import site.iotify.userservice.global.exception.UnAuthorizedException;
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
    public String registerTenant(TenantRequestDto.TenantCreate tenantInfo, String userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("userId %s not found", userId));
        }
        String tenantId = chirpstackAdaptor.createTenant(
                new TenantRequestDto.ChirpstackTenantCreate(tenantInfo, LocalDateTime.now(), LocalDateTime.now())
        );
        if (Objects.isNull(tenantId)) {
            throw new CreationFailedException();
        }
        tenantInfo.setId(tenantId);
        addUserInTenant(userId, tenantId, true);

        return tenantId;
    }

    /**
     * 조직을 반환합니다. ids = nullable
     * ids가 null이 아니라면 해당 조직을 반환합니다.
     * ids가 null이면 전체 조직을 반환합니다.
     *
     * @param ids
     * @return List<TenantResponseDto.TenantGet>
     */
    public List<TenantResponseDto.TenantGet> getTenants(List<String> ids) {
        Map<String, TenantResponseDto.TenantGet> tenantInfoMap = chirpstackAdaptor
                .getTenants(Integer.MAX_VALUE, 0, null, null)
                .stream()
                .collect(Collectors.toMap(TenantResponseDto.TenantGet::getId, tenantGet -> tenantGet));
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
    public List<TenantResponseDto.TenantGet> getTenantsByUser(String userId) {
        ChirpstackUserResponseDto.UserGet user = chirpstackAdaptor.getUser(userId);
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
    public void updateTenant(TenantRequestDto.TenantCreate tenantDto) {
        TenantResponseDto.TenantGetWrapped tenantGetWrapped = chirpstackAdaptor.getTenant(tenantDto.getId());
        chirpstackAdaptor.updateTenant(new TenantRequestDto.ChirpstackTenantCreate(tenantDto, tenantGetWrapped.getCreatedAt(), LocalDateTime.now()));
    }

    public void addUser(TenantRequestDto.TenantUserAdd tenantUserAdd, String tenantId, boolean isAdmin) {
        User user = userRepository.findByEmail(tenantUserAdd.getEmail())
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", tenantUserAdd.getEmail())));

        addUserInTenant(user.getId(), tenantId, isAdmin);
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

    /**
     * 단일 조직 정보를 가져오는 서비스
     *
     * @param tenantId
     * @return
     */
    public TenantResponseDto.TenantWithUsersGet getTenant(String tenantId, Pageable pageable) {
        TenantResponseDto.TenantGetWrapped tenantGetWrapped = chirpstackAdaptor.getTenant(tenantId);

        int offset = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() * pageable.getPageSize() - pageable.getPageSize();
        int limit = pageable.getPageNumber() == 0 ? pageable.getPageSize() : pageable.getPageSize() * pageable.getPageNumber();

        TenantResponseDto.TenantUsersGet tenantUsersGet = chirpstackAdaptor.getTenantUsers(limit, offset, tenantId);
        // todo : 여기 게트웨이 리스트도 rest로 가져와서 아이디 리스트만 넣고 보내기
        return new TenantResponseDto.TenantWithUsersGet(tenantGetWrapped, tenantUsersGet);
    }

    /**
     * 조직 내에 유저를 삭제합니다.
     *
     * @param userId
     * @param id
     * @return
     */
    public void removeUser(String userId, String tenantId, String targetUserId) {
        TenantResponseDto.TenantUserGet user = chirpstackAdaptor.getTenantUser(tenantId, userId);
        if (!user.getTenantUser().isAdmin()) {
            throw new UnAuthorizedException();
        }
        chirpstackAdaptor.deleteUserInTenant(tenantId, targetUserId);
    }

    public void updateUser(String tenantId, String targetUserId, TenantRequestDto.TenantUserUpdate tenantUserUpdate) {
        chirpstackAdaptor.updateUserInTenant(tenantId, targetUserId, tenantUserUpdate);
    }

    public void deleteTenant(String tenantId) {
        chirpstackAdaptor.deleteTenant(tenantId);
    }
}
