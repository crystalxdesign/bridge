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
`IFF`: if and only if

## Tasks
The game of bridge has four main segments: set up, bidding, playing the hand, and scoring.

### Set up
- Bridge is played by 4 players - North, South, East, and West.
 - The players are divided into 2 partnerships: `N`/`S` and `E`/`W`.
- Create a standard deck of 52 cards and shuffle it.
- Deal one hand of 13 cards to each of the players.
- Choose one of the players to be the dealer (the player who bids first).

### Bidding
- The bidding follows the cycle: `N` -> `E` -> `S` -> `W` -> `N`.
- The dealer bids first.
- Bids take the form `(number + trump)` or one of `(pass, double, redouble)`.
 - `number` is an integer in the range [1, 7]
 - `trump` is one of `C`, `D`, `H`, `S`, and `NT`.
 - In the second form, the words or abbreviations may be used
- Each bid must be either strictly greater than the most recent one or a `P`, `X`, or `XX`.
- A bid is strictly greater than another bid `IFF`:
 - The number of the first bid is strictly greater than the number of the second bid,
 - Or the numbers of the bids are equal and the trump of the first bid is strictly greater than that of the second, where `C` < `D` < `H` < `S` < 
`NT`.
 - Additionally, neither bid may be `P`, `X`, or `XX` (comparison is undefined for those bids).
- Doubles:
 - When `X` is bid, it applies to the most recent non-pass/double bid only.
 - X may only be bid a non-pass bid has been made previously.
 - A double is active until the next non-pass/`XX` bid
 - XX may be bid `IFF` a double is currently active.
- If all players pass on their first bid, the hand ends immediately and the play segment is skipped.
- The bidding ends immediately after 3 consecutive players pass following a non-pass bid.

### Playing the hand
- Play follows the same rotation as the bidding.
 - Declarer is the member of the partnership which made the final non-`P`/`X` bid who was the first to bid the suit.
 - Dummy is declarer's partner.
 - Declarer plays for dummy.
- One set of four cards played is a trick.
- The player before dummy plays the first trick.
- After the opening lead, dummy's cards are laid out so that everyone can see them.
- After the first trick, the player who won the previous trick leads the next one.
- The first player may play any card from their hand.
- The remaining players must play a card of the same suit, if they can.
- A player may play a card that isn't in the suit led `IFF` they have no cards remaining in the suit.
- Winning the trick:
 - The highest card played wins the trick.
 - Cards played outside of the suit led don't count.
 - If there is a trump suit and 1 or more of the cards played are in the trump suit, the highest trump wins.
