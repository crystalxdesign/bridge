import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Card implements Comparable<Card> {
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
    public Card() { this(12, 3); }

    /**
     * Creates a card from a string.
     *
     * @param cardStr a string representing the card to be created of the form `rank + suit`
     * @throws MalformedCardException if the passed string doesn't represent a card
     */
    public Card(String cardStr) {
        cardStr = cardStr.toLowerCase(); // The conversion is case-insensitive

        // Split the string into two parts
        Matcher cardMatch = Pattern.compile("^(?<rank>[2-9]|10|[TtJjQqKkAa])(?<suit>[CcDdHhSs])$").matcher(cardStr);
        if (!cardMatch.matches()) { // Validate the input
            throw new MalformedCardException(cardStr);
        }

        String rankStr = cardMatch.group("rank");
        String suitStr = cardMatch.group("suit");

        // Convert the strings entered to chars
        char r; // Two options for r: 2-9/t or 10, the latter case is equivalent to t
        if (rankStr.length() == 1) { r = rankStr.charAt(0); }
        else { r = 't'; }

        char s = suitStr.charAt(0);

        if (r >= '2' && r <= '9') { rank = (int)r - 48; } // Convert char digit to integer to get the rank
        else if (r == 't')        { rank = Card.TEN; }
        else if (r == 'j')        { rank = Card.JACK; }
        else if (r == 'q')        { rank = Card.QUEEN; }
        else if (r == 'k')        { rank = Card.KING; }
        else if (r == 'a')        { rank = Card.ACE; }

        if (s == 'c')      { suit = Card.CLUBS; }
        else if (s == 'd') { suit = Card.DIAMONDS; }
        else if (s == 'h') { suit = Card.HEARTS; }
        else if (s == 's') { suit = Card.SPADES; }

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

    /**
     * Accessor for the rank of the card.
     *
     * @return the rank of the card
     */
    public int rank() { return this.rank; }

    /**
     * Accessor for the suit of the card.
     *
     * @return one of {@code Card.CLUBS}, {@code Card,DAIMONDS}, {@code Card.HEARTS}, and {@code Card.SPADES}
     */
    public int suit() { return this.suit; }

    /**
     * Convert the {@code Card} to a string.
     * If {@code c} is a {@code Card} and {@code Card c1 = new Card(c.toString())},
     * the two objects should be equivalent.
     *
     * @return a string
     */
    public String toString() {
        String rankStr, suitStr;
        if (this.rank >= 2 && this.rank <= 9) { rankStr = new Integer(this.rank).toString(); }
        else if (this.rank == Card.TEN)       { rankStr = "10"; }
        else if (this.rank == Card.JACK)      { rankStr = "J"; }
        else if (this.rank == Card.QUEEN)     { rankStr = "Q"; }
        else if (this.rank == Card.KING)      { rankStr = "K"; }
        else                                  { rankStr = "A"; }

        if (this.suit == Card.CLUBS)         { suitStr = "C"; }
        else if (this.suit == Card.DIAMONDS) { suitStr = "D"; }
        else if (this.suit == Card.HEARTS)   { suitStr = "H"; }
        else                                 { suitStr = "S"; }

        return rankStr + suitStr;
    }

    public boolean equals(Card c) {
        return c.rank() == this.rank && c.suit() == this.suit;
    }

    /**
     * Find whether one card is smaller, equal to, or greater than another.
     * A card is smaller than another one if one of the following is satisfied:
     * - The suit of the former is less than the suit of the latter
     * - The suits are equal and the suit of the former is less than that of
     *   the latter
     * Two cards are equal if their suits and ranks are equal. Otherwise, the
     * first card is larger than the second.
     *
     * @param c the card to compare to this one
     * @return {@literal <} 0 if the first card is less than the second, 0 if they are
     *         equal, {@literal >} 0 otherwise.
     * @see java.lang.Comparable#compareTo
     */
    @Override
    public int compareTo(Card c) {
        int result = this.suit() - c.suit();
        if (result == 0) { result = this.rank() - c.rank(); }

        return result;
    }
}
