# ICS 2O3 Summative Proposal - Bridge
## Table of contents
- [Abbreviations](#abbreviations-used)
- [Proposal](#proposal)
    - [Development steps](#steps-of-development)
    - [Rules](#rules)
        - [Auction](#auction)
        - [Playing the hand](#playing-the-hand)
        - [Scoring](#scoring)
- [Tasks](#tasks)
    - [Set up](#set-up)
    - [Auction](#auction-1)
    - [Playing the hand](#playing-the-hand-1)
    - [Scoring](#scoring-1)

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

## Proposal
### Steps of development
1. Create deck, deal hands
2. Implement hand-related methods (display hand, choose a card, remove a card, etc.)
3. Implement basic play of the hand
4. Add following suit
5. Implement trumps
6. Add bidding classes and implement basic bidding
7. Add bidding rules
8. Add scoring
9. Rewrite relevant methods to use `Console` and images
10. If I'm feeling ambitious, use a fully-fledged GUI (click on a card to play, etc.)

### Rules
- Bridge is played by 4 players - North, South, East, and West.
    - The players are divided into 2 partnerships: `N`/`S` and `E`/`W`.
- Bridge uses a standard deck of 52 cards.
- The four suits are split into two groups: minor suits (`C` and `D`) and major suits (`H` and `S`).

#### Auction
- Players take turns making "calls" which are one of 38 options:
    - A bid of the form `rank + strain`, where:
        - `rank` is an integer from 1 to 7, inclusive.
        - `strain` is one of `C`, `D`, `H`, `S`, and `NT`.
    - The remaining three calls are pass, double, and redouble.
- Pass may be called at any time, and has no effect on the auction.
    - A player may later bid again after passing.
- A call of `X` can only be made if a bid has previously been called.
- A call of `XX` can be made if `X` has been called after the most recent bid.
- If a bid is called, then it must be larger than the most recent bid.
- A bid greater than another bid if:
    - The rank of the first bid is strictly greater than the rank of the second bid,
    - Or the ranks of the bids are equal and the strain of the first bid is strictly greater than that of the second.
        - Where `C` < `D` < `H` < `S` < `NT`.
- The auction ends immediately after 3 consecutive players pass following a non-`P` call.
    - If all four players pass in the first round of bidding, the auction ends and the game skips to the scoring.
- The contract is the largest bid at the end of the auction.
    - If `X` or `XX` have been called after the most recent bid, the contract is doubled or redoubled (respectively).
- The partnership which bid the contract are the declaring side, the other partnership is the defending side.

#### Playing the hand
- Declarer is the member of the declaring side who was the first to bid the strain of the contract.
- The strain of the contract determines the trump suit (`NT` indicates no trump suit).
- Dummy is declarer's partner.
    - Dummy's cards are shown to everyone after the opening lead.
    - Declarer plays for dummy.
- One set of four cards played is a trick.
- The player before dummy leads the opening trick.
- After the first trick, the player who won the previous trick leads the next one.
- The player leading may play any card from their hand.
    - The remaining players must play a card of the same suit (follow suit) if they can.
    - A player must play a card that isn't in the suit led only if they can't follow suit.
- Winning the trick:
    - The highest card played wins the trick.
    - Cards played outside of the suit led that aren't trump don't count.
    - If there is a trump suit and 1 or more of the cards played are in the trump suit, the highest trump wins.

#### Scoring
- If all four players passed to open the bidding, both partnerships score 0.
- Otherwise, the contract is made if declarer took 6 tricks plus the rank of their contract.
    - Tricks more than the contract are overtricks.
    - Tricks less than the contract are undertricks.
- The side which doesn't score is given a score equal to the negative of the other partnership's score.

##### Contract points - declarer scores
- 40 points for the first trick and 30 points for each subsequent trick in `NT`.
- 30 points/trick in major suits.
- 20 points/trick in minor suits.
- `X` multiplies the points by 2, `XX` by 4.

##### Overtrick points - declarer scores
- 30 points/overtrick in major suits and `NT`.
- 20 points/overtrick in minor suits.
- 100 points/overtrick if the contract is doubled.
- 200 points/overtrick if the contract is redoubled.

##### Undertrick points - defenders score
- 50 points/undertrick.
- Doubled:
    - 100 points for the first undertrick.
    - 200 points for the second and third undertrick.
    - 300 points/undertrick thereafter.
- If the contract is redoubled, the point awarded are twice those when doubled.

##### Bonuses - declarer scores
- Bonuses are awarded only for made contracts, are cumulative, and are in addition to contract points and overtrick points.
- Slam bonus:
    - For a made rank-6 contract, 500 points are awarded.
    - For a made rank-7 contract, 1000 points are awarded.
- Game bonus: for a made contract worth 100 or more points, 300 points are awarded.
- Doubled/redoubled bonus:
    - If a made contract was doubled, 50 points are awarded.
    - If a made contract was redoubled, 100 points are awarded.

## Tasks
The game of bridge has four main segments: set up, bidding, playing the hand, and scoring.

### Set up
- Create a standard deck of 52 cards and shuffle it.
- Deal one hand of 13 cards to each of the players.
- Choose one of the players to be the dealer (the player who bids first).

### Auction
- The auction follows the cycle: `N` -> `E` -> `S` -> `W` -> `N` -> ... .
- For each player, starting with the dealer:
    - Read a bid
    - Make sure it's valid
    - Check if the auction has ended
- Determine the declarer and dummy.

### Playing the hand
- Play follows the same rotation as the auction.
- Determine the opening lead.
- After the opening lead, display dummy's cards.
- For each trick:
    - Determine the leader and get the card they play.
    - Get the remaining three cards in order.
    - Make sure the cards played are valid.
    - Determine the winner of the trick.

### Scoring
- Count the number of tricks declarer made and subtract 6 and the rank of the contract.
    - If the result is 0, the contract was made.
    - If the result is greater than 0, the contract was made with overtricks.
    - If the result is less than 0, the contract went down.
- Determine contract points.
- Determine overtrick and undertrick points.
- Determine bonus points.
- Inform the players of the result.
