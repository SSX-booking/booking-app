package mate.academy.bookingapp.exception;

public class InvalidEmailVerificationException extends RuntimeException {
    public InvalidEmailVerificationException(String message) {
        super(message);
    }
}
