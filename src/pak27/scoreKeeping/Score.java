package pak27.scoreKeeping;


import pak27.util.ANSIColour;

/**
 * A singuar score containing a name, the points, the colour it should be displayed in and if the player used any tools to achieve the score
 * @author Panagiotis karapas
 * @version 2.1
 */
public class Score {
    private String name;
    private int score;//i could use a short since it's impossible to have > 255 piles, but again I want this to be a template for more card games!
    private ANSIColour colour;
    private boolean wasAssisted;

    /**
     *Constructor for the score.
     *
     * @param name The name of the scorer.
     * @param score The points scored.
     * @param colour What colour their name should be.
     * @param wasAssisted Was the game assisted by the computer?
     */
    public Score(String name, int score, ANSIColour colour, boolean wasAssisted) {
        this(name,score,colour);
        this.wasAssisted = wasAssisted;
    }
    /**
     *Constructor for the score.
     *
     * @param name The name of the scorer.
     * @param score The points scored.
     * @param colour What colour their name should be.
     */
    public Score(String name, int score, ANSIColour colour) {
        this(name,score);
        this.colour = colour;
    }
    /**
     *Constructor for the score.
     *
     * @param name The name of the scorer.
     * @param score The points scored.
     */
    public Score(String name, int score) {
        this.name = name;
        this.score = score;
        this.colour = ANSIColour.BLACK;
        wasAssisted = false;
    }

    /**
     * Gets name.

     *
     * @return value of name.
     */
    public String getName() {
        return name;
    }


    /**
     * Gets score.
     *
     * @return value of score.
     */
    public int getScore() {
        return score;
    }


    /**
     * Gets colour.
     *
     * @return value of colour.
     */
    public ANSIColour getColour() {
        return colour;
    }


    /**
     * Gets wasAssisted.
     *
     * @return value of wasAssisted
     */
    public boolean wasAssisted() {
        return wasAssisted;
    }

    /**
     * Sets the value of name.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value of score.
     *
     * @param score The new score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the value of colour.
     *
     * @param colour The new colour.
     */
    public void setColour(ANSIColour colour) {
        this.colour = colour;
    }

    /**
     * Sets the value of wasAssisted.
     *
     * @param wasAssisted The new value.
     */
    public void setWasAssisted(boolean wasAssisted) {
        this.wasAssisted = wasAssisted;
    }

    @Override
    public String toString() {
        return name + " " + score;
    }

}
