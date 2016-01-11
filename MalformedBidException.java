public class MalformedBidException extends Exception {
    /**
     * Create the exception with the default message.
     */
    public MalformedBidException() {
        super("Invalid bid.");
    }

    /**
     * Create the exception and add the entered string to the message.
     *
     * @param bid what the user attempted to convert to a bid
     */
    public MalformedBidException(String bid) {
        super("Invalid bid: " + bid);
    }
}
