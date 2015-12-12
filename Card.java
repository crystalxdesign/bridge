public class Card {
    public static final int CLUBS    = 0;
    public static final int DIAMONDS = 1;
    public static final int HEARTS   = 2;
    public static final int SPADES   = 3;

    public static final int TEN   = 10;
    public static final int JACK  = 11;
    public static final int QUEEN = 12;
    public static final int KING  = 13;
    public static final int ACE   = 14;

    /**
     * The rank of the card.
     *
     * Must be in 2 - 12, inclusive.
     */
    private int rank;
    /**
     * The suit of the card.
     *
     * Must be one of: 0 (clubs), 1 (diamonds), 2 (hearts), or 3 (spades).
     */
    private int suit;

    /**
     * Creates the default card (ace of spades).
     */
    public Card() {
        this(12, 3);
    }

    /**
     * Creates a card from a string.
     *
     * @param cardStr a string representing the card to be created of the form `rank + suit`
     */
    public Card(String cardStr) {
        char r = cardStr.charAt(0); // First character
        char s = cardStr.charAt(1); // Second character
        int rank, suit;

        if (r >= '2' && r <= '9') { rank = (int)r - 48; } // Convert char digit to integer to get the rank
        else if (r == 't')        { rank = Card.TEN; }
        else if (r == 'j')        { rank = Card.JACK; }
        else if (r == 'q')        { rank = Card.QUEEN; }
        else if (r == 'k')        { rank = Card.KING; }
        else if (r == 'a')        { rank = Card.ACE; } // Default to ace

        if (s == 'c')      { suit = Card.CLUBS; }
        else if (s == 'd') { suit = Card.DIAMONDS; }
        else if (s == 'h') { suit = Card.HEARTS; }
        else if (s == 's') { suit = Card.SPADES; } // Default to spades

        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Creates a card from the rank and suit.
     *
     * @param rank the rank of the card, an integer in the range 2, 12 (inclusive)
     * @param suit the suit of the card, an integer in the range 0, 3 (inclusive)
     */
    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }
}
