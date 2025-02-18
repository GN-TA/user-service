package site.iotify.userservice.domain.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.domain.gateway.dto.GatewayRequest;
import site.iotify.userservice.domain.gateway.dto.GatewayListResponse;
import site.iotify.userservice.domain.gateway.dto.GatewayResponse;
import site.iotify.userservice.domain.gateway.service.GatewayService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/gateways")
public class GatewayController {
    private final GatewayService gatewayService;

    @GetMapping
    public ResponseEntity<GatewayListResponse> getGatewayList(@RequestParam(defaultValue="0") int limit,
                                                              @RequestParam(defaultValue="0") int offset,
                                                              @RequestParam(defaultValue="0") String search,
                                                              @RequestParam String tenantId,
                                                              @RequestParam(required=false) String multicastGroupId) {


        return ResponseEntity.ok(gatewayService.getGatewayList(limit, offset, search, tenantId, multicastGroupId));
    }

    @GetMapping("/{gateway-id}")
    public ResponseEntity<GatewayResponse> getGateway(@PathVariable("gateway-id") String gatewayId) {
        return ResponseEntity.ok(gatewayService.getGateway(gatewayId));
    }


    @PostMapping
    public ResponseEntity<String> saveGateway(@RequestBody @Valid GatewayRequest gateway) {
        gatewayService.saveGateway(gateway);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{gateway-id}")
    public void updateGateway(@PathVariable("gateway-id") String gatewayId, @RequestBody @Valid GatewayRequest gateway) {
        gatewayService.updateGateway(gatewayId, gateway);
    }

    @DeleteMapping("/{gateway-id}")
    public ResponseEntity<String> deleteGateway(@PathVariable("gateway-id") String gatewayId) {
        gatewayService.deleteGateway(gatewayId);
        return ResponseEntity.status(204).build();
    }

}
