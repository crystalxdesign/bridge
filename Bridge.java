import hsa.Console;

public class Bridge extends Console {
    private Deck deck;

    /**
     * The four people playing the game.
     */
    public Player north, south, east, west;

    public Bridge() {
        this.deck = new Deck();
        this.deck.shuffle();

        this.north = new Player(this.deck.deal(13));
        this.south = new Player(this.deck.deal(13));
        this.east  = new Player(this.deck.deal(13));
        this.west  = new Player(this.deck.deal(13));
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
}
