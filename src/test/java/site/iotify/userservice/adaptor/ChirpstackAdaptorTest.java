package site.iotify.userservice.adaptor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import site.iotify.userservice.dto.tenant.CreateTenantRequest;
import site.iotify.userservice.dto.tenant.TenantDto;
import site.iotify.userservice.entity.Tenant;
import site.iotify.userservice.entity.TenantTag;

import java.time.LocalDateTime;
import java.util.List;

class ChirpstackAdaptorTest {
    private static ChirpstackAdaptor chirpstackAdaptor;

    @BeforeAll
    public static void setup() {
        chirpstackAdaptor = new ChirpstackAdaptor(new RestTemplate());
        ReflectionTestUtils.setField(chirpstackAdaptor, "host", "http://192.168.70.203");
        ReflectionTestUtils.setField(chirpstackAdaptor, "port", 8090);
        ReflectionTestUtils.setField(chirpstackAdaptor, "key", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjaGlycHN0YWNrIiwiaXNzIjoiY2hpcnBzdGFjayIsInN1YiI6ImVhZmJmNmE3LWNiM2YtNGE2OC05OGM4LTNkMWU2ODVhZGNjOSIsInR5cCI6ImtleSJ9.FkIF-TRZc7jG4DiqrSwDejfNvnHxb0ynGSJ2uCkkRAk");
    }

    @Test
    void getTenants() {
        List<Tenant> tenantList = chirpstackAdaptor.getTenants(100, 0, null, "05244f12-6daf-4e1f-8315-c66783a0ab56");

        for (Tenant t : tenantList) {
            System.out.println(t.getId());
        }
    }

    @Test
    void createTenant() {
        Tenant tenant = Tenant.builder()
                .id("asdf114")
                .name("asdfff1134")
                .description("asdfdescrip")
                .canHaveGateway(true)
                .privateGatewaysUp(false)
                .privateGatewaysDown(true)
                .maxDeviceCount(0)
                .ip("asdfasdf")
                .build();
        tenant.setTags(List.of(new TenantTag(0, "asdf", "asdf", tenant)));
        CreateTenantRequest createTenantRequest = new CreateTenantRequest(TenantDto.getDto(tenant), LocalDateTime.now(), LocalDateTime.now());
        String tenantId = chirpstackAdaptor.createTenant(createTenantRequest);

        System.out.println(tenantId);
    }
}