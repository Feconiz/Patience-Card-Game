package pak27;

import javafx.application.Application;
import javafx.stage.Stage;
import pak27.autoplay.AutoPlayer;
import pak27.playingCards.Card;
import pak27.playingCards.Deck;
import pak27.playingCards.Stack;
import pak27.scoreKeeping.Score;
import pak27.scoreKeeping.ScoreBoard;
import pak27.util.ANSIColour;
import pak27.util.Util;
import uk.ac.aber.dcs.cs12320.cards.gui.javafx.CardTable;

import java.util.ArrayList;

/**
 * The game of patience main class
 *
 * @author Chris Loftus, Lynda Thomas and Panagiotis karapas
 * @version 4.2
 */
public class Game extends Application {
    private CardTable cardTable;
    private Stage stage;

    private ArrayList<Stack> gameBoard;
    private ScoreBoard scores;
    private int numberOfDecks;

    private boolean isFinished = false;
    private boolean update = true;//Does the Gui need updating?(to save resources)
    private boolean wasRunAssisted;

    /**
     * The method run in the beggining of the program.
     * @param stage The stage used to render the graphics.
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        Util.SCAN.useDelimiter("\n|\n?\r");

        // The interaction with this game is from a command line
        // menu. We need to create a separate non-GUI thread
        // to run this in. DO NOT REMOVE THIS.
        Runnable commandLineTask = () -> {

            if(!Util.yesNoQuestion(Util.getColoured("Is this text red for you? (Y/N)", ANSIColour.RED))) {
                Util.setUseColor(false);
            }
            initialise();
            do{
                if(update){
                    cardTable.cardDisplay(gameBoard);
                    update = false;
                }
                printMenu();
                runMenu();
                if (!isFinished) {
                    checkGameOver();
                }
            }while (!isFinished);
        };
        Thread commandLineThread = new Thread(commandLineTask);

        // This is how we start the thread.
        // This causes the run method to execute.
        commandLineThread.start();
    }

    private void runMenu() {
        String input = Util.SCAN.nextLine();
        switch (input.toLowerCase()) {
            case "0":
                if(gameBoard.get(0) instanceof Deck) {
                    ((Deck) gameBoard.get(0)).shuffle();
                }
                break;
            case "1":
                System.out.println(gameBoard.get(0).toString());
                break;
            case "2":
                deal();
                break;
            case "3":
                move(1, 2);
                break;
            case "4":
                move(1, 4);
                break;
            case "5":
                amalgamateMiddle();
                break;
            case "6":
                printCardsOnTable();
                break;
            case "7":
                wasRunAssisted = true;
                autoPlayOnce();
                break;
            case "8":
                wasRunAssisted = true;
                autoPlay(Util.readInteger("How many times to play?: "));
                break;
            case "9":
                printScoreBoard();
                break;
            case "a":
                wasRunAssisted = true;
                autoRun();
                break;
            case "q":
                if (!Util.yesNoQuestion(Util.getColoured("If you exit now your score will be sub-optimal, are you sure you want to do that? (Y/N)", ANSIColour.RED))) {
                    break;
                }
                int score = Math.round(520 - 10*((gameBoard.size() + gameBoard.get(0).getCards().size() - 2)/((float)(numberOfDecks))));
                gameOver(score);
                break;
            default:
                Util.printErrorln("Invalid Input, try again!");
                break;
        }
    }
    private void printScoreBoard() {
        System.out.println(Util.getColoured("       -==={ ScoreBoard }====-", ANSIColour.GREEN));
        for (int i = 0; i < scores.getBoard().length; i++) {
            Score current = scores.getBoard()[i];
            StringBuilder spaces = new StringBuilder();
            for(int j = 0;j < 19-current.getName().length()-Integer.toString(current.getScore()).length();j++){//So the scores are aligned
                spaces.append(" ");
            }
            String currentLine = (i + 1) + ") " + (i<9?" ":"") + current.getName()  + spaces.toString() + (current.wasAssisted()?" (Assisted) ":"            ") + current.getScore();
            System.out.println(Util.getColoured(currentLine, current.getColour()));
        }
        System.out.println(Util.getColoured("       -=====================-", ANSIColour.GREEN));
    }

    private void printCardsOnTable() {
        StringBuilder sb = new StringBuilder("Cards displayed on the table:\n");
        for (int i = 1; i < gameBoard.size(); i++) {
            sb.append(gameBoard.get(gameBoard.size() - i).lookAtTopMost());
            sb.append(" ");
        }
        System.out.println(sb.toString());
    }

    private void autoRun() {
        int[][] instructions = AutoPlayer.solve(gameBoard, Util.readInteger("How many stacks is the maximum you want to have? (The less, the more time it takes): "));
        if (instructions != null) {
            Util.dealAll(gameBoard);
            cardTable.allDone();

            for (int[] instruction : instructions) {
                move(instruction[0] + 1, instruction[1] + 1);
            }
        } else {
            Util.printErrorln("The operation is impossible!");
        }
    }

    private void initialise() {
        isFinished = false;
        cardTable = new CardTable(stage, "The cards.");
        gameBoard = new ArrayList<>();
        gameBoard.add(new Deck());//So gameBoard.get(0) is always the starting pile
        numberOfDecks = Util.readInteger("How many decks do you want to play with? (1-4): ",5,0);

        boolean wasLoaded = ((Deck) gameBoard.get(0)).readCards(numberOfDecks, "cards.json");
        if(!wasLoaded){
            Util.printErrorln("Something went wrong!");
            System.exit(-1);
        }
        wasRunAssisted = false;
        if (scores == null) {//to ensure we don't read it again if they replay
            scores = ScoreBoard.readFromFile("scores.json");
        }
    }

    private void checkGameOver() {//if there is stuff to do, then just return otherwise end game.
        if (gameBoard.get(0).getCards().size() != 0) {
            return;
        }
        Card[] topCards = new Card[gameBoard.size() - 1];//all except the starting deck
        for (int i = 1; i < gameBoard.size(); i++) {
            topCards[i - 1] = gameBoard.get(i).lookAtTopMost();//minus oe to accommodate for the deck's absence
        }
        for (int i = 0; i + 1 < topCards.length; i++) {//checking up to i+1 since the last card will have been tested already from the previous one's
            if (i + 3 < topCards.length && topCards[i].sameValOrSuit(topCards[i + 3])) {//check furthest first, make sure in bounds
                return;
            }
            if (i + 1 < topCards.length && topCards[i].sameValOrSuit(topCards[i + 1])) {
                return;
            }
        }
        gameOver();
    }

    private void autoPlay(int times) {
        for (; times > 0; times--) {
            if (!autoPlayOnce()) {//make sure we didn't gameOver in the  autoPlayOnce()
                return;
            }
        }
    }

    private boolean autoPlayOnce() {//Merges once or deals once if no merge is possible
        Card[] topCards = new Card[gameBoard.size() - 1];//all except the starting deck
        for (int i = 1; i < gameBoard.size(); i++) {
            topCards[i - 1] = gameBoard.get(i).lookAtTopMost();//minus oe to accommodate for the deck's absence
        }
        for (int i = 0; i + 1 < topCards.length; i++) {//checking up to i+1 since the last card will have been tested already from the previous one's
            if (i + 3 < topCards.length && topCards[i].sameValOrSuit(topCards[i + 3])) {//check furthest first, make sure in bounds
                move(i + 1, i + 4);//adding one since we removed one whe creating the array
                System.out.println("Found a double jump move!");
                return true;
            }
            if (i + 1 < topCards.length && topCards[i].sameValOrSuit(topCards[i + 1])) {
                move(i + 1, i + 2);//adding one since we removed one whe creating the array
                System.out.println("Found a single move!");
                return true;
            }
        }
        if (cardTable.isStartingPileEmpty()) {//in case a huge number is entered
            System.out.println("No possible move, and the starting pile is empty!");
            cardTable.cardDisplay(gameBoard);//since the game will finish and it wouldn't update otherwise!
            gameOver();
            return false;
        } else {
            System.out.println("No legal move found! Dealing one card.");
            deal();
            return true;
        }

    }

    private void gameOver() {
        int score = Math.round(520 - 10*((gameBoard.size() - 2)/((float)(numberOfDecks))));
        gameOver(score);
    }

    private void gameOver(int score) {
        cardTable.cardDisplay(gameBoard);//make sure the display is up to date!

        System.out.println("Game over!");
        System.out.println("You managed to get it down to " + (gameBoard.size() - 1) + " piles! Your score is: " + score);
        scores.submit(score, wasRunAssisted);
        isFinished = true;
        if (Util.yesNoQuestion("Do you want to replay?(Y/N)")) {
            System.out.println("Restarting game!");
            initialise();
        } else {
            System.out.println("Close the GUI to exit!");
            printScoreBoard();
            scores.saveToFile("scores.json");
        }
    }
    private void amalgamateMiddle() {
        int from = gameBoard.size() - Util.readInteger("Index of pile to be moved: ");//since we want to count from left to right

        if (from >= gameBoard.size() || from <= 0) {
            Util.printErrorln("Pile doesn't exist!");
            return;
        }

        int to = gameBoard.size() - Util.readInteger("Index of destination pile: ");//since we want to count from left to right
        move(from, to);

    }

    /**
     * If a legal move, moves the from pile on the to pile.
     *
     * @param from index of the from pile on the gameBoard (0 being the rightmost pile).
     * @param to   index of the destination pile on  the gameBoard.
     */
    private void move(int from, int to) {
        if (from >= gameBoard.size() || to >= gameBoard.size() || from <= 0 || to <= 0) {
            Util.printErrorln("Illegal Move! Both piles need to exist before making a move!");
            return;
        }
        if (Math.abs(to - from) > 3) {//taking the absolute value since its bidirectional
            Util.printErrorln("Illegal Move! Piles can be at most 2 piles apart!");
            return;
        }

        if (!gameBoard.get(to).lookAtTopMost().sameValOrSuit(gameBoard.get(from).lookAtTopMost())) {
            Util.printErrorln("Illegal Move! Top most card of the 2 piles must have either the same value or suit!");
            return;
        }
        if (!update) {
            update = true;//make sure the display is updated!
        }
        gameBoard.get(to).addOnTop(gameBoard.get(from));
        gameBoard.remove(from);
    }

    /**
     * Removes a card from the starting pile and creates a new pile to put it on.
     */
    private void deal() {
        if (!cardTable.isStartingPileEmpty()) {
            Stack s = new Stack();
            s.add(gameBoard.get(0).pop());
            gameBoard.add(1, s);

            if (gameBoard.get(0).getCards().size() == 0) {
                cardTable.allDone();
            }

            update = true;//make sure the display is updated!
        } else {
            Util.printErrorln("No more cards to deal!");//Pile is not removed because a lot of stuff is based on the basis that the 0th pile is the deck!
        }
    }

    private static void printMenu() {
        //It looks messy but it's constant so it made more sense than concaterating or having multiple calls
        System.out.print("0. Shuffle the cards.\n1. Show the pack.\n2. Deal a card.\n3. Make a move, move last pile onto previous one.\n4. Make a move, move last pile back over two piles.\n5. Amalgamate piles in the middle (by giving their numbers, leftmost card is 1).\n6. Show all the displayed cards in text form on the command line.\n7. Play for me once.(Furthest first).\n8. Play for me a number of times.\n9. Show high-scores.\na. Plays automatically to reduce the stacks to the desired amount!\nq. Quit this game.\nChoose one of the above: ");

    }

    /**
     * The main method just calls  Application.launch(args)
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Application.launch(args);
    }
}
