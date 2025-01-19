package site.iotify.userservice.domain.tenant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.domain.tenant.dto.TenantInfo;
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
    @GetMapping("/tenants/{userId}")
    public ResponseEntity<List<TenantInfo>> getTenants(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(tenantService.getTenantsByUser(userId));
    }

    @PostMapping("/tenants/{userId}")
    public ResponseEntity<String> createTenant(@PathVariable String userId,
                                               @RequestBody TenantInfo tenantInfo) {

        ResponseEntity.status(HttpStatus.CREATED).body(tenantService.registerTenant(tenantInfo).getId());

        return null;
    }

}
