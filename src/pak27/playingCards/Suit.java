package pak27.playingCards;

/**
 * The different Suits of cards.
 * @author Panagiotis karapas
 * @version 1.2
 */
public enum Suit {
    HEART(Colour.RED, 'H'),
    SPADE(Colour.BLACK, 'S'),
    DIAMOND(Colour.RED, 'D'),
    CLUB(Colour.BLACK, 'C');

    private Colour colour;
    private char asChar;

    Suit(Colour colour, char asChar) {
        this.colour = colour;
        this.asChar = asChar;
    }

    /**
     * Gets the colour of the cards that have this suit.
     *
     * @return value of colour
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Getsthe Suit as a single char.
     * @return The suit as a char.
     */
    public char getAsChar() {
        return asChar;
    }
}
