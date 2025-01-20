package site.iotify.userservice.domain.gateway.exception;

import org.springframework.http.HttpStatus;

public abstract class GatewayApiException extends RuntimeException {
    public GatewayApiException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
