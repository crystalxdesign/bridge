package bridge;

import bridge.hsa.Console;
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
     * The contract of the bridge game, as determined by the auction.
     */
    private Contract contract;

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
     * @param cards the hand to show
     * @param top the coordinate of the top of the cards
     */
    public void show(Card[] cards, int top) {
        for (int i = 0; i < cards.length; i++) {
            this.drawImage(cards[i].getImage(), i * 20, top, null);
        }
    }

    /**
     * Get a well-formed card from the user. A string is well-formed if it
     * satisfies the rules of {@link Card#Card(String)}.
     *
     * @param prompt a string to display the the user when asking for a card
     * @return a valid (but not necessarily legal) Card object
     */
    public Card readCard(String prompt) {
        this.print(prompt);

        Card c;
        String s = "";
        try {
            s = this.readLine(); // Read the input
            if (s.equals("?") || s.equals("h") || s.equals("H")) { // If the user asks for more information
                Bridge.showCardHelp();

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
     * @return the player that won
     */
    public int trick(int leader) {
        // Setup
        Card entered, lead = null; // lead == null indicates this is the first card of the trick
        Card[] played = new Card[4];
        int pos; // The position of the entered card in the current player's hand

        if (leader == -1) {
            leader = (this.contract.declarer() + 1) % 4;
        }
        int dummy = (this.contract.declarer() + 2) % 4;

        // Play the cards
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

            // Print the contract
            this.println(this.contract.toString());

            // Print the results thus far
            for (int r : this.results) {
                if (r == -1) { continue; } // Skip -1
                if (r % 2 == i % 2) { this.print("|"); } // If either this player or their partner won, display a |
                else { this.print("-"); } // Otherwise display a -
            }
            this.println();

            // Display cards
            if (i != dummy) {
                this.println("Your hand: ");
                this.show(this.players[i].hand(), 15 * this.getRow());

                if (!(i == leader && this.results[0] == -1)) {
                    this.setCursor(this.getRow() + 6, 1); // Move to the row below the cards
                    this.println("Dummy's hand: ");
                    this.show(this.players[dummy].hand(), 15 * (this.getRow() + 1));
                }
            }
            else {
                this.println("Dummy's hand: ");
                this.show(this.players[dummy].hand(), 15 * this.getRow());

                this.setCursor(this.getRow() + 6, 1); // Move to the row below the cards
                this.println("Declarer's hand: ");
                this.show(this.players[this.contract.declarer()].hand(), 15 * (this.getRow() + 1));
            }

            this.setCursor(this.getRow() + 6, 1);
            entered = this.readCard("Enter a card (or ? for help): ");
            pos = this.players[i].find(entered);

            // Validate card
            for ( ; !Rules.playable(entered, this.players[i], lead); pos = this.players[i].find(entered)) {
                this.setCursor(this.getRow() - 1, 1); // Print over the current row
                this.print(entered.toString() + " can't be played. ");
                entered = this.readCard("Enter a card  (or ? for help): ");
            }

            played[i] = this.players[i].playCard(pos);

            if (lead == null) { lead = entered; }
        }

        int winner = this.winner(played, leader); // Determine the winner of the trick

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
     * Determine the winner.
     * The highest card wins. To compare to cards:
     * - If both cards follow suit with the leader, the higher rank wins.
     * - Otherwise the higher trump wins.
     *
     * Because the initial highest card is the lead, the highest card will
     * always follow suit or be trump.
     *
     * @param played the four cards played this trick
     * @param leader the person who played the first card of the trick
     */
    public int winner(Card[] played, int leader) {
        Card lead = played[leader];
        int winner = 0;

        for (int i = 0; i < 4; i++) {
            if (played[i].suit() == played[winner].suit()) {
                if (played[i].rank() > played[winner].rank()) { winner = i; }
            }
            else if (played[i].suit() == this.contract.strain()) { winner = i; }
        }

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
    public static void showCardHelp() {
        Console help = new Console("Help");

        help.println("Enter a card as (rank)(suit).");
        help.println("The card is evaluated case-insensitively, so H and h are the same.");
        help.println("Ranks:             Suits:");
        help.println("A    -> ace      S -> spades");
        help.println("K    -> king     H -> hearts");
        help.println("Q    -> queen    D -> diamonds");
        help.println("J    -> jack     C -> clubs");
        help.println("T/10 -> ten");
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

    /**
     * Determine the contract.
     *
     * @param dealer the first person to bid
     */
    public void auction(int dealer) {
        Call entered;
        List<Call> calls = new ArrayList<Call>(); // null -> pass
        Bid lastBid = null;
        int dblLevel = Rules.UNDOUBLED;
        int side = 0;

        for (int i = dealer; calls.size() < 4 || !Bridge.auctionFinished(calls); i = (i+1) % 4) {
            this.clear();
            this.println("Press a key to start " + Rules.playerName(i) + "'s turn.");
            this.getChar();
            this.clear();

            // Display previous bids
            this.showAuction(calls, i, dealer);

            // Display the hand
            this.show(this.players[i].hand(), 20 * this.getRow() + 10);
            //this.println();

            boolean valid = false;
            while (!valid) {
                entered = this.readCall("Enter a call (or ? for help): ");
                valid = true; // Assume the entry was legal until proven otherwise
                if (entered instanceof Bid && (lastBid == null || ((Bid) entered).compareTo(lastBid) > 0)) { // Bids
                    calls.add(entered);
                    lastBid = (Bid) entered;
                    dblLevel = Rules.UNDOUBLED;
                    side = i % 2;
                }
                else if (entered instanceof Double && lastBid != null &&
                         (((Double) entered).level() == Rules.DOUBLE && dblLevel == Rules.UNDOUBLED ||
                         ((Double) entered).level() == Rules.REDOUBLE && dblLevel == Rules.DOUBLE)) { // Doubles
                    dblLevel = ((Double) entered).level();
                    calls.add(entered);
                }
                else if (entered == null) { // Pass
                    calls.add(entered);
                }
                else {
                    this.setCursor(this.getRow() - 1, 1);
                    this.print("Illegal call. ");
                    valid = false;
                }
            }
        }

        if (lastBid != null) {
            int declarer = Rules.declarer(calls, side, lastBid.strain(), dealer);
            this.contract = new Contract(lastBid, dblLevel, declarer);
        }
        else {
            this.contract = null;
        }
    }

    /**
     * Show help for call entry in a new window.
     */
    public static void showCallHelp() {
        Console help = new Console("Help");

        help.println("A call is either a bid, pass, or double.");
        help.println("Calls are parsed case-insensitively, so P and p are the same.");
        help.println("Enter a bid as (rank)(strain).");
        help.println("To pass, enter P/p.");
        help.println("A double is a single 'x', a redouble is two 'x's.");
        help.println("Strains:");
        help.println("C    -> clubs");
        help.println("D    -> diamonds");
        help.println("H    -> hearts");
        help.println("S    -> spades");
        help.println("NT/N -> notrump");
        help.println();
        help.println("Examples:");
        help.println("3n -> Three notrump");
        help.println("xX -> redouble");
        help.println("p  -> pass");
        help.println();
        help.println("Enter \"?\", \"h\", or \"H\" to print this text.");
        help.println();
        help.println("Press any key to exit.");

        help.getChar();
        help.close();
    }

    /**
     * Display the history of the auction, highlighting the current player.
     *
     * @param calls the calls up to this point
     * @param current the current player
     * @param dealer the first player to bid
     */
    public void showAuction(List<Call> calls, int current, int dealer) {
        for (int i = 0; i < 4; i++) {
            this.print(" ");

            if (i == current) { this.setTextColour(java.awt.Color.RED); }
            this.print(Rules.playerName(i), 6);
            this.setTextColour(java.awt.Color.BLACK);

            this.print(i != 3 ? "|" : "\n");
        }
        this.println("-------+-------+-------+-------");
        for (int i = 0; i < dealer; i++) {
            this.print("       |");
        }
        for (int i = 0; i < calls.size(); i++) {
            this.print(" ");
            if (calls.get(i) != null) {
                this.print(calls.get(i).toString(), 6);
            }
            else {
                this.print("P", 6);
            }

            this.print((i + dealer) % 4 != 3 ? "|" : "\n");
        }
        this.println();
    }

    /**
     * Read a valid call, displaying help when asked.
     *
     * @param prompt a string to display when asking for input
     * @return a valid (but not necessarily legal) Call object
     */
    public Call readCall(String prompt) {
        this.print(prompt);
        String s = this.readLine();
        while (!Call.isCall(s)) { // While the call is invalid
            this.setCursor(this.getRow() - 1, 1); // Move back to the start of the current row

            if (s.equals("?") || s.equals("h") || s.equals("H")) { Bridge.showCallHelp(); }
            else { this.print(s + " isn't a valid call. "); }

            // Read it again
            this.print(prompt);
            s = this.readLine();
        }

        Call out;
        if (Call.isBid(s)) {
            out = new Bid(s);
        }
        else if (Call.isDouble(s)) {
            out = new Double(s);
        }
        else {
            out = null;
        }

        return out;
    }

    /**
     * Determine if the auction if over.
     * The auction is finished if the last three bids are passes.
     *
     * @param calls the history of the auction
     * @return whether or not the auction is done
     */
    public static boolean auctionFinished(List<Call> calls) {
        return calls.get(calls.size() - 1) == null &&
               calls.get(calls.size() - 2) == null &&
               calls.get(calls.size() - 3) == null;
    }

    /**
     * Get the contract for this hand.
     *
     * @return the contract
     */
    public Contract getContract() { return this.contract; }

    public static void main(String[] args) {
        Bridge game = new Bridge();

        char choice = 'y';
        List<Integer> scores = new ArrayList<Integer>();
        int leader = -1;
        while (choice == 'y') {
            // Determine the contract
            game.auction((int) (Math.random() * 4));

            // Play the hand
            if (game.getContract() != null) {
                for (int i = 0; i < 13; i++) {
                    leader = game.trick(leader);
                    game.setResult(i, leader % 2);
                }
            }

            // Calculate the score
            if (game.getContract() != null) {
                if (game.getContract().declarer() % 2 == 0) {
                    scores.add(game.getContract().score(game.getResults()));
                }
                else {
                    scores.add(-game.getContract().score(game.getResults()));
                }
            }
            else {
                scores.add(0);
            }
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
            game.setCursorVisible(true);
            game.print("Continue [y/n]? ");
            choice = game.getChar();
            game.setCursorVisible(false);
            game.println(choice);

            choice = Character.toLowerCase(choice);

            // Validate the input
            while (choice != 'y' && choice != 'n') {
                game.print("Please enter y or n. ");
                choice = game.getChar();
                game.println(choice);

                choice = Character.toLowerCase(choice);
            }

            game.close();
            game = new Bridge(); // Reset the game, start a new one
        }

        game.close();
    }
}
