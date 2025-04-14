package site.iotify.userservice.adaptor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;

@ActiveProfiles("test")
@SpringBootTest
class ChirpstackAdaptorTest {

    @Autowired
    private ChirpstackAdaptor chirpstackAdaptor;

    @Test
    void getUser() {
        String id = "e4cb2550-d93c-4627-968d-fdfd113da28d";
        System.out.println(chirpstackAdaptor.getUser(id));
    }
}