/* *
 * @version date ( in_ISO_8601 format : 2019 - 12 - 05 )
 * @author Alana Chigbrow
 * This class creates a Card object that has a rank and a suit.
 * Instructions for use:
 * This class is meant to be used in conjunction with Deck.java, Rank.java,
 * Suit.java, KlondikeModel.java, and Klondike.java.
 * 1. Open in IntelliJ, other IDE, or console
 * 2. Run the Klondike.java class.
 * 3. Play Klondike Solitaire according to established rules.
 */

public class Card {

    /**
     * Rank of the card (Ace through King).
     */
    private Rank rank;

    /**
     * Suit of the card (Diamond, Heart, Spade, Club).
     */
    private Suit suit;

    /**
     * Indicates whether or not the card is face up on the pile.
     */
    private boolean faceUp = true;

    /**
     * Constructs a new Card with the given rank and suit. Newly constructed
     * cards are face-up by default.
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Returns the rank of this card: one of ACE through KING.
     *
     * @return Rank of card
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Returns the suit of this card, one of CLUBS, SPADES, HEARTS, or
     * DIAMONDS.
     *
     * @return Suit of card
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Indicates whether or not the card is face up.
     *
     * @return true if card is face up. False if card is face down.
     */
    public boolean isFaceUp() {
        return faceUp;
    }

    /**
     * Checks if the card is red.
     *
     * @return true if the suit is either HEARTS or DIAMONDS (red). Returns
     * false if the suit is either SPADES or CLUBS (black).
     */
    public boolean isRed() {
        return (getSuit() == Suit.HEARTS || getSuit() == Suit.DIAMONDS);
    }

    /**
     * Flips the card from face up to face down. Changes boolean faceUp to
     * false.
     */
    public void flipOver() {
        faceUp = !faceUp;
    }

    /**
     * Overrides the toString method.
     *
     * @return a String representation of the Rank and Suit of the Card.
     */
    @Override
    public String toString() {
        return String.format("%s%s", getRank(), getSuit());
    }

}
