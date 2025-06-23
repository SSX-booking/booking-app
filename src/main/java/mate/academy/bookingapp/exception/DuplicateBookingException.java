package mate.academy.bookingapp.exception;

public class DuplicateBookingException extends RuntimeException {

    public DuplicateBookingException(String message) {
        super(message);
    }
}
