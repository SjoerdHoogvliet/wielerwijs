package nl.hu.wielerwijs.data.Exceptions;

public class AlreadyVotedException extends RuntimeException {
    public AlreadyVotedException(String message) {
        super(message);
    }
}
