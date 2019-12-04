public class Card {

    private Rank rank;

    private Suit suit;

    private boolean faceUp;
    private boolean red;

    private String color;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;

    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public boolean isRed() {
        return (getSuit() == Suit.HEARTS || getSuit() == Suit.DIAMONDS);
    }

    public void flipOver() {
        faceUp = !faceUp;
    }

    @Override
    public String toString() {
        return String.format("%s%s", getRank(), getSuit());
    }

//    @Override
//    public int compareTo(Card other) {
//        int result = suit.getColor().compareTo(other.suit.getColor());
//        if (result == 0) {
//            result = suit.compareTo(other.suit);
//            if (result == 0) {
//                result = rank.compareTo(other.rank);
//            }
//        }
//        return result;
//    }

}
