package structures.tester;

public class WrongImplementationException extends Exception{

    public WrongImplementationException(String message, long seed) {
        super(message);
    }
}
