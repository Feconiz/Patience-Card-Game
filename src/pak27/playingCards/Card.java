package pak27.playingCards;

import javafx.scene.image.Image;
import pak27.util.ANSIColour;
import pak27.util.Util;

/**
 * A class representing a single card.
 *
 * @author Panagiotis karapas
 * @version 2.4
 */
public class Card {
    private transient Image img;
    private Suit suit;
    private Value value;

    /**
     * Creates a card.
     * @param suit The suit of the card.
     * @param value The value of the card.
     */
    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
        setImg();
    }

    private Card(Suit suit, Value value, boolean loadImg) {
        this.suit = suit;
        this.value = value;
        if (loadImg) {
            setImg();
        } else {
            img = null;
        }
    }

    private String getImagePath() {
        return "cards/" + value.getAsChar() + "" + suit.getAsChar() + ".png";
    }

    /**
     * Gets img
     *
     * @return value of img
     */
    public Image getImg() {
        return img;
    }

    /**
     * Sets the image to the appropriate one in the resource folder.
     */
    void setImg(){
        this.img = new Image(getClass().getClassLoader().getResource(getImagePath()).toString(), true);
    }

    /**
     * Gets the suit of the card
     *
     * @return value of suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Gets the value of the card
     *
     * @return value of card
     */
    public Value getValue() {
        return value;
    }

    /**
     * Returns the card in the appropriate colour if colours are supported.
     * @return the card information.
     */
    @Override
    public String toString() {

        return Util.getColoured(value.getAsChar() + "" + suit.getAsChar(), suit.getColour()==Colour.BLACK? ANSIColour.BLACK: ANSIColour.RED);
    }

    /**
     * Checks if this card and the other card have either the save value or suit.
     * @param other The other card to check.
     * @return True if they do, false otherwise.
     */

    public boolean sameValOrSuit(Card other) {
        return value.equals(other.value) || suit.equals(other.suit);
    }

    /**
     * Creates a new instance of the card class that is identical to this one (except maybe the image).
     * @param includeCardImg Should the image of this cad be loaded. 9true only if the card will be displayed).
     * @return The new card.
     */
    public Card copy(boolean includeCardImg) {
        return new Card(suit, value, includeCardImg);
    }

    /**
     * Checks if thiscard is the same with o
     * @param o The other card to check against
     * @return true if the cards arethe same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit &&
                value == card.value;
    }

}
