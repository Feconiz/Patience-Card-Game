package pak27.playingCards;

import java.util.ArrayList;

/**
 * Implements a FILO Scheme using an array list.
 * @author Panagiotis karapas
 * @version 2.1
 */
public class Stack {
    private ArrayList<Card> cards;

    /**
     * Creates an empty stack.
     */
    public Stack() {
        cards = new ArrayList<>();
    }

    /**
     * Adds a card at the top of the stack.
     * @param c The card to be added.
     */
    public void add(Card c) {
        cards.add(c);
    }

    /**
     * Remove and returns the last element of the stack. If the stack is empty, it returns null.
     *
     * @return the last element of the stack.
     */
    public Card pop() {
        if(cards.size() == 0) {
            return null;
        }
        Card c = cards.get(cards.size() - 1);
        cards.remove(cards.size()-1);
        return c;
    }

    /**
     * Getter for the cards in this stack.
     *
     * @return The cards in this stack.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Gets the card at the top of the deck.
     * @return The topmost Card.
     */
    public Card lookAtTopMost() {
        return cards.get(cards.size() - 1);
    }


    /**
     * Sets the value of cards.
     *
     * @param cards the new cards value.
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Adds the other pile on top of this one.
     * Does not remove the cards from the other.
     *
     * @param other the stack to add on top of this.
     */
    public void addOnTop(Stack other) {
        for (Card c : other.getCards()) {
            add(c);
        }
    }

    /**
     * Compiles all the cards in the stack to a  string.
     * @return A string containing all relevant information about ths stack.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stack{\n");
        sb.append("     cards={\n");

        for (Card c : cards) {
            sb.append("         ");
            sb.append(c.toString());
            sb.append("\n");
        }
        sb.append("     }\n}");
        return sb.toString();
    }

    /**
     * Copies this stack, creating an identical copy, also copies the cards of the stacks.
     * @param includeCardImg passed as an argument to card.copy
     * @return The copy of this stack.
     */
    public Stack copy(boolean includeCardImg) {
        Stack ret = new Stack();
        for (Card c : cards) {
            ret.add(c.copy(includeCardImg));
        }
        return ret;
    }
}
