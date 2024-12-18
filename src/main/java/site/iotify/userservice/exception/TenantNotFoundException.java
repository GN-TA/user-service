package site.iotify.userservice.exception;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(String s) {
        super(s);
    }
}
