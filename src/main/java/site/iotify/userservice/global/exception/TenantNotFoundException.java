package site.iotify.userservice.global.exception;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(String s) {
        super(s);
    }
}
