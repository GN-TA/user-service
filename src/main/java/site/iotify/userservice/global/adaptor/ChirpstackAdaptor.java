package site.iotify.userservice.global.adaptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import site.iotify.userservice.domain.tenant.dto.*;
import site.iotify.userservice.domain.user.dto.ChirpstackTenantUserDto;
import site.iotify.userservice.domain.user.dto.ChirpstackUserDto;
import site.iotify.userservice.domain.user.dto.UserDto;
import site.iotify.userservice.domain.user.dto.response.ChirpstackUserListResponse;
import site.iotify.userservice.domain.user.dto.request.ChirpstackUserRequestDto;
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
    public List<TenantResponseDto.TenantGet> getTenants(int limit, int offset, String search, String userId) {
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

        ResponseEntity<TenantResponseDto.ChirpstackTenantListGet> exchange = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpRequest,
                new ParameterizedTypeReference<TenantResponseDto.ChirpstackTenantListGet>() {
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

        ResponseEntity<TenantResponseDto.ChirpstackTenantListGet> exchange = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                httpRequest,
                new ParameterizedTypeReference<TenantResponseDto.ChirpstackTenantListGet>() {
                });

        return Objects.requireNonNull(exchange.getBody()).getTotalCount();
    }

    public String createTenant(TenantRequestDto.ChirpstackTenantCreate tenantDto) {
        HttpEntity<TenantRequestDto.ChirpstackTenantCreate> httpRequest = HttpEntityFactory.<TenantRequestDto.ChirpstackTenantCreate>builder()
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

    public TenantResponseDto.TenantGetWrapped getTenant(String id) {
        HttpEntity<Void> httpEntity = HttpEntityFactory.<Void>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .build();

        UriComponentsBuilder uriComponentBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .path(String.format("/api/tenants/%s", id));

        ResponseEntity<TenantResponseDto.TenantGetWrapped> tenantDto = restTemplate.exchange(
                uriComponentBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<TenantResponseDto.TenantGetWrapped>() {
                }
        );
        return tenantDto.getBody();
    }

    public void updateTenant(TenantRequestDto.ChirpstackTenantCreate tenantDto) {
        HttpEntity<TenantRequestDto.ChirpstackTenantCreate> httpEntity = HttpEntityFactory.<TenantRequestDto.ChirpstackTenantCreate>builder()
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

    public String addUsersInNetwork(String ip, ChirpstackUserRequestDto chirpstackUserRequestDto) {
        String targetHost = host;
        if (ip != null) {
            targetHost = ip;
        }
        chirpstackUserRequestDto.getUser().setAdmin(false);

        HttpEntity<ChirpstackUserRequestDto> httpEntity = HttpEntityFactory.<ChirpstackUserRequestDto>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .body(chirpstackUserRequestDto)
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

    public TenantResponseDto.TenantUsersGet getTenantUsers(int limit, int offset, String tenantId) {
        HttpEntity<Void> httpEntity = HttpEntityFactory.<Void>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/tenants/{tenantId}/users")
                .queryParam("limit", limit)
                .queryParam("offset", offset);

        String url = uriComponentsBuilder.buildAndExpand(tenantId).toUriString();
        ResponseEntity<TenantResponseDto.TenantUsersGet> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<TenantResponseDto.TenantUsersGet>() {
                });

        return response.getBody();
    }

    public TenantResponseDto.TenantUserGet getTenantUser(String tenantId, String userId) {
        HttpEntity<Void> httpEntity = HttpEntityFactory.<Void>builder()
                .contentType(MediaType.APPLICATION_JSON)
                .setBearerAuth(key)
                .build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(host)
                .path("/api/tenants/{tenantId}/users/{userId}");


        return restTemplate.exchange(
                        uriComponentsBuilder.buildAndExpand(tenantId, userId).toUriString(),
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<TenantResponseDto.TenantUserGet>() {
                        })
                .getBody();
    }

    public void deleteUserInTenant(String tenantId, String userId) {
        restTemplate.exchange(
                UriComponentsBuilder
                        .fromHttpUrl(host)
                        .path("/api/tenants/{tenantId}/users/{userId}")
                        .buildAndExpand(tenantId, userId)
                        .toUriString(),
                HttpMethod.DELETE,
                HttpEntityFactory.<Void>builder()
                        .setBearerAuth(key)
                        .build(),
                new ParameterizedTypeReference<Void>() {
                });
    }

    public void updateUserInTenant(String tenantId, String userId, TenantRequestDto.TenantUserUpdate tenantUserUpdate) {
        restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(host)
                        .path("/api/tenants/{tenantId}/users/{userId}")
                        .buildAndExpand(tenantId, userId)
                        .toUriString(),
                HttpMethod.PUT,
                HttpEntityFactory.builder()
                        .contentType(MediaType.APPLICATION_JSON)
                        .setBearerAuth(key)
                        .body(tenantUserUpdate)
                        .build(),
                new ParameterizedTypeReference<TenantRequestDto.TenantUserUpdate>() {
                }
        );
    }

    public void updateUser(String userId, ChirpstackUserRequestDto.UserUpdateWrapper user) {
        restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(host)
                        .path("/api/users/{userId}")
                        .buildAndExpand(userId)
                        .toUriString(),
                HttpMethod.PUT,
                HttpEntityFactory.builder()
                        .contentType(MediaType.APPLICATION_JSON)
                        .setBearerAuth(key)
                        .body(user)
                        .build(),
                new ParameterizedTypeReference<Void>() {
                }
        );
    }

    public void deleteTenant(String tenantId) {
        restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(host)
                        .path("/api/tenants/{tenantId}")
                        .buildAndExpand(tenantId)
                        .toUriString(),
                HttpMethod.DELETE,
                HttpEntityFactory.builder().setBearerAuth(key).build(),
                new ParameterizedTypeReference<Void>() {
                }
        );
    }
}
