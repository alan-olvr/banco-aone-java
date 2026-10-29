package exceptions;

public class InvalidDataAccountException extends RuntimeException {
    public InvalidDataAccountException(String message) {
        super(message);
    }
}
