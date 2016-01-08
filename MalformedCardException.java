public class MalformedCardException extends Exception {
    /**
     * Create the exception with the default message.
     */
    public MalformedCardException() {
        super("Invalid card.");
    }

    /**
     * Create the exception and add the entered string to the message.
     *
     * @param card what the user attempted to convert to a card
     */
    public MalformedCardException(String card) {
        super("Invalid card: " + card);
    }
}
