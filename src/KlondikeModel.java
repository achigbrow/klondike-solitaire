/**
 * Model of the Klondike solitaire game.
 */
public class KlondikeModel {

    /** The deck of undrawn cards. */
    private Deck deck;

    /** The pile of cards available to move to the tableau or foundations. */
    private Deck drawPile;

    /** Piles of cards building on the aces. */
    private Deck[] foundations;

    /** Splayed piles of cards. */
    private Deck[] tableau;

    public KlondikeModel() {
        // Prepare the deck
        deck = new Deck();
        deck.fill();
        deck.shuffle();
        for (int i = 0; i < deck.size(); i++) {
            Card c = deck.getCardAt(i);
            if(c != null && c.isFaceUp()) {
                c.flipOver();
            }
        }
        // Create the tableau
        tableau = new Deck[7];
        for (int i = 0; i < tableau.length; i++) {
            tableau[i] = new Deck();
            for (int j = 0; j <= i; j++) {
                deck.moveTo(tableau[i]);
                if (j == i) {
                    Card c = tableau[i].getCardAt(j);
                    if(c != null && !c.isFaceUp()) {
                        c.flipOver();
                    }
                }
            }
        }
        // Create the empty foundations
        foundations = new Deck[4];
        for (int i = 0; i < foundations.length; i++) {
            foundations[i] = new Deck();
        }
        // Draw the first card
        drawPile = new Deck();
        deck.moveTo(drawPile);
        Card topCard = drawPile.getTopCard();
        if(topCard != null && !topCard.isFaceUp()) {
            topCard.flipOver();
        }
    }

    /**
     * Takes the next card from the deck and places it face up on the draw pile.
     * Moves the draw pile back to the deck if deck is empty.
     */
    public void drawNextCard() {
        if (deck.size() > 0) {
            deck.moveTo(drawPile);
            Card c = drawPile.getTopCard();
            if(!c.isFaceUp()) {
                c.flipOver();
            }
        } else {
            while(drawPile.size() > 0) {
                drawPile.moveTo(deck);
                Card c = deck.getTopCard();
                if(c.isFaceUp()) {
                    c.flipOver();
                }
            }
        }
    }

    /** Returns the deck. */
    public Deck getDeck() {
        return deck;
    }

    /** Returns the draw pile. */
    public Deck getDrawPile() {
        return drawPile;
    }

    /**
     * Returns the ith foundation (indexed by suit).
     *
     * @see Card#getSuit()
     */
    public Deck getFoundation(int i) {
        return foundations[i];
    }
    public Deck getFoundation(Suit s) {
        return foundations[s.ordinal()];
    }

    /** Returns the ith tableau pile (zero-indexed from left). */
    public Deck getTableau(int i) {
        return tableau[i];
    }

    /**
     * Moves a sequence of face-up cards from here to there, so that the face-up
     * cards on that form a sequence in ranks with alternating colors. For
     * example, if we have:
     * <p>
     * a: ## 8d 7s 6d 5s 4d 3c<br>
     * b: ## ## 6h
     * <p>
     * then klondikeMove(a, b) should result in:
     * <p>
     * a: ## 8d 7s 6d<br>
     * b: ## ## 6h 5s 4d 3c
     *
     * Has no effect if the move is illegal.
     */
    public void klondikeMove(Deck here, Deck there) {
        Card c = there.getTopCard(); // The card of top of that, if any
        for (int i = here.size() - 1; (i >= 0) && here.getCardAt(i).isFaceUp(); i--) {
            if (successor(here.getCardAt(i), c)) {
                here.moveTo(there, here.size() - i);
            }
        }
    }

    /**
     * Moves a card from source to the specified foundation, if this is legal.
     * The next card in source (if any) is turned face up.
     */
    public void moveToFoundation(Deck source, int foundationIndex) {
        if ((foundationIndex >= 0) && (foundationIndex < 4)) {
            Card c = source.getTopCard();
            Deck foundation = foundations[foundationIndex];
            if (c != null && (c.getSuit().ordinal() == foundationIndex)
              && (((foundation.size() == 0) && (c.getRank() == Rank.ACE))
              || ((foundation.size() > 0)
              && (c.getRank().ordinal() == foundation.getTopCard().getRank().ordinal() + 1)))) {
                source.moveTo(foundation);
                if (source.size() > 0) {
                    Card top = source.getTopCard();
                    if(!top.isFaceUp()) {
                        top.flipOver();
                    }
                }
            }
        }
    }

    /**
     * Moves one card (if source is the draw pile) or several (if source is a
     * tableau pile) from source to the indicated tableau pile. Does nothing if
     * the move would be illegal.
     */
    public void moveToTableau(Deck source, int tableauIndex) {
        Deck destination = tableau[tableauIndex];
        if (source == drawPile) {
            Card sourceCard = source.getTopCard();
            Card destCard = destination.getTopCard();
            if (sourceCard != null &&
              successor(sourceCard, destCard)) {
                source.moveTo(destination);
            }
        } else {
            klondikeMove(source, destination);
            if (source.size() > 0) {
                Card top = source.getTopCard();
                if(!top.isFaceUp()) {
                    top.flipOver();
                }
            }
        }
    }

    /**
     * Returns true if a can be placed on top of b, i.e., either a and b are
     * different colors and a's rank is one less than b's, or a is a king and b
     * is null.
     */
    public boolean successor(Card a, Card b) {
        return ((b == null) && (a.getRank() == Rank.KING))
          || ((b != null)
          && (a.getRank().ordinal() == b.getRank().ordinal() - 1)
          && (a.isRed() != b.isRed()));
    }

}