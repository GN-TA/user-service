package site.iotify.userservice.domain.gateway.service;

import org.springframework.http.HttpEntity;

public interface RestTemplateService {
    public HttpEntity generateRequestEntity();

    <T> HttpEntity generateRequestEntity(T requestBody);

    public <T> T get(String requestUrl, Class<T> responseType);
    public <T> T post(String requestUrl, Class<T> responseType, Object requestBody);
    public <T> T put(String requestUrl, Class<T> responseType, Object requestBody);
    public <T> T delete(String requestUrl, Class<T> responseType);
}
