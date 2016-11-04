package shared.model;

public class InvalidActionException extends Exception {
    public String message;

    public InvalidActionException(String message) {
        super();
        this.message = message;
    }

}
