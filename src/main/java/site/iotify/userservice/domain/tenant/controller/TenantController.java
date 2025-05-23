package site.iotify.userservice.domain.tenant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.global.annotations.TenantAdminOnly;
import site.iotify.userservice.domain.tenant.dto.TenantRequestDto;
import site.iotify.userservice.domain.tenant.dto.TenantResponseDto;
import site.iotify.userservice.domain.tenant.service.TenantService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class TenantController {
    private final TenantService tenantService;

    /**
     * 사용자가 속한 조직 리스트를 조회합니다.
     *
     * @return
     */
    @GetMapping("/tenants")
    public ResponseEntity<List<TenantResponseDto.TenantGet>> getTenants(@RequestHeader("X-USER-ID") String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(tenantService.getTenantsByUser(userId));
    }

    /**
     * 조직 생성합니다
     *
     * @param userId
     * @param tenantDto
     * @return
     */
    @PostMapping("/tenants")
    public ResponseEntity<String> createTenant(@RequestHeader("X-USER-ID") String userId,
                                               @RequestBody TenantRequestDto.TenantCreate tenantDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.registerTenant(tenantDto, userId));
    }

    /**
     * 단일 조직의 상세 정보 조회합니다
     *
     * @param tenantId
     * @param pageable
     * @return
     */
    @GetMapping("/tenant/{tenant-id}")
    public ResponseEntity<TenantResponseDto.TenantWithUsersGet> getTenant(@PathVariable("tenant-id") String tenantId,
                                                                          @PageableDefault Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(tenantService.getTenant(tenantId, pageable));
    }

    /**
     * 조직 정보를 업데이트 합니다.
     *
     * @param tenantId
     * @param tenantUpdate
     * @return
     */
    @TenantAdminOnly
    @PostMapping("/tenant/{tenant-id}")
    public ResponseEntity<Void> updateTenant(@PathVariable("tenant-id") String tenantId,
                                             @RequestBody TenantRequestDto.TenantUpdate tenantUpdate) {
        tenantService.updateTenant(tenantId, tenantUpdate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * tenant 의 유저를 삭제합니다.
     *
     * @param userId
     * @param tenantId
     * @param targetUserId
     * @return
     */
    @TenantAdminOnly
    @DeleteMapping("/tenant/{tenant-id}/user/{user-id}")

    public ResponseEntity<Void> removeUser(@RequestHeader(name = "X-USER-ID") String userId,
                                           @PathVariable(name = "tenant-id") String tenantId,
                                           @PathVariable(name = "user-id") String targetUserId) {
        tenantService.removeUser(userId, tenantId, targetUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 조직의 사용자를 수정합니다.
     *
     * @param tenantId
     * @param targetUserId
     * @param tenantUserUpdate
     * @return
     */
    @TenantAdminOnly
    @PutMapping("/tenant/{tenant-id}/users/{user-id}")
    public ResponseEntity<Void> updateUser(@PathVariable(name = "tenant-id") String tenantId,
                                           @PathVariable(name = "user-id") String targetUserId,
                                           @RequestBody TenantRequestDto.TenantUserUpdate tenantUserUpdate) {
        tenantService.updateUser(tenantId, targetUserId, tenantUserUpdate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @TenantAdminOnly
    @PostMapping("/tenant/{tenant-id}/users")
    public ResponseEntity<Void> addUser(@PathVariable(name = "tenant-id") String tenantId,
                                        @RequestBody TenantRequestDto.TenantUserAdd tenantUserAdd) {
        tenantService.addUser(tenantUserAdd, tenantId, false);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @TenantAdminOnly
    @DeleteMapping("/tenant/{tenant-id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable(name = "tenant-id") String tenantId) {
        tenantService.deleteTenant(tenantId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
