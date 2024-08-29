
package images;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame {

    private ArrayList<String> cards;
    private String[] board;
    private boolean[] flipped;
    private int pairsFound;

    public MemoryGame() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] faceNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

        cards = new ArrayList<>();
        for (String suit : suits) {
            for (String faceName : faceNames) {
                cards.add(faceName + " of " + suit);
            }
        }

        Collections.shuffle(cards);
        cards = new ArrayList<>(cards.subList(0, 10)); // Select 10 cards
        cards.addAll(cards); // Create pairs
        Collections.shuffle(cards);

        board = new String[cards.size()];
        flipped = new boolean[cards.size()];
        pairsFound = 0;
    }

    public String getCard(int index) {
        return cards.get(index);
    }

    public boolean isFlipped(int index) {
        return flipped[index];
    }

    public void flipCard(int index) {
        flipped[index] = true;
        board[index] = cards.get(index);
    }

    public void unflipCard(int index) {
        flipped[index] = false;
        board[index] = null;
    }

    public boolean checkMatch(int firstIndex, int secondIndex) {
        return cards.get(firstIndex).equals(cards.get(secondIndex));
    }

    public void foundPair() {
        pairsFound++;
    }

    public boolean allPairsFound() {
        return pairsFound == 10;
    }

    public String[] getBoard() {
        return board;
    }
}
