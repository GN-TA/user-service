package site.iotify.userservice.adaptor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import site.iotify.userservice.domain.tenant.dto.TenantDto;
import site.iotify.userservice.domain.tenant.dto.TenantInfo;
import site.iotify.userservice.domain.tenant.entity.Tenant;
import site.iotify.userservice.domain.tenant.entity.TenantTag;

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
        List<TenantInfo> tenantList = chirpstackAdaptor.getTenants(100, 0, null, "05244f12-6daf-4e1f-8315-c66783a0ab56");

        for (TenantInfo t : tenantList) {
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
        TenantDto tenantDto = new TenantDto(TenantInfo.getDto(tenant), LocalDateTime.now(), LocalDateTime.now());
        String tenantId = chirpstackAdaptor.createTenant(tenantDto);

        System.out.println(tenantId);
    }

    @Test
    void updateTenant() {
        TenantDto tenantDto = new TenantDto(
                new TenantInfo(
                        "1d12eaf1-ca63-4a50-8efc-3b50c76c98d6",
                        "qwer",
                        "qwerdecrip",
                        true,
                        false,
                        true,
                        0,
                        0,
                        "11212121",
                        null
                ),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        chirpstackAdaptor.updateTenant(tenantDto);
    }
}