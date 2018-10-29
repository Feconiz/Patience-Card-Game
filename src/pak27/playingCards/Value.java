package pak27.playingCards;

/**
 * The different values of cards.
 * @author Panagiotis karapas
 * @version 1.1
 */
public enum Value {
    ACE('A', 1),
    TWO('2', 2),
    THREE('3', 3),
    FOUR('4', 4),
    FIVE('5', 5),
    SIX('6', 6),
    SEVEN('7', 7),
    EIGHT('8', 8),
    NINE('9', 9),
    TEN('T', 10),
    JACK('J', 11),
    QUEEN('Q', 12),
    KING('K', 13),
    JOKER('O', 0);

    private char asChar;
    private int num;

    Value(char asChar, int num) {
        this.asChar = asChar;
        this.num = num;
    }

    /**
     * Gets the Value of a card as a number.
     * @return The value of the card represented as a number. (A = 1, K = 13, Joker = 0)
     */
    public int getNum() {
        return num;
    }

    /**
     * Returns a Value from the provided num.
     * @param num The number of value we should return.
     * @return The Value found, null if the value doesn't exist.
     */
    public static Value getByNum(int num) {
        for (Value v : Value.values()) {
            if (v.getNum() == num) {
                return v;
            }
        }
        return null;
    }

    /**
     * Returns all the values except for Joker.
     * @return an array containing all the the values except Joker. If you want to include joker then use Value.values()
     */
    public static Value[] allExceptJoker() {
        return new Value[]{ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING};
    }

    /**
     * Returns the value represented by a single char. (2 = '2', King = 'K', Joker = 'O')
     * @return A char representing the value.
     */
    public char getAsChar() {
        return asChar;
    }


}
