# ICS 2O3 Summative Proposal - bridge
### Abbreviations used
`C`: clubs  
`D`: diamonds  
`H`: hearts  
`S`: spades  
`NT`: no trump  
`P`: pass  
`X`: double  
`XX`: redouble  
`N`: north  
`S`: south  
`E`: east  
`W`: west  

## Tasks
The game of bridge has four main segments: set up, bidding, playing the hand, and scoring.

### Set up
- Bridge is played by 4 players - North, South, East, and West.
    - The players are divided into 2 partnerships: `N`/`S` and `E`/`W`.
- Create a standard deck of 52 cards and shuffle it.
- Deal one hand of 13 cards to each of the players.
- Choose one of the players to be the dealer (the player who bids first).

### Bidding
- The bidding follows the cycle: `N` -> `E` -> `S` -> `W` -> `N` -> ... .
- Players take turns making "calls" which are one of 38 options:
    - A bid of the form `rank + strain`, where:
        - `rank` is an integer from 1 to 7, inclusive
        - `strain` is one of `C`, `D`, `H`, `S`, and `NT`
    - The remaining three calls are `P`, `X`, and `XX`.
- `P` may be called at any time, and has no effect on the contract.
    - A player may later bid again after passing.
- A call of `X` can only be made if a bid has previously been made.
- If a bid is called, then it must be larger than the most recent bid.
- A bid greater than another bid if:
    - The rank of the first bid is strictly greater than the rank of the second bid,
    - Or the ranks of the bids are equal and the strain of the first bid is strictly greater than that of the second, where `C` < `D` < `H` < `S` < `NT`.
- The bidding ends immediately after 3 consecutive players pass following a non-`P` call.
    - If all four players pass in the first round of bidding, the bidding ends and control skips to the scoring.
- The contract is the largest bid at the end of the auction.
- The partnership which bid the contract are the declaring side, the other partnership is the defending side.

### Playing the hand
- Play follows the same rotation as the bidding.
- Declarer is the member of the declaring side who was the first to bid the strain of the contract.
- Dummy is declarer's partner.
- Declarer plays for dummy.
- One set of four cards played is a trick.
- The player before dummy plays the first trick.
- After the opening lead, dummy's cards are laid out so that everyone can see them.
- After the first trick, the player who won the previous trick leads the next one.
- The first player may play any card from their hand.
- The remaining players must play a card of the same suit (follow suit) if they can.
- A player must play a card that isn't in the suit led only if they can't follow suit.
- Winning the trick:
    - The highest card played wins the trick.
    - Cards played outside of the suit led don't count.
    - If there is a trump suit and 1 or more of the cards played are in the trump suit, the highest trump wins.

### Scoring
- If all four players passed to open the bidding, both partnerships score 0.
- Otherwise, the contract is made if declarer took 6 tricks plus the number of their bid.
 - Any tricks more than the contract are termed overtricks
 - Any tricks less than the contract are termed
- Made contracts:

      Suit       | Normal |  X  |  XX 
:--------------: | :----: | :-: | :-:
`NT` - 1st trick |   40   |  80 | 160
`NT` - others    |   30   |  60 | 120
    `H`/`S`      |   30   |  60 | 120
    `C`/`D`      |   20   |  40 | 80

- Overtricks:
 - If undoubled: 30 points/overtrick in `H`, `S`, or `NT`
