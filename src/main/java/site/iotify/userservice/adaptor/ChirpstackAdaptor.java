package site.iotify.userservice.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import site.iotify.userservice.entity.Tenant;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChirpstackAdaptor {
    private final RestTemplate restTemplate;
    @Value("${chirpstack.host}")
    private String host;
    @Value("${chirpstack.port}")
    private int port;


}
