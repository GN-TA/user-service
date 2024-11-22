package site.iotify.userservice.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String userId) {
        super("User with id " + userId + " already exists");
    }
}
