import hsa.Console;

public class Bridge extends Console {
    private Deck deck;
    private Player[] players;

    public Bridge() {
        this.deck = new Deck();
        this.deck.shuffle();

        this.players = new Player[4]; // Bridge is played by four people
        for (int i = 0; i < 4; i++) {
            this.players[i] = new Player(this.deck.deal(13));
        }
    }
}
