import hsa.Console;

public class Bridge extends Console {
    private Deck deck;

    /**
     * The four people playing the game.
     */
    private Player[] players;

    /**
     * The winners of previous tricks. If if the trick hasn't been played
     * yet, the number should be -1.
     */
    private int[] results;

    public static final int NORTH = 0;
    public static final int EAST  = 1;
    public static final int SOUTH = 2;
    public static final int WEST  = 3;

    /**
     * Create a bridge game. The game has a standard 52-card deck dealt to 4
     * players so that each has a hand of 13 cards.
     */
    public Bridge() {
        super();
        this.deck = new Deck();
        this.deck.shuffle();

        this.players = new Player[4];
        for (int i = 0; i < 4; i++) {
            this.players[i] = new Player(this.deck.deal(13)); // Initialize the players
        }

        this.results = new int[13]; // 13 tricks
        for (int i = 0; i < 13; i++) {
            this.results[i] = -1; // Fill the array with -1 because none of the tricks have been played yet
        }
    }

    /**
     * Display a hand of cards
     *
     * @param hand the player's cards
     */
    public void show(Card[] hand) {
        this.print("Your hand: ");
        for (Card c : hand) {
            this.print(c + " ");
        }

        this.println();
    }

    /**
     * Get a well-formed card from the user. A string is well-formed if it
     * satisfies the rules of {@link Card#Card(String)}.
     *
     * @param prompt a string to display the the user when asking for a card
     * @return the card the user entered
     */
    public Card readCard(String prompt) {
        this.print(prompt);

        Card c;
        try {
            String s = this.readLine(); // Read the input
            c = new Card(s); // Try to convert it to a Card
        } catch (MalformedCardException e) { // If the user entered a bad string
            this.print(s + " is not a valid card. "); // Print an error message
            c = this.readCard(prompt); // Try again
        }

        return c;
    }

    /**
     * Play a trick, retrieving a card from each player.
     *
     * @param leader the first player to go, one of Bridge.NORTH, Bridge.EAST,
     *              Bridge.SOUTH, and Bridge.WEST
     * @param trump the trump suit, one of Card.CLUBS, Card.DIAMONDS,
     *              Card.DIAMONDS, and Card.SPADES
     * @return the player that won
     */
    public int trick(int leader, int trump) {
        // Play the cards
        Card entered, lead = null; // lead == null indicates this is the first card of the trick
        Card[] played = new Card[4];
        int pos; // The position of the entered card in the current player's hand

        for (int i = leader; i != (leader+4) % 4 || lead == null; i = (i+1) % 4) {
            this.clear();
            this.println("Press a key to start the turn.");
            this.getChar();
            this.clear();

            // Print the cards played so far
            for (int j = Bridge.NORTH; j <= Bridge.WEST; j++) {
                if (j == i) { this.setTextColour(java.awt.Color.RED); } // Highlight the current player

                if (j == Bridge.NORTH)      { this.print("North: "); }
                else if (j == Bridge.EAST)  { this.print("East: "); }
                else if (j == Bridge.SOUTH) { this.print("South: "); }
                else if (j == Bridge.WEST)  { this.print("West: "); }

                this.println(played[j] != null ? played[j].toString() : ""); // Print empty strings instead of nulls
                this.setTextColour(java.awt.Color.BLACK);
            }
            this.println();

            // Print the results thus far
            for (int r : this.results) {
                if (r == -1) { continue; } // Skip -1
                if (r == i || r == (i+2) % 4) { this.print("|"); } // If either this player or their partner won, display a |
                else { this.print("-"); } // Otherwise display a -
            }
            this.println();
            this.show(this.players[i].hand()); // Print the hand

            entered = this.readCard("Choose a card: ");
            pos = this.players[i].find(entered);

            // Validate card
            for ( ; !Bridge.playable(entered, this.players[i], lead); pos = this.players[i].find(entered)) {
                this.print(entered.toString() + " can't be played. ");
                entered = this.readCard("Choose a card: ");
            }

            played[i] = this.players[i].playCard(pos);

            if (lead == null) { lead = entered; }
        }

        /*
         * Determine the winner:
         * - If both cards follow suit with the leader, the higher rank wins.
         * - Otherwise the higher trump wins.
         *
         * Because the initial highest card is the lead, the highest card will always follow suit or be trump.
         */
        int winner = leader;
        for (int i = 0; i < 4; i++) {
            if (Bridge.followsSuit(played[i], lead) && Bridge.followsSuit(played[winner], lead)) {
                if (played[i].rank() > played[winner].rank()) { winner = i; }
            }
            else {
                if (played[i].suit() == trump && played[winner].suit() != trump) { winner = i; }
                else if (played[i].suit() == trump && played[winner].suit() == trump) {
                    if (played[i].rank() > played[winner].rank()) { winner = i; }
                }
            }
        }

        // Print the played cards
        this.clear();
        this.println("North: " + played[Bridge.NORTH]);
        this.println("East: " + played[Bridge.EAST]);
        this.println("South: " + played[Bridge.SOUTH]);
        this.println("West: " + played[Bridge.WEST]);
        this.getChar();

        return winner;
    }

    /**
     * Save the result of a trick.
     *
     * @param trickNum the zero-indexed number of the trick (i. e. the first
     *                 trick is trick 0)
     * @param r the winner of the trick
     */
    public void setResult(int trickNum, int r) { this.results[trickNum] = r; }

    /**
     * Check if a card has the same suit as the lead.
     *
     * @param check the {@code Card} to test
     * @param lead the {@code Card} lead, or {@code null} if this is the first
     *             card of the trick
     * @return {@code true} if the suits of the two cards are the same, {@code false} otherwise
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

        boolean legalSuit = Bridge.followsSuit(check, lead) || !p.hasSuit(leadSuit);
        boolean legalCard = p.find(check) >= 0;

        return legalSuit && legalCard;
    }

    public static void main(String[] args) {
        Bridge game = new Bridge();
        int lead = Bridge.WEST;
        for (int i = 0; i < 13; i++) {
            lead = game.trick(lead, Card.SPADES);
            game.setResult(i, lead);
        }
        game.close();
    }
}
