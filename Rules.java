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

    public static final int PASS     = 0;
    public static final int DOUBLE   = 1;
    public static final int REDOUBLE = 2;

    private Rules() {} // A Rules object should never be instantiated

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

        /*
         * Undertrick points:
         * | Number of undertricks | Undoubled | Doubled | Redoubled |
         * |-----------------------+-----------+---------+-----------|
         * |          1st          |           |   100   |    200    |
         * |-----------------------|           |---------+-----------|
         * |       2nd & 3rd       |    50     |   200   |    400    |
         * |-----------------------|           |---------+-----------|
         * |          4th+         |           |   300   |    600    |
         * |-----------------------+-----------+---------+-----------|
         */
        if (made < need) {
            int undertricks = need - made;
            if (doubled == 0) {
                underPoints = 50 * undertricks;
            }
            else {
                for (int i = 0; i < undertricks; i++) {
                    if (i == 0) {
                        underPoints += 100 * doubled;
                    }
                    else if (i == 1 || i == 2) {
                        underPoints += 200 * doubled;
                    }
                    else {
                        underPoints += 300 * doubled;
                    }
                }
            }
        }

        /*
         * Bonus points:
         * - Slam bonus:
         *  - For a made rank-6 contract, 500 points are awarded.
         *  - For a made rank-7 contract, 1000 points are awarded.
         * - Game bonus: for a made contract worth 100 or more points, 300
         *   points are awarded.
         * - Doubled/redoubled bonus:
         *  - If a made contract was doubled, 50 points are awarded.
         *  - If a made contract was redoubled, 100 points are awarded.
         */
        if (made >= need) {
            if (need == 6) {
                bonusPoints += 500;
            }
            else if (need == 7) {
                bonusPoints += 1000;
            }

            if (contractPoints >= 100) {
                bonusPoints += 300;
            }

            if (made - need >= 0) {
                if (doubled == 1) {
                    bonusPoints += 50;
                }
                else if (doubled == 2) {
                    bonusPoints += 100;
                }
            }
        }

        return contractPoints + overPoints - underPoints + bonusPoints;
    }
}
