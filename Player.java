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
     * Getter for specific cards.
     *
     * @param pos the position to retrieve
     * @return the card at <code>pos</code>
     */
    public Card cardAt(int pos) {
        return this.hand[pos];
    }

    /**
     * Accessor for the hand.
     *
     * @return the player's hand
     */
    public Card[] hand() {
        return this.hand;
    }

    /**
     * Find a <code>Card</code> in the hand.
     *
     * @param c the card to find
     * @return the position of the card, or -1 if not found
     */
    public int find(Card c) {
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i] == c) { return i; }
        }

        return -1;
    }

    /**
     * Play a card, removing it from the hand.
     *
     * @param pos the position of the card to play
     * @return the card at <code>pos</code>
     */
    public Card playCard(int pos) {
        Card played = this.cardAt(pos); // Save the card played to be returned later

        // Remove the card
        Card[] newHand = new Card[this.hand.length - 1];
        for (int i = 0; i < pos; i++) { newHand[i] = this.hand[i]; } // Copy everything up to pos
        for (int i = pos+1; i < newHand.length; i++) { newHand[i] = this.hand[i-1]; } // Skip the card at pos
        this.hand = newHand;

        return played;
    }
}
