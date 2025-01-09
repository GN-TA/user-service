package site.iotify.userservice.adaptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import site.iotify.userservice.dto.tenant.ChirpstackTenantResponse;
import site.iotify.userservice.dto.tenant.CreateTenantRequest;
import site.iotify.userservice.dto.tenant.TenantDto;
import site.iotify.userservice.entity.Tenant;
import site.iotify.userservice.interceptor.LoggingInterceptor;
import site.iotify.userservice.util.HttpEntityFactory;

import java.util.List;
import java.util.Objects;

@Component
public class ChirpstackAdaptor {
    private final RestTemplate restTemplate;

    // 필드 의존성 주입, 생성자 주입으로 전환 고려 필요
    @Value("${chirpstack.host}")
    private String host;
    @Value("${chirpstack.port}")
    private int port;
    @Value("${chirpstack.admin.api}")
    private String key;

    public ChirpstackAdaptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.getInterceptors().add(new LoggingInterceptor());
    }

    public List<Tenant> getTenants(int limit, int offset, String search, String userId) {
        HttpEntity<Void> httpRequest = HttpEntityFactory.<Void>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .port(port)
                .path("/api/tenants")
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("search", search)
                .queryParam("userId", userId);
        ResponseEntity<ChirpstackTenantResponse> exchange = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpRequest,
                new ParameterizedTypeReference<ChirpstackTenantResponse>() {
                });

        return Objects.requireNonNull(exchange.getBody()).getResult();
    }

    public String createTenant(CreateTenantRequest tenantRequest) {
        HttpEntity<CreateTenantRequest> httpRequest = HttpEntityFactory.<CreateTenantRequest>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .body(tenantRequest)
                .build();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .port(port)
                .path("/api/tenants");

        ResponseEntity<String> exchange = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.POST,
                httpRequest,
                new ParameterizedTypeReference<String>() {
                }
        );

        return exchange.getBody();
    }
}
