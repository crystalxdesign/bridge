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
     * - The suit of the card is the same as the suit of the lead, unless either
     *   of the following are true:
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

    public static String playerName(int player) {
        String name = "";
        if (player == Rules.NORTH)      { name = "North"; }
        else if (player == Rules.EAST)  { name = "East"; }
        else if (player == Rules.SOUTH) { name = "South"; }
        else if (player == Rules.WEST)  { name = "West"; }

        return name;
    }

    public static int score(Bid contract, int doubled, int side, int[] results) {
        int contractPoints = 0, overPoints = 0, underPoints = 0, bonusPoints = 0;
        int need = 6 + contract.rank();
        int made = 0;
        for (int r : results) {
            if (r == side) { made++; }
        }

        if (contract == null) { return 0; } // Nobody scores if everybody passed

        /*
         * Contract points:
         * - NT: 40 points for the first trick, 30 points/trick after
         * - Major suits: 30 points/trick
         * - Minor suits: 20 points/trick
         * - X and XX double and quadruple the score
         */
        if (made >= need) {
            if (contract.strain() == Rules.NOTRUMP) {
                contractPoints = 10 + 30 * contract.rank();
            }
            else if (contract.strain() == Rules.HEARTS || contract.strain() == Rules.SPADES) {
                contractPoints = 30 * contract.rank();
            }
            else if (contract.strain() == Rules.CLUBS || contract.strain() == Rules.DIAMONDS) {
                contractPoints = 20 * contract.rank();
            }

            contractPoints *= doubled * 2;
        }

        /*
         * Overtrick points:
         * - NT/H/S: 30 points/overtrick
         * - C/D: 20 points/overtrick
         * - X: 100 points/overtrick
         * - XX: 200 points/overtrick
         */
        if (made > need) {
            int overtricks = made - need;
            if (doubled == 1) {
                overPoints = 100 * overtricks;
            }
            else if (doubled == 2) {
                overPoints = 200 * overtricks;
            }
            else if (contract.strain() == Rules.HEARTS || contract.strain() == Rules.SPADES || contract.strain() == Rules.NOTRUMP) {
                overPoints = 30 * overtricks;
            }
            else {
                overPoints = 20 * overtricks;
            }
        }

        return contractPoints + overPoints - underPoints + bonusPoints;
    }
}
