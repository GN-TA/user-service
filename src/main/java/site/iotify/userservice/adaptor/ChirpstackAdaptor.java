package site.iotify.userservice.adaptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import site.iotify.userservice.domain.tenant.dto.ChirpstackTenantListResponse;
import site.iotify.userservice.domain.tenant.dto.TenantDto;
import site.iotify.userservice.domain.tenant.dto.TenantInfo;
import site.iotify.userservice.dto.ChirpstackTenantUserDto;
import site.iotify.userservice.dto.ChirpstackUserDto;
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

    /**
     * chirpstack rest api에 조직 리스트를 조회하는 메서드입니다.
     *
     * @param limit  검색결과 갯수 제한 파라미터
     * @param offset 검색결과 중 offset만큼 건너뛰고 조회하고자 할때 사용하는 파라미터
     * @param search 검색하고자 하는 조직 정보, null 전달 시 쿼리 파라미터로 입력 되지 않음
     * @param userId 유저가 속한 조직 정보를 조회하기 위한 파라미터, null 전달 시 쿼리 파라미터로 입력 되지 않고 모든 조직 검색
     * @return
     */
    public List<TenantInfo> getTenants(int limit, int offset, String search, String userId) {
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
        ResponseEntity<ChirpstackTenantListResponse> exchange = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpRequest,
                new ParameterizedTypeReference<ChirpstackTenantListResponse>() {
                });

        return Objects.requireNonNull(exchange.getBody()).getResult();
    }

    public String createTenant(TenantDto tenantDto) {
        HttpEntity<TenantDto> httpRequest = HttpEntityFactory.<TenantDto>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .body(tenantDto)
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

    public TenantDto getTenant(String id) {
        HttpEntity<Void> httpEntity = HttpEntityFactory.<Void>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .build();
        UriComponentsBuilder uriComponentBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .port(port)
                .path(String.format("/api/tenants/%s", id));
        ResponseEntity<TenantDto> tenantDto = restTemplate.exchange(
                uriComponentBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<TenantDto>() {
                }
        );
        return tenantDto.getBody();
    }

    public void updateTenant(TenantDto tenantDto) {
        HttpEntity<TenantDto> httpEntity = HttpEntityFactory.<TenantDto>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .body(tenantDto)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .port(port)
                .path(String.format("/api/tenants/%s", tenantDto.getTenant().getId()));

        restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.PUT,
                httpEntity,
                Void.class);
    }

    public ChirpstackUserDto getUser(String id) {
        HttpEntity<Void> httpEntity = HttpEntityFactory.<Void>builder()
                .setBearerAuth(key)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .port(port)
                .path(String.format("/api/users/%s", id));

        ResponseEntity<ChirpstackUserDto> response = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<ChirpstackUserDto>() {
                });

        return response.getBody();
    }

    // todo add user in tenant
    public void addUserInTenant(ChirpstackTenantUserDto chirpstackTenantUserDto) {
        HttpEntity<ChirpstackTenantUserDto> httpEntity = HttpEntityFactory.<ChirpstackTenantUserDto>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .body(chirpstackTenantUserDto)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .port(port)
                .path(String.format("/api/tenants/%s/users", chirpstackTenantUserDto.getTenantUser().getUserId()));

        restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.POST,
                httpEntity,
                Void.class);
    }
}
