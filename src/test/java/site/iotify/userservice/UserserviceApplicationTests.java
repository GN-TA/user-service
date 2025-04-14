package site.iotify.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import site.iotify.userservice.domain.user.service.MinioService;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;

@SpringBootTest
@ActiveProfiles("test")
class UserserviceApplicationTests {
    @MockBean
    MinioService minioService;
    @MockBean
    ChirpstackAdaptor chirpstackAdaptor;

    @Test
    void contextLoads() {
    }

}
