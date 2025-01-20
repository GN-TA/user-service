package site.iotify.userservice.domain.gateway.service.impl;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.iotify.userservice.domain.gateway.service.RestTemplateService;

import java.util.List;

@Service
public class RestTemplateServiceImpl implements RestTemplateService {
    private final RestTemplate restTemplate;
    protected final HttpHeaders headers;

    public RestTemplateServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.headers = new HttpHeaders();
    }

    @Override
    public HttpEntity generateRequestEntity() {
        return generateRequestEntity(null);
    }

    @Override
    public <T> HttpEntity generateRequestEntity(T requestBody) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(requestBody, headers);
    }

    @Override
    public <T> T get(String requestUrl, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                generateRequestEntity(),
                responseType
        );
        return response.getBody();
    }

    @Override
    public <T> T post(String requestUrl, Class<T> responseType, Object requestBody) {
        ResponseEntity<T> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.POST,
                generateRequestEntity(requestBody),
                responseType
        );
        return response.getBody();
    }

    @Override
    public <T> T put(String requestUrl, Class<T> responseType, Object requestBody) {
        ResponseEntity<T> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.PUT,
                generateRequestEntity(requestBody),
                responseType
        );
        return response.getBody();
    }

    @Override
    public <T> T delete(String requestUrl, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.DELETE,
                generateRequestEntity(),
                responseType
        );
        return response.getBody();
    }

}
