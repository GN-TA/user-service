package site.iotify.userservice.domain.gateway.exception;

import org.springframework.http.HttpStatus;

public class GatewayGetRequestException extends GatewayApiException {
    public GatewayGetRequestException(String msg) {
        super(msg);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
