public class Deck {


    private Card[] cards;

    private int totalCards = cards.length;

    public Deck() {
        cards = new Card[52];
    }

    public void fill() {
        int i = 0;
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                cards[i]= new Card(r,s);
                i++;
            }
        }
    }

    public int size() {
        return cards.length;
    }

    public int size(Deck other) {
        return this.cards.length;
    }


    public void add(Card card) {
        Card[] newDeck = new Card [cards.length + 1];
        for (int i = 0; i < cards.length; i++) {
            newDeck[i] = cards[i];
        }
        newDeck[cards.length] = card;
        cards = newDeck;
    }

    public void add(Card card, Deck other) {
        Card[] newDeck = new Card [size(other) + 1];
        for (int i = 0; i < cards.length; i++) {
            newDeck[i] = cards[i];
        }
        newDeck[cards.length] = card;
        cards = newDeck;
    }

    public Card getCardAt(int n) {
        return cards[n];
    }

    public void moveTo(Deck other){
        add(getCardAt(cards.length-1));
        remove(1);
    }

    public void remove(int n) {
        Card[] newDeck = new Card [cards.length - 1];
        for (int i = 0; i < newDeck.length; i++) {
            newDeck[i] = cards[i];
        }
        cards = newDeck;
    }



}
