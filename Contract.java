public class Contract {
    /**
     * The member of the partnership who introduced the strain of the contract.
     */
    private final int DECLARER;

    /**
     * A doubled or redoubled contract will result in higher scores.
     */
    private final int DOUBLED;

    /**
     * The players need to take 6 more than the rank of the contract.
     */
    private final int RANK;

    /**
     * The trump suit of the contract.
     */
    private final int STRAIN;

    /**
     * Create the default contract (1S by North).
     */
    public Contract() {
        this(1, Rules.NOTRUMP, 0, Rules.NORTH);
    }

    /**
     * Create a contract from four ints.
     *
     * @param rank 1-7, inclusive
     * @param strain {@code Rules.CLUBS}, {@code Rules.DIAMONDS},
     *               {@code Rules.HEARTS}, {@code Rules.SPADES}, or
     *               {@code Rules.NOTRUMP}
     * @param doubled 0 if undoubled, 1 if doubled, or 2 if redoubled
     * @param declarer {@code Rules.NORTH}, {@code Rules.EAST},
     *                 {@code Rules.SOUTH}, or {@code Rules.WEST}
     */
    public Contract(int rank, int strain, int doubled, int declarer) {
        this.DECLARER = declarer;
        this.DOUBLED = doubled;
        this.RANK = rank;
        this.STRAIN = strain;
    }

    /**
     * Create a contract from the highest bid.
     *
     * @param b the highest bid in the auction
     * @param doubled 0 if undoubled, 1 if doubled, or 2 if redoubled
     * @param declarer {@code Rules.NORTH}, {@code Rules.EAST},
     *                 {@code Rules.SOUTH}, or {@code Rules.WEST}
     */
    public Contract(Bid b, int doubled, int declarer) {
        this(b.rank(), b.strain(), doubled, declarer);
    }

    /**
     * Get the rank of the contract.
     *
     * @return 1-7, inclusive
     */
    public int rank() { return this.RANK; }

    /**
     * Get the strain of the contract.
     *
     * @return {@code Rules.CLUBS}, {@code Rules.DIAMONDS},
     *         {@code Rules.HEARTS}, {@code Rules.SPADES}, or
     *         {@code Rules.NOTRUMP}
     */
    public int strain() { return this.STRAIN; }

    /**
     * Get whether the contract was doubled.
     *
     * @return 0 if undoubled, 1 if doubled, or 2 if redoubled
     */
    public int doubled() { return this.DOUBLED; }

    /**
     * Get the player who introduced the strain of the contract.
     *
     * @return {@code Rules.NORTH}, {@code Rules.EAST},
     *         {@code Rules.SOUTH}, or {@code Rules.WEST}
     */
    public int declarer() { return this.DECLARER; }
}
