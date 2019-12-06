import java.util.Arrays;

/** Class to test our card, deck, and games classes. */
public class KlondikeTester {

    private static int correctTests = 0;
    private static int totalTests = 0;

    private static void clearCounts() {
        correctTests = 0;
        totalTests = 0;
    }

    private static void countTest(boolean correct) {
        if(correct) {
            correctTests++;
        } else {
            // Uncomment for a trace of which test failed.
            new Exception("Failed Test").printStackTrace();
        }
        totalTests++;
    }

    public static void testGetRank() {
        countTest(Rank.THREE.equals(new Card(Rank.THREE, Suit.HEARTS).getRank()));
    }

    public static void testGetSuit() {
        countTest(Suit.HEARTS.equals(new Card(Rank.THREE, Suit.HEARTS).getSuit()));
    }

    public static void testFaceUp() {
        Card c = new Card(Rank.SEVEN, Suit.SPADES);
        countTest(c.isFaceUp());
        c.flipOver();
        countTest(!c.isFaceUp());
        c.flipOver();
        countTest(c.isFaceUp());
    }

    public static void testIsRed() {
        countTest(!new Card(Rank.FOUR, Suit.CLUBS).isRed());
        countTest(!new Card(Rank.FOUR, Suit.SPADES).isRed());
        countTest(new Card(Rank.FOUR, Suit.HEARTS).isRed());
        countTest(new Card(Rank.FOUR, Suit.DIAMONDS).isRed());
    }



    public static void testSize() {
        Deck deck = new Deck();
        countTest(0 == deck.size());
        deck.add(new Card(Rank.FIVE, Suit.DIAMONDS));
        countTest(1 == deck.size());
        deck.add(new Card(Rank.SIX, Suit.DIAMONDS));
        countTest(2 == deck.size());
    }

    public static void testGetCardAt() {
        Deck deck = new Deck();
        Card c = new Card(Rank.FIVE, Suit.DIAMONDS);
        Card d = new Card(Rank.KING, Suit.CLUBS);
        deck.add(c);
        deck.add(d);
        countTest(c == deck.getCardAt(0));
        countTest(d == deck.getCardAt(1));
    }

    public static void testGetTop() {
        Deck deck = new Deck();
        countTest(null == deck.getTopCard());
        deck.add(new Card(Rank.FIVE, Suit.DIAMONDS));
        Card c = new Card(Rank.SIX, Suit.DIAMONDS);
        deck.add(c);
        countTest(c == deck.getTopCard());
    }

    public static void testFill() {
        Deck deck = new Deck();
        deck.fill();
        countTest(52 == deck.size());
        // Keep track of which cards have been seen
        boolean[][] seen = new boolean[13][4];
        for (int i = 0; i < deck.size(); i++) {
            Card c = deck.getCardAt(i);
            // If a card is seen again, fail
            countTest(!seen[c.getRank().ordinal()][c.getSuit().ordinal()]);
            seen[c.getRank().ordinal()][c.getSuit().ordinal()] = true;
        }
    }

    public static void testMove1() {
        Deck deck = new Deck();
        deck.fill();
        Deck d = new Deck();
        d.add(new Card(Rank.NINE, Suit.HEARTS));
        deck.moveTo(d);
        countTest(2 == d.size());
        countTest(51 == deck.size());
        Card c = d.getCardAt(0);
        countTest(c != null && Rank.NINE == c.getRank());
        countTest(c != null && Suit.HEARTS == c.getSuit());
        c = d.getCardAt(1);
        countTest(c != null && Rank.KING == c.getRank());
        countTest(c != null && Suit.DIAMONDS == c.getSuit());
    }

    public static void testMoveN() {
        Deck deck = new Deck();
        deck.fill();
        Deck d = new Deck();
        d.add(new Card(Rank.NINE, Suit.HEARTS));
        Card[] before = { deck.getCardAt(49), deck.getCardAt(50), deck.getCardAt(51) };
        deck.moveTo(d, 3);
        countTest(4 == d.size());
        countTest(49 == deck.size());
        Card c = d.getCardAt(0);
        countTest(c != null && Rank.NINE == c.getRank());
        countTest(c != null && Suit.HEARTS == c.getSuit());
        for (int i = 0; i < 3; i++) {
            c = d.getCardAt(i + 1);
            countTest(c != null && before[i].getRank() == c.getRank());
            countTest(c != null && before[i].getSuit() == c.getSuit());
        }
    }

    public static void testShuffle() {
        Deck deck = new Deck();
        // This test is inspired by experiments 1.1.36 and 1.1.37 in Sedgewick
        // and Wayne, Algorithms, 4th edition. It tests whether, over many
        // shuffles, each card is equally likely to end up in each position. It
        // may fail once in a great while, but it generally passes for a correct
        // shuffling algorithm and fails for common incorrect algorithms.
        Deck unshuffled = new Deck();
        unshuffled.fill();
        int size = unshuffled.size();
        if(size < 0) size = 0;
        int numTrials = 5000;
        int[][] counts = new int[size][size];
        for (int trial = 0; trial < numTrials; trial++) {
            deck = new Deck();
            deck.fill();
            deck.shuffle();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < deck.size(); j++) {
                    Card a = unshuffled.getCardAt(i);
                    Card b = deck.getCardAt(j);
                    if ((a.getRank() == b.getRank()) && (a.getSuit() == b.getSuit())) {
                        counts[i][j]++;
                    }
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                countTest(counts[i][j] > (numTrials / 52.0) * 0.5);
                countTest(counts[i][j] < (numTrials / 52.0) * 1.5);
            }
        }


        // Test a second time with a partial deck
        Card[] cards = { new Card(Rank.QUEEN, Suit.HEARTS),
          new Card(Rank.KING, Suit.SPADES),
          new Card(Rank.ACE, Suit.CLUBS),
          new Card(Rank.TWO, Suit.DIAMONDS),
          new Card(Rank.THREE, Suit.HEARTS),
          new Card(Rank.FOUR, Suit.SPADES),
          new Card(Rank.FIVE, Suit.CLUBS)};
        size = unshuffled.size() < 0 ? 0 : cards.length;
        counts = new int[size][size];
        for (int trial = 0; size > 0 && trial < numTrials; trial++) {
            deck = new Deck();
            for(Card card : cards) {
                deck.add(card);
            }
            deck.shuffle();
            int nullCount = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < deck.size(); j++) {
                    Card a = cards[i];
                    Card b = deck.getCardAt(j);
                    if(b == null) {
                        nullCount++;
                    } else if ((a.getRank() == b.getRank()) && (a.getSuit() == b.getSuit())) {
                        counts[i][j]++;
                    }
                }
            }
            countTest(nullCount == 0);
        }
        double dSize = (double)size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                countTest(counts[i][j] > (numTrials / dSize) * 0.5);
                countTest(counts[i][j] < (numTrials / dSize) * 1.5);
            }
        }
    }



    public static void testSuccessor() {
        KlondikeModel model = null;
        try { model = new KlondikeModel(); } catch (Exception ignore) {}
        countTest(null != model);
        Card c = new Card(Rank.QUEEN, Suit.CLUBS);
        Card d = new Card(Rank.KING, Suit.HEARTS);
        boolean okay = model != null
          && c.getRank() != null && d.getRank() != null
          && c.getSuit() != null && d.getSuit() != null;
        countTest(okay && model.successor(c, d));
        countTest(okay && !model.successor(d, c));
        countTest(okay && !model.successor(c, null));
        countTest(okay && model.successor(d, null));
    }

    public static void testKlondikeMove() {
        KlondikeModel model = null;
        try { model = new KlondikeModel(); } catch (Exception ignore) {}
        countTest(null != model);
        Deck deck = new Deck();
        // Deck is the source pile
        Card c = new Card(Rank.TEN, Suit.SPADES);
        c.flipOver();
        deck.add(c);
        c = new Card(Rank.JACK, Suit.HEARTS);
        c.flipOver();
        deck.add(c);
        deck.add(new Card(Rank.SIX, Suit.HEARTS));
        deck.add(new Card(Rank.FIVE, Suit.SPADES));
        deck.add(new Card(Rank.FOUR, Suit.DIAMONDS));
        deck.add(new Card(Rank.THREE, Suit.CLUBS));
        // Destination is the other pile
        Deck destination = new Deck();
        c = new Card(Rank.KING, Suit.HEARTS);
        c.flipOver();
        destination.add(c);
        destination.add(new Card(Rank.EIGHT, Suit.DIAMONDS));
        destination.add(new Card(Rank.SEVEN, Suit.SPADES));
        destination.add(new Card(Rank.SIX, Suit.DIAMONDS));
        // It is illegal to move from destination to deck
        if(null != model) model.klondikeMove(destination, deck);
        countTest(6 == deck.size());
        countTest(4 == destination.size());
        // Moving from deck to destination should have the desired effect
        if(null != model) model.klondikeMove(deck, destination);
        countTest(3 == deck.size());
        countTest(7 == destination.size());
        for (int i = 0; i < 6; i++) {
            Rank rank = destination.size() == 7 ?
              destination.getCardAt(destination.size() - 1 - i).getRank() : null;
            countTest(rank != null && 2 + i == rank.ordinal());
        }
    }

    public static void testMoveKingToEmptyDeck() {
        KlondikeModel model = null;
        try { model = new KlondikeModel(); } catch (Exception ignore) {}
        countTest(null != model);
        Deck deck = new Deck();
        // A 7 can't be moved to an empty pile
        deck.add(new Card(Rank.SEVEN, Suit.DIAMONDS));
        Deck destination = new Deck();
        if(null != model) model.klondikeMove(deck, destination);
        countTest(1 == deck.size());
        // ...but a king can
        Card c = deck.getCardAt(0);
        if(c != null && c.isFaceUp()) c.flipOver();
        deck.add(new Card(Rank.KING, Suit.HEARTS));
        if(null != model) model.klondikeMove(deck, destination);
        countTest(1 == deck.size());
        countTest(1 == destination.size());
    }

    public static void testDrawNextCard() {
        KlondikeModel model = null;
        try { model = new KlondikeModel(); } catch (Exception ignore) {}
        countTest(null != model);
        // There are 23 cards left in the deck after the initial deal
        for (int i = 1; i <= 23; i++) {
            if(null != model) model.drawNextCard();
            countTest(null != model && 23 - i == model.getDeck().size());
            countTest(null != model && 1 + i == model.getDrawPile().size());
        }
        // Trying to draw another card should flip the deck
        if(null != model) model.drawNextCard();
        countTest(null != model && 0 == model.getDrawPile().size());
        countTest(null != model && 24 == model.getDeck().size());
    }

    public static void testMoveToFoundation() {
        KlondikeModel model = null;
        try { model = new KlondikeModel(); } catch (Exception ignore) {}
        countTest(null != model);
        Deck source = new Deck();
        source.add(new Card(Rank.SEVEN, Suit.SPADES));
        Card c = source.getTopCard();
        if(c != null && c.isFaceUp()) c.flipOver();
        source.add(new Card(Rank.TWO, Suit.HEARTS));
        source.add(new Card(Rank.ACE, Suit.HEARTS));
        // This illegal move should have no effect
        if(null != model) model.moveToFoundation(source, Suit.CLUBS.ordinal());
        // These should work
        if(null != model) model.moveToFoundation(source, Suit.HEARTS.ordinal());
        if(null != model) model.moveToFoundation(source, Suit.HEARTS.ordinal());
        // This should not
        if(null != model) model.moveToFoundation(source, Suit.SPADES.ordinal());
        countTest(1 == source.size());
        Card d = source.getTopCard();
        countTest(d != null && d.isFaceUp());
    }

    public static void testMoveToTableau() {
        KlondikeModel model = null;
        try { model = new KlondikeModel(); } catch (Exception ignore) {}
        countTest(null != model);
        // Arrange the tableau to make some moves possible
        Deck garbage = new Deck();
        if(null != model) model.getTableau(6).moveTo(garbage);
        if(null != model) model.getTableau(6).add(new Card(Rank.SIX, Suit.SPADES));
        if(null != model) model.getTableau(5).moveTo(garbage);
        if(null != model) model.getTableau(5).add(new Card(Rank.QUEEN, Suit.CLUBS));
        if(null != model) model.getTableau(5).add(new Card(Rank.FOUR, Suit.CLUBS));
        if(null != model) model.getTableau(5).add(new Card(Rank.THREE, Suit.HEARTS));
        if(null != model) model.getDrawPile().add(new Card(Rank.FIVE, Suit.DIAMONDS));
        // Moving from the draw pile to tableau pile 5 should have no effect
        if(null != model) model.moveToTableau(model.getDrawPile(), 5);
        countTest(null != model && 2 == model.getDrawPile().size());
        // Moving to pile 6 should work
        if(null != model) model.moveToTableau(model.getDrawPile(), 6);
        countTest(null != model && 1 == model.getDrawPile().size());
        // Now a move from tableau pile 5 to 6 should move two cards
        if(null != model) model.moveToTableau(model.getTableau(5), 6);
        countTest(null != model && 10 == model.getTableau(6).size());
        countTest(null != model && 6 == model.getTableau(5).size());
    }

    public static void testImageFilename() {
        Card c = new Card(Rank.THREE, Suit.HEARTS);
        String faceName = "card-images" + java.io.File.separator + "47.png";
        countTest(faceName.equals(Klondike.imageFilename(c)));
        c.flipOver();
        String backName = "card-images" + java.io.File.separator + "b2fv.png";
        countTest(backName.equals(Klondike.imageFilename(c)));

        // Make sure we can find the files.
        // If we can't, the card images aren't where we expect them to be.
        java.nio.file.Path path = java.nio.file.Paths.get(faceName);
        countTest(java.nio.file.Files.isReadable(path));
        path = java.nio.file.Paths.get(backName);
        countTest(java.nio.file.Files.isReadable(path));
    }

    public static void main(String[] args) {
        int correct = 0;
        int total = 0;

        System.out.println("========== TESTING STUDENT CODE ========== ");
        System.out.println("---------- testing Card ----------");

        clearCounts();
        System.out.println("testGetRank");
        testGetRank();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testGetSuit");
        testGetSuit();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testFaceUp");
        testFaceUp();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testIsRed");
        testIsRed();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);


        System.out.println("---------- testing Deck ----------");

        clearCounts();
        System.out.println("testSize");
        testSize();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testGet");
        testGetCardAt();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testGetTop");
        testGetTop();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testFill");
        testFill();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testMove1");
        testMove1();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testMoveN");
        testMoveN();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testShuffle");
        testShuffle();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);


        System.out.println("=========== TESTING GAME INTERNALS ===========");
        System.out.println("---------- testing KlondikeModel ----------");

        clearCounts();
        System.out.println("testSuccessor");
        testSuccessor();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testKlondikeMove");
        testKlondikeMove();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testMoveKingToEmptyDeck");
        testMoveKingToEmptyDeck();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testDrawNextCard");
        testDrawNextCard();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testMoveToFoundation");
        testMoveToFoundation();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        clearCounts();
        System.out.println("testMoveToTableau");
        testMoveToTableau();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);


        System.out.println("---------- testing Klondike ----------");

        clearCounts();
        System.out.println("testImageFilename");
        testImageFilename();
        correct+= correctTests;
        total += totalTests;
        System.out.println("  Passed " + correctTests + " of " + totalTests);

        System.out.println("---------- testing complete ----------");
        System.out.println("Overall: Passed " + correct  + " out of " + total + " tests");

    }
}