// This class is for the custom error if the user enters something they are not supposed to 
public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
        // Default constructor
    }
    // Constructor for custom exception
    public InvalidInputException(String reason) {
        super(reason);
    }
}