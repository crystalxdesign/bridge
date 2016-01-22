import hsa.Console;
import java.util.List;
import java.util.ArrayList;

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

    /**
     * Create a bridge game. The game has a standard 52-card deck dealt to 4
     * players so that each has a hand of 13 cards.
     */
    public Bridge() {
        super("Bridge");
        this.deck = new Deck();
        this.deck.shuffle();

        this.players = new Player[4];
        for (int i = 0; i < 4; i++) {
            this.players[i] = new Player(this.deck.deal(13)); // Initialize the players
        }

        this.results = new int[13]; // 13 tricks
        for (int i = 0; i < 13; i++) {
            this.results[i] = -1; // Fill the array with -1
        }
    }

    /**
     * Display a hand of cards
     *
     * @param p the player whose hand is being displayed
     */
    public void show(Player p) {
        this.println("Your hand:");

        this.print("C: ");
        for (Card c : p.suit(Rules.CLUBS)) { this.print(c.rankStr() + " "); }
        this.println();

        this.print("D: ");
        for (Card c : p.suit(Rules.DIAMONDS)) { this.print(c.rankStr() + " "); }
        this.println();

        this.print("H: ");
        for (Card c : p.suit(Rules.HEARTS)) { this.print(c.rankStr() + " "); }
        this.println();

        this.print("S: ");
        for (Card c : p.suit(Rules.SPADES)) { this.print(c.rankStr() + " "); }
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
        String s = "";
        try {
            s = this.readLine(); // Read the input
            if (s.equals("?") || s.equals("h") || s.equals("H")) { // If the user asks for more information
                Bridge.showHelp();

                c = this.readCard(prompt); // Go back to the prompt
            }
            else {
                c = new Card(s); // Try to convert it to a Card
            }
        } catch (MalformedCardException e) { // If the user entered a bad string
            this.print(s + " is not a valid card. "); // Print an error message
            c = this.readCard(prompt); // Try again
        }

        return c;
    }

    /**
     * Play a trick, retrieving a card from each player.
     *
     * @param leader the first player to go, one of Rules.NORTH, Rules.EAST,
     *              Rules.SOUTH, and Rules.WEST
     * @param trump the trump suit, one of Rules.CLUBS, Rules.DIAMONDS,
     *              Rules.DIAMONDS, and Rules.SPADES
     * @return the player that won
     */
    public int trick(int leader, int trump) {
        // Play the cards
        Card entered, lead = null; // lead == null indicates this is the first card of the trick
        Card[] played = new Card[4];
        int pos; // The position of the entered card in the current player's hand

        for (int i = leader; i != (leader+4) % 4 || lead == null; i = (i+1) % 4) {
            this.clear();
            this.println("Press a key to start " + Rules.playerName(i) + "'s turn.");
            this.getChar();
            this.clear();

            // Print the cards played so far
            for (int j = Rules.NORTH; j <= Rules.WEST; j++) {
                if (j == i) { this.setTextColour(java.awt.Color.RED); } // Highlight the current player

                this.print(Rules.playerName(j) + ": ");

                this.println(played[j] != null ? played[j].toString() : ""); // Don't print null
                this.setTextColour(java.awt.Color.BLACK);
            }
            this.println();

            // Print the results thus far
            for (int r : this.results) {
                if (r == -1) { continue; } // Skip -1
                if (r % 2 == i % 2) { this.print("|"); } // If either this player or their partner won, display a |
                else { this.print("-"); } // Otherwise display a -
            }
            this.println();
            this.show(this.players[i]); // Print the hand

            entered = this.readCard("Enter a card (or ? for help): ");
            pos = this.players[i].find(entered);

            // Validate card
            for ( ; !Rules.playable(entered, this.players[i], lead); pos = this.players[i].find(entered)) {
                this.print(entered.toString() + " can't be played. ");
                entered = this.readCard("Enter a card  (or ? for help): ");
            }

            played[i] = this.players[i].playCard(pos);

            if (lead == null) { lead = entered; }
        }

        /*
         * Determine the winner:
         * - If both cards follow suit with the leader, the higher rank wins.
         * - Otherwise the higher trump wins.
         *
         * Because the initial highest card is the lead, the highest card will
         * always follow suit or be trump.
         */
        int winner = leader;
        for (int i = 0; i < 4; i++) {
            if (Rules.followsSuit(played[i], lead) && Rules.followsSuit(played[winner], lead)) {
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
        this.println("North: " + played[Rules.NORTH]);
        this.println("East: " + played[Rules.EAST]);
        this.println("South: " + played[Rules.SOUTH]);
        this.println("West: " + played[Rules.WEST]);
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
    public void setResult(int trickNum, int r) { this.results[trickNum] = r % 2; }

    /**
     * Get the results of the game.
     *
     * @return the winners of each trick
     */
    public int[] getResults() { return this.results; }

    /**
     * Show help for card entry in a new window.
     */
    public static void showHelp() {
        Console help = new Console("Help");

        help.println("Enter a card as (rank)(suit).");
        help.println("The card is evaluated case-insensitively, so H and h are the same.");
        help.println("Ranks:             Suits:");
        help.println("A/a    -> ace      S/s -> spades");
        help.println("K/k    -> king     H/h -> hearts");
        help.println("Q/q    -> queen    D/d -> diamonds");
        help.println("J/j    -> jack     C/c -> clubs");
        help.println("T/t/10 -> ten");
        help.println();
        help.println("Examples:");
        help.println("Ac  -> Ace of clubs");
        help.println("10d -> Ten of diamonds");
        help.println("TH  -> Ten of hearts");
        help.println("2s  -> Two of spades");
        help.println();
        help.println("Enter \"?\", \"h\", or \"H\" to print this text.");
        help.println();
        help.println("Press any key to exit.");

        help.getChar();
        help.close();
    }

    public static void main(String[] args) throws MalformedBidException {
        Bridge game = new Bridge();
        Contract c = new Contract(new Bid("4S"), Rules.DOUBLE, Rules.NORTH);

        char choice = 'y';
        List<Integer> scores = new ArrayList<Integer>();
        while (choice == 'y') {
            int leader = (c.declarer() + 1) % 4;
            for (int i = 0; i < 13; i++) {
                leader = game.trick(leader);
                game.setResult(i, leader % 2);
            }

            scores.add(c.score(game.getResults()));
            game.clear();

            // Show the tricks North and South won
            game.print("N/S: ");
            for (int r : game.getResults()) {
                game.print(r == 0 ? "|" : " ");
            }
            game.println();

            // Show the tricks East and West won
            game.print("E/W: ");
            for (int r : game.getResults()) {
                game.print(r == 1 ? "|" : " ");
            }
            game.println();

            // Print the scores
            game.println("North/South scores " + scores.get(scores.size() - 1));
            game.println("East/West scores " + -scores.get(scores.size() - 1));

            game.println();
            game.println("  N/S  |  E/W  ");
            game.println("-------+-------");
            for (int score : scores) {
                game.print(score, 6);
                game.print(" | ");
                game.print(-score);
            }

            game.println();
            game.print("Continue [y/n]? ");
            choice = game.getChar();
            game.println(choice);

            choice = Character.toLowerCase(choice);

            // Validate the input
            while (choice != 'y' && choice != 'n') {
                game.print("Please enter y or n. ");
                choice = game.getChar();
                game.println(choice);

                choice = Character.toLowerCase(choice);
            }
        }

        game.close();
    }
}
