package site.iotify.userservice.global.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequestDetails(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponseDetails(response);

        return response;
    }

    private void logRequestDetails(HttpRequest request, byte[] body) throws IOException {
        System.out.println("===== HTTP Request =====");
        System.out.println("URI         : " + request.getURI());
        System.out.println("Method      : " + request.getMethod());
        System.out.println("Headers     : " + request.getHeaders());
        System.out.println("Request body: " + new String(body, StandardCharsets.UTF_8));
        System.out.println("========================");
    }

    private void logResponseDetails(ClientHttpResponse response) throws IOException {
        System.out.println("===== HTTP Response =====");
        System.out.println("Status code  : " + response.getStatusCode());
        System.out.println("Status text  : " + response.getStatusText());
        System.out.println("Headers      : " + response.getHeaders());

        // 응답 바디 읽기
        String responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
        System.out.println("Response body: " + responseBody);
        System.out.println("=========================");
    }
}
