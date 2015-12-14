public class Player {
    private Card[] hand;

    /**
     * Create a player.
     *
     * @param hand an array of <code>Card</code>s of length 13
     */
    public Player(Card[] hand) {
        this.hand = hand;
    }

    /**
     * Return the card at the specified position.
     *
     * @param pos the position to retrieve
     * @return the card at <code>pos</code>
     */
    public Card getCard(int pos) {
        return this.hand[pos];
    }

    /**
     * Play a card, removing it from the hand.
     *
     * @param pos the position of the card to play
     * @return the card at <code>pos</code>
     */
    public Card playCard(int pos) {
        Card played = this.getCard(pos); // Save the card played to be returned later

        // Remove the card
        Card[] newHand = new Card[this.hand.length - 1];
        for (int i = 0; i < pos; i++) { newHand[i] = this.hand[i]; } // Copy everything up to pos
        for (int i = pos+1; i < newHand.length; i++) { newHand[i] = this.hand[i-1]; } // Skip the card at pos
        this.hand = newHand;

        return played;
    }
}
