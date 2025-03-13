package site.iotify.userservice.domain.tenant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/tenants")
    public ResponseEntity<String> createTenant(@RequestHeader("X-USER-ID") String userId,
                                               @RequestBody TenantRequestDto.TenantCreate tenantDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.registerTenant(tenantDto, userId));
    }

    @GetMapping("/tenant/{tenant-id}")
    public ResponseEntity<TenantResponseDto.TenantGet> getTenant(@PathVariable("tenant-id") String tenantId) {
        return ResponseEntity.status(HttpStatus.OK).body(tenantService.getTenant(tenantId));
    }
}
