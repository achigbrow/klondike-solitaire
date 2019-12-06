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

import java.security.SecureRandom;

public class Deck {

    /**
     * An empty array of Cards.
     */
    private Card[] cards;

    /**
     * The default size for a deck. Will be used to create each new Deck
     * object.
     */
    private final int DECK = 52;

    /**
     * The number of cards in the deck.
     */
    private int deckSize;

    /**
     * Creates a new deck object with a Card Array size 52.
     */
    public Deck() {
        cards = new Card[DECK];
    }

    /**
     * Fills cards array with the standard deck of cards.
     */
    public void fill() {
        int i = 0;

        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                cards[i] = new Card(r, s);
                i++;
            }
        }
    }

    /**
     * Counts the number of non-null elements in cards.
     *
     * @return the total number of cards in the cards array.
     */
    public int size() {
        int totalCards = 0;

        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null) {
                totalCards++;
            } else {
                break;
            }
        }
        deckSize = totalCards;

        return deckSize;
    }

    /**
     * Counts the number of cards in another deck.
     *
     * @param other the other deck
     * @return the number of cards in the other deck.
     */
    public int size(Deck other) {
        return other.size();
    }

    /**
     * Adds the Card passed in to the top of a new deck.
     *
     * @param card Card object to be added to a deck.
     */
    public void add(Card card) {
        Card[] newDeck = new Card[DECK];

        System.arraycopy(cards, 0, newDeck, 0, cards.length);
        for (int i = 0; i < newDeck.length; i++) {
            if (newDeck[i] == null) {
                newDeck[i] = card;
                break;
            }
        }
        cards = newDeck;
    }

    /**
     * Identifies the card at the indicated index.
     *
     * @param n index of the card you want.
     * @return the Card at index n.
     */
    public Card getCardAt(int n) {
        return cards[n];
    }

    /**
     * Takes the top card from this deck and moves it to the other deck.
     *
     * @param other Deck object that will receive the top card of this Deck.
     */
    public void moveTo(Deck other) {
        Card move = getTopCard();
        other.add(move);
        remove(1);
    }

    /**
     * Moves a specified number of cards, in order, from this to Deck to another
     * deck.
     *
     * @param other Deck that will receive the cards.
     * @param n     Number of cards to pass over to the cards.
     */
    public void moveTo(Deck other, int n) {
        int size = size();
        int start = size - n;

        for (int i = start; i < size; i++) {
            Card newCard = getCardAt(i);
            other.add(newCard);
        }
        remove(n);
    }

    /**
     * Helper method that removes cards from a Deck that have been moved to
     * another deck.
     *
     * @param n number of cards to remove from the deck.
     */
    public void remove(int n) {
        int noCards = size() - n;
        Card[] newDeck = new Card[DECK];
        for (int i = 0; i < noCards; i++) {
            newDeck[i] = cards[i];
        }
        cards = newDeck;
    }

    /**
     * Gets the card that is on top of the deck.
     *
     * @return the Card object at the top of the deck (i.e. at the largest
     * index).
     */
    public Card getTopCard() {
        int n = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null) {
                n = i;
            } else {
                break;
            }
        }
        return getCardAt(n);
    }

    /**
     * Shuffles the deck using Fisher-Yates and the SecureRandom java util.
     */
    public void shuffle() {
        int length = size();
        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndexToSwap = rand.nextInt(i + 1);
            Card temp = cards[randomIndexToSwap];
            cards[randomIndexToSwap] = cards[i];
            cards[i] = temp;
        }
    }
}
