public class Deck {
    /**
     * Internal storage for the deck.
     */
    private Card[] deck;

    /**
     * Create a new deck.
     */
    public Deck() {
        this.deck = new Card[52];

        int pos = 0;
        for (int r = 2; r <= Card.ACE; r++) {
            for (int s = Card.CLUBS; s <= Card.SPADES; s++) {
                this.deck[pos] = new Card(r, s);
            }
        }
    }

    /**
     * Randomly shuffle the deck in-place.
     */
    public void shuffle() {
        int j; // A random number for performing the shuffle
        java.util.Random r = new java.util.Random();
        Card temp; // A temporary variable for swapping values

        // Fisher-Yates shuffle, as modified by Durstenfeld and Knuth
        for (int i = deck.length - 1; i > 0; i--) {
            j = r.nextInt(i + 1); // Generate a random integer in the range [0, i]

            // Swap this.deck[i] and this.deck[j]
            temp = this.deck[i];
            this.deck[i] = this.deck[j];
            this.deck[j] = temp;
        }
    }

    /**
     * Deal a specified number of cards and remove them from the deck.
     *
     * @param number the number of cards to deal
     * @return an array of cards of length <code>number</code>
     */
    public Card[] deal(int number) {
        if (number < 1 || number > this.deck.length) {
            throw new IllegalArgumentException("Number of cards must between 1 and " + deck.length + ".");
        }
        // Get the first n cards from the deck
        Card[] cards = new Card[number];
        for (int i = 0; i < number; i++) { cards[i] = this.deck[i]; }

        // Remove the dealt cards from the deck
        Card[] newDeck = new Card[this.deck.length - number];
        for (int i = number; i < this.deck.length; i++) { newDeck[i] = this.deck[i]; }
        this.deck = newDeck;

        return cards;
    }
}
