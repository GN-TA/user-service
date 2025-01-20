package site.iotify.userservice.domain.gateway.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import site.iotify.userservice.domain.gateway.dto.GatewayListResponse;
import site.iotify.userservice.domain.gateway.dto.GatewayRequest;
import site.iotify.userservice.domain.gateway.dto.GatewayResponse;
import site.iotify.userservice.domain.gateway.exception.GatewayGetRequestException;
import site.iotify.userservice.domain.gateway.service.GatewayService;
import site.iotify.userservice.domain.gateway.service.RestTemplateService;

@Service
@RequiredArgsConstructor
public class GatewayServiceImpl implements GatewayService {
    private final RestTemplateService chirpStackRestService;
    private final String PATH_PREFIX = "/api/gateways";

    @Value("${chirpstack.host}")
    private String API_HOST;

    @Value("${chirpstack.port}")
    private String API_PORT;

    @Override
    public GatewayListResponse getGatewayList(int limit, int offset, String search, String tenantId, String multicastGroupId) {
        if (limit < 0 || offset < 0) {
            throw new GatewayGetRequestException("limit과 offset에 정수를 입력해주세요");
        }
        if (tenantId.trim().isEmpty()) {
            throw new GatewayGetRequestException("TenantI Id를 입력해주세요");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_HOST).port(API_PORT).path(PATH_PREFIX);
        if (limit > 0) {
            if (limit == 0) {
                limit = chirpStackRestService.get(uriBuilder.toUriString(), GatewayListResponse.class).getTotalCount();
            }
            uriBuilder.queryParam("limit", limit);
        }
        if (offset > 0) {
            uriBuilder.queryParam("offset", offset);
        }
        if (!search.trim().isEmpty()) {
            uriBuilder.queryParam("search", search);
        }
        return chirpStackRestService.get(
                uriBuilder.toUriString(),
                GatewayListResponse.class
        );
    }

    @Override
    public GatewayResponse getGateway(String gatewayId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_HOST).port(API_PORT).path(PATH_PREFIX).path("gatewayId");
        return chirpStackRestService.get(
                uriBuilder.toUriString(),
                GatewayResponse.class
        );
    }

    @Override
    public void saveGateway(GatewayRequest gateway) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_HOST).port(API_PORT).path(PATH_PREFIX);
        chirpStackRestService.post(
                uriBuilder.toUriString(),
                Void.class,
                gateway
        );
    }

    @Override
    public void updateGateway(String gatewayId, GatewayRequest gateway) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_HOST).port(API_PORT).path(PATH_PREFIX).path("/").path(gatewayId);
        chirpStackRestService.post(
                uriBuilder.toUriString(),
                Void.class,
                gateway
        );
    }

    @Override
    public void deleteGateway(String gatewayId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_HOST).port(API_PORT).path(PATH_PREFIX).path("/").path(gatewayId);
        chirpStackRestService.delete(
                uriBuilder.toUriString(),
                Void.class
        );
    }
}
