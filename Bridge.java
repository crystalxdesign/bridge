import hsa.Console;

public class Bridge extends Console {
    private Deck deck;

    /**
     * The four people playing the game.
     */
    private Player[] players;

    public static final int NORTH = 0;
    public static final int EAST  = 1;
    public static final int SOUTH = 2;
    public static final int WEST  = 3;

    public Bridge() {
        super();
        this.deck = new Deck();
        this.deck.shuffle();

        this.players = new Player[4];
        for (int i = 0; i < 4; i++) {
            this.players[i] = new Player(this.deck.deal(13));
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

    public Card readCard(String prompt) {
        this.print(prompt);

        String s = "";
        Card c;
        try {
            s = this.readLine(); // Read the input
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
     * @param first the first player to go, one of Bridge.NORTH, Bridge.EAST,
     *              Bridge.SOUTH, and Bridge.WEST
     * @param trump the trump suit, one of Card.CLUBS, Card.DIAMONDS,
     *              Card.DIAMONDS, and Card.SPADES
     * @return the player that won
     */
    public int trick(int first, int trump) {
        Card entered, lead = null; // lead == null indicates this is the first trick
        Card[] played = new Card[4];
        int pos;

        boolean firstCard = true; // Needed so that the for repeats at least once
        for (int i = first; i != (first+4) % 4 || firstCard; i = (i+1) % 4) {
            this.clear();
            this.println("Press a key to start the turn.");
            this.getChar();
            this.clear();

            // Print the cards played so far
            for (int j = 0; j < 4; j++) {
                if (j == NORTH)      { this.print("North: "); }
                else if (j == EAST)  { this.print("East: "); }
                else if (j == SOUTH) { this.print("South: "); }
                else if (j == WEST)  { this.print("West: "); }

                this.println(played[j] != null ? played[j].toString() : "");
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

            if (firstCard) {
                lead = entered;
                firstCard = false;
            }
        }

        this.clear();
        this.println("North: " + played[0]);
        this.println("East: " + played[1]);
        this.println("South: " + played[2]);
        this.println("West: " + played[3]);
        this.getChar();

        return 0;
    }

    /**
     * Check if it's legal to play a card.
     *
     * @param check the {@code Card} to test
     * @param p the person playing the card, before removing it from their hand
     * @param lead the {@code Card} lead, or {@code null} if this is the first
     *             card of the trick
     */
    public static boolean playable(Card check, Player p, Card lead) {
        int leadSuit = lead == null ? check.suit() : lead.suit(); // Any suit can be played if this is the card lead

        return !(p.find(check) >= 0 && check.suit() != leadSuit);
    }

    public static void main(String[] args) {
        Bridge game = new Bridge();
        int lead = 3;
        for (int i = 0; i < 13; i++) {
            lead = game.trick(lead, 0);
        }
        game.close();
    }
}