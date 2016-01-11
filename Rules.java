public class Rules {
    public static final int CLUBS    = 0;
    public static final int DIAMONDS = 1;
    public static final int HEARTS   = 2;
    public static final int SPADES   = 3;
    public static final int NOTRUMP  = 4;

    public static final int TEN   = 10;
    public static final int JACK  = 11;
    public static final int QUEEN = 12;
    public static final int KING  = 13;
    public static final int ACE   = 14;

    public static final int NORTH = 0;
    public static final int EAST  = 1;
    public static final int SOUTH = 2;
    public static final int WEST  = 3;

    /**
     * Check if a card has the same suit as the lead.
     *
     * @param check the {@code Card} to test
     * @param lead the {@code Card} lead, or {@code null} if this is the first
     *             card of the trick
     * @return {@code true} if the suits of the two cards are the same,
     *         {@code false} otherwise
     */
    public static boolean followsSuit(Card check, Card lead) {
        int leadSuit = lead == null ? check.suit() : lead.suit(); // Any suit can be played if this is the card lead

        return check.suit() == leadSuit;
    }

    /**
     * Check if it is legal to play a card.
     *
     * The card is legal if the following conditions are met:
     * - The card is in the player's hand.
     * - The suit of the card is the same as the suit of the lead, unless either of the following are true:
     *  - The card being played is the lead
     *  - The player can't follow suit
     *
     * @param check the {@code card} to test
     * @param p the person playing the card, before it's removed from their hand
     * @param lead the {@code Card} lead, or {@code null} if this is the first
     *             card of the trick
     * @return {@code true} if this is a legal play, {@code false} otherwise
     */
    public static boolean playable(Card check, Player p, Card lead) {
        int leadSuit = lead == null ? check.suit() : lead.suit(); // Any suit can be played if this is the card lead

        boolean legalSuit = Rules.followsSuit(check, lead) || !p.hasSuit(leadSuit);
        boolean legalCard = p.find(check) >= 0;

        return legalSuit && legalCard;
    }
}
