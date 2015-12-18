public class MalformedCardException extends RuntimeException {
    public MalformedCardException() {
        super("Invalid card.");
    }

    public MalformedCardException(String card) {
        super("Invalid card: " + card);
    }
}
