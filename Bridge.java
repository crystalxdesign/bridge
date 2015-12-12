import hsa.Console;

public class Bridge extends Console {
    private Deck deck;

    public Bridge() {
        this.deck = new Deck();
        this.deck.shuffle();
    }
}
