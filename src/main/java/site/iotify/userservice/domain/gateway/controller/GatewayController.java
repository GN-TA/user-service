package site.iotify.userservice.domain.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.domain.gateway.dto.GatewayRequest;
import site.iotify.userservice.domain.gateway.dto.GatewayListResponse;
import site.iotify.userservice.domain.gateway.dto.GatewayResponse;
import site.iotify.userservice.domain.gateway.service.GatewayService;

/**
 * Gateway 정보를 관리하는 REST 컨트롤러입니다.
 * <p>
 * 게이트웨이의 조회, 등록, 수정, 삭제 등의 기능을 제공합니다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/gateways")
public class GatewayController {
    private final GatewayService gatewayService;

    /**
     * 게이트웨이 목록을 조회합니다.
     *
     * @param limit           조회할 데이터 개수 (기본값: 0, 전체 조회)
     * @param offset          조회 시작 위치 (기본값: 0)
     * @param search          게이트웨이 이름 또는 ID로 검색 (기본값: 빈 문자열)
     * @param tenantId        테넌트 ID (필수)
     * @param multicastGroupId 멀티캐스트 그룹 ID (선택)
     * @return 조회된 게이트웨이 목록
     */
    @GetMapping
    public ResponseEntity<GatewayListResponse> getGatewayList(@RequestParam(defaultValue="0") int limit,
                                                              @RequestParam(defaultValue="0") int offset,
                                                              @RequestParam(defaultValue="") String search,
                                                              @RequestParam String tenantId,
                                                              @RequestParam(required=false) String multicastGroupId) {
        return ResponseEntity.ok(gatewayService.getGatewayList(limit, offset, search, tenantId, multicastGroupId));
    }

    /**
     * 특정 게이트웨이의 상세 정보를 조회합니다.
     *
     * @param gatewayId 조회할 게이트웨이 ID
     * @return 해당 게이트웨이의 상세 정보
     */
    @GetMapping("/{gateway-id}")
    public ResponseEntity<GatewayResponse> getGateway(@PathVariable("gateway-id") String gatewayId) {
        return ResponseEntity.ok(gatewayService.getGateway(gatewayId));
    }

    /**
     * 새로운 게이트웨이를 등록합니다.
     *
     * @param gateway 등록할 게이트웨이 정보
     * @return 성공 시 201 Created 상태 반환
     */
    @PostMapping
    public ResponseEntity<String> saveGateway(@RequestBody @Valid GatewayRequest gateway) {
        gatewayService.saveGateway(gateway);
        return ResponseEntity.status(201).build();
    }

    /**
     * 기존 게이트웨이 정보를 수정합니다.
     *
     * @param gatewayId 수정할 게이트웨이의 ID
     * @param gateway   수정할 게이트웨이 정보
     */
    @PutMapping("/{gateway-id}")
    public void updateGateway(@PathVariable("gateway-id") String gatewayId,
                              @RequestBody @Valid GatewayRequest gateway) {
        gatewayService.updateGateway(gatewayId, gateway);
    }

    /**
     * 특정 게이트웨이를 삭제합니다.
     *
     * @param gatewayId 삭제할 게이트웨이의 ID
     * @return 성공 시 204 No Content 상태 반환
     */
    @DeleteMapping("/{gateway-id}")
    public ResponseEntity<String> deleteGateway(@PathVariable("gateway-id") String gatewayId) {
        gatewayService.deleteGateway(gatewayId);
        return ResponseEntity.status(204).build();
    }
}
