package site.iotify.userservice.domain.gateway.service;

import site.iotify.userservice.domain.gateway.dto.GatewayListResponse;
import site.iotify.userservice.domain.gateway.dto.GatewayRequest;
import site.iotify.userservice.domain.gateway.dto.GatewayResponse;

public interface GatewayService {
    GatewayListResponse getGatewayList(int limit, int offset, String search, String tenantId, String multicastGroupId);

    GatewayResponse getGateway(String gatewayId);

    void saveGateway(GatewayRequest gateway);

    void updateGateway(String gatewayId, GatewayRequest gateway);

    void deleteGateway(String gatewayId);
}
