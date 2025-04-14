package site.iotify.userservice.adaptor;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import site.iotify.userservice.domain.user.service.MinioService;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;

@SpringBootTest
class ChirpstackAdaptorTest {
    @MockBean
    MinioService minioService;
    @InjectMocks
    ChirpstackAdaptor chirpstackAdaptor; // or mock whatever its dependencies are


    @Test
    void getUser() {
        String id = "e4cb2550-d93c-4627-968d-fdfd113da28d";
        System.out.println(chirpstackAdaptor.getUser(id));
    }
}