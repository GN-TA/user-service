package site.iotify.userservice.domain.rule.exception;

public class NodeRedException extends RuntimeException {

    public NodeRedException(String msg, Exception e) {
        super(msg, e);
    }
}
