package site.iotify.userservice.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found with id " + id);
    }

    public UserNotFoundException(String email) {
        super("Could not find user with email " + email);
    }
}
