package pak27.scoreKeeping;


import pak27.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;


/**
 * The scoreboard class provides an easy to use scoreboard interface. After instantiating it, submiting a score is as easy as calling the submit method on every new score in the game.
 * The method will determine if the score should be on the board or not.
 *
 * @author Chris Loftus, Lynda Thomas and Panagiotis karapas
 * @version 4.2
 */
public class ScoreBoard {
    private int numberOfEntries;
    private Score[] board;

    //this is only here because i wanted the scoreboard class to be reusable in other games
    //where smaller scores are better
    private boolean isBiggerBetter;

    /**
     * Empty constructor, creates a scoreBoard with 10 entries and bigger values are better.
     */
    public ScoreBoard() {
        this(10, true);
    }

    public ScoreBoard(int numberOfEntries, boolean isBiggerBetter) {
        this.numberOfEntries = numberOfEntries;
        board = new Score[numberOfEntries];
        this.isBiggerBetter = isBiggerBetter;
        init();
    }

    private void init() {
        for (int i = 0; i < numberOfEntries; i++) {
            board[i] = new Score("<Empty>", 0);
        }
    }

    /**
     * Reads a scoreboard from a json file and returns it.
     * @param infileName The path to the file.
     * @return The scoreboard read.
     */
    public static ScoreBoard readFromFile(String infileName) {//decided to go with static and set it in my game, because i don't want to initialise the scoreboard if i am gonna override it anyway
        try (FileReader fr = new FileReader(infileName)) {
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

            Gson gson = gsonBuilder.create();
            ScoreBoard loaded = gson.fromJson(fr, ScoreBoard.class);
            return loaded == null ? new ScoreBoard(10, true) : loaded;//if something goes wrong while reading or file is empty return a new Scoreboard with a default size of 10
        } catch (IOException e) {
            Util.printErrorln("[ERROR] Failed to load file! Creating new scoreBoard!");
            return new ScoreBoard(10, true);
        }
    }

    /**
     * Writes this scoreboard to a file, it uses the json format.
     * @param filename the file to write to.
     */
    public void saveToFile(String filename) {// Again using try-with-resource so that I don't need to close the file explicitly
        try (PrintWriter outfile = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, outfile);
        } catch (IOException e) {
            Util.printErrorln("[ERROR] Failed to save!");
        }
    }

    /**
     * Gives the last score on the board (so the worst one).
     * @return the worst score.
     */
    public Score getWorst() {
        return board[board.length - 1];
    }

    private void add(Score score) {
        int i = board.length - 1;
        while ((board[i].getScore() < score.getScore() && isBiggerBetter)
                || (board[i].getScore() > score.getScore() && !isBiggerBetter)
                || (board[i].getScore() == score.getScore() && board[i].wasAssisted() && !score.wasAssisted())) {
            i--;
            if (i <= -1) {
                break;
            }
        }
        insert(i + 1, score);

    }


    private void insert(int index, Score score) {
        Integer i = new Integer(5);
        Score swap;
        while (index < board.length) {
            swap = board[index];
            board[index] = score;
            score = swap;
            index++;
        }
    }

    /**
     * Gets the scoreboard as an array of Scores.
     * @return the board.
     */
    public Score[] getBoard() {
        return board;
    }

    /**
     * Checks if the score should be on the board, and if that is the case then it asks for the players information and puts them in the correct position.
     * @param score The score to be submitted
     * @param wasAssisted A boolean value which determines if the game was assisted
     */
    public void submit(int score, boolean wasAssisted) {
        if ((getWorst().getScore() < score && isBiggerBetter) || (getWorst().getScore() > score && !isBiggerBetter)) {
            System.out.println("You qualify to be on the scoreboard!\nPlease give your name: ");
            String name = Util.SCAN.nextLine();
            while (name.length() <= 0 || name.length() > 15) {
                System.out.println("Name has to be between 1 (inclusive) and 15 (inclusive) characters long.\nPlease give your name:");
                name = Util.SCAN.nextLine();
            }
            ANSIColour colour = ANSIColour.BLACK;
            if (Util.yesNoQuestion("Would you like your name to have a color " + (Util.getUseColor()?"":"(even if you won't be able to see it)") + "? (Y/N)")) {
                System.out.println("Choose one of the following by entering the letter associated with it: ");
                colour = selectColour();
            }

            add(new Score(name, score, colour, wasAssisted));
        }
    }

    private static ANSIColour selectColour(){
        System.out.println(Util.getColoured("B - Black", ANSIColour.BLACK) + "\n" +
                Util.getColoured("R  - Red", ANSIColour.RED) + "\n" +
                Util.getColoured("G  - Green", ANSIColour.GREEN) + "\n" +
                Util.getColoured("BL - Blue", ANSIColour.BLUE) + "\n" +
                Util.getColoured("Y  - Yellow", ANSIColour.YELLOW) + "\n" +
                Util.getColoured("C  - Cyan", ANSIColour.CYAN) + "\n" +
                Util.getColoured("M  - Magenta", ANSIColour.MAGENTA));
        do{
            switch (Util.SCAN.nextLine().toUpperCase()){
                case "B":
                    return ANSIColour.BLACK;
                case "R":
                    return ANSIColour.RED;
                case "G":
                    return ANSIColour.GREEN;
                case "BL":
                    return ANSIColour.BLUE;
                case "Y":
                    return ANSIColour.YELLOW;

                case "C":
                    return ANSIColour.CYAN;
                case "M":
                    return ANSIColour.MAGENTA;
            }
            System.out.println("Invalid selection! Try again: ");
        }while(true);
    }

}
