package site.iotify.userservice.global.adaptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import site.iotify.userservice.domain.tenant.dto.ChirpstackTenantListResponse;
import site.iotify.userservice.domain.tenant.dto.TenantDto;
import site.iotify.userservice.domain.tenant.dto.TenantInfo;
import site.iotify.userservice.domain.user.dto.ChirpstackTenantUserDto;
import site.iotify.userservice.domain.user.dto.ChirpstackUserDto;
import site.iotify.userservice.domain.user.dto.response.ChirpstackUserListResponse;
import site.iotify.userservice.domain.user.dto.request.ChirpstackCreateUserRequestDto;
import site.iotify.userservice.global.exception.TenantNotFoundException;
import site.iotify.userservice.global.util.HttpEntityFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ChirpstackAdaptor {
    private final RestTemplate restTemplate;

    // 필드 의존성 주입, 생성자 주입으로 전환 고려 필요
    @Value("${chirpstack.host}")
    private String host;
    @Value("${chirpstack.admin.api}")
    private String key;

    public ChirpstackAdaptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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

    public int getTenantTotalCount() {
        HttpEntity<Void> httpRequest = HttpEntityFactory.<Void>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .path("/api/tenants");

        ResponseEntity<ChirpstackTenantListResponse> exchange = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpRequest,
                new ParameterizedTypeReference<ChirpstackTenantListResponse>() {
                });

        return Objects.requireNonNull(exchange.getBody()).getTotalCount();
    }

    public String createTenant(TenantDto tenantDto) {
        HttpEntity<TenantDto> httpRequest = HttpEntityFactory.<TenantDto>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .body(tenantDto)
                .build();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .path("/api/tenants");

        ResponseEntity<Map<String, String>> exchange = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.POST,
                httpRequest,
                new ParameterizedTypeReference<Map<String, String>>() {
                }
        );
        Map<String, String> responseMap = exchange.getBody();
        if (responseMap == null) {
            throw new TenantNotFoundException(String.format("it couldn't save the Tenant : %s", tenantDto.getTenant().getId()));
        }
        return responseMap.get("id");
    }

    public TenantDto getTenant(String id) {
        HttpEntity<Void> httpEntity = HttpEntityFactory.<Void>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .build();
        UriComponentsBuilder uriComponentBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .path(String.format("/api/tenants/%s", id));
        restTemplate.getForEntity(uriComponentBuilder.toUriString(),
                TenantDto.class);
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
                .path(String.format("/api/users/%s", id));

        ResponseEntity<ChirpstackUserDto> response = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<ChirpstackUserDto>() {
                });

        return response.getBody();
    }

    public void addUserInTenant(ChirpstackTenantUserDto chirpstackTenantUserDto) {
        HttpEntity<ChirpstackTenantUserDto> httpEntity = HttpEntityFactory.<ChirpstackTenantUserDto>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .body(chirpstackTenantUserDto)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .path(String.format("/api/tenants/%s/users", chirpstackTenantUserDto.getTenantId()));

        restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.POST,
                httpEntity,
                Void.class);
    }

    public ChirpstackUserListResponse getUsersInNetwork(String ip, int limit, int offset) {
        String targetHost = host;
        if (ip != null) {
            targetHost = ip;
        }

        HttpEntity<Void> httpEntity = HttpEntityFactory.<Void>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(targetHost)
                .path("/api/users")
                .queryParam("limit", limit)
                .queryParam("offset", offset);

        ResponseEntity<ChirpstackUserListResponse> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<ChirpstackUserListResponse>() {
                });
        return response.getBody();
    }

    public String addUsersInNetwork(String ip, ChirpstackCreateUserRequestDto chirpstackCreateUserRequestDto) {
        String targetHost = host;
        if (ip != null) {
            targetHost = ip;
        }
        chirpstackCreateUserRequestDto.getUser().setAdmin(false);

        HttpEntity<ChirpstackCreateUserRequestDto> httpEntity = HttpEntityFactory.<ChirpstackCreateUserRequestDto>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .body(chirpstackCreateUserRequestDto)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(targetHost)
                .path("/api/users");
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });

        return Objects.requireNonNull(response.getBody()).get("id");
    }


}
