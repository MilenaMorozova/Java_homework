public class IncorrectInputException extends Exception {

    public IncorrectInputException(String message) {
        super("Incorrect input " + message);
    }
}
