package site.iotify.userservice.domain.gateway.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChirpStackRestService extends RestTemplateServiceImpl {

    @Value("${chirpstack.admin.api}")
    private String API_KEY;

    public ChirpStackRestService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public <T> HttpEntity generateRequestEntity(T requestBody) {
        headers.set("Authorization", "Bearer " + API_KEY);
        return super.generateRequestEntity(requestBody);
    }

}
