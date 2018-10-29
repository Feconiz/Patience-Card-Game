package pak27.util;

import pak27.playingCards.Stack;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A utillity class having different functionality for the whole project.
 *
 * @author Panagiotis karapas
 * @version 2.3
 */
public class Util {
    private static boolean useColor = true;
    public static final Scanner SCAN = new Scanner(System.in);//Because i don't like having multiple input streams open!

    /**
     * Dealls all the cards on the starting pile (in position 0).
     * This will not deal any stack other than the 0th stack!
     * @param inputBoard The board to be expanded.
     */
    public static void dealAll(ArrayList<Stack> inputBoard) {
        int size = inputBoard.get(0).getCards().size();
        for (int i = 0; i < size; i++) {
            Stack s = new Stack();
            s.add(inputBoard.get(0).pop());
            inputBoard.add(1, s);
        }
    }

    /**
     * Returns the input text surrounded by the appropriete ANSI codes to have it be the colour.
     * If colour is not supported by the users console, then the text is returned as is.
     * @param text The text to be coloured.
     * @param colour The ANSIColour to be used as the paint. If ANSIColour.RESET is used, the returned text will be the default colour (usually black).
     * @return The coloured text, if colour is supported. The input text otherwise.
     */
    public static String getColoured(String text, ANSIColour colour){//an enum for ANSIColour was used so an external programmer can't feed anything to the  function.
        if(useColor){
            return colour.getCode() + text + ANSIColour.RESET.getCode();
        }
        return text;


    }

    /**
     * Asks the user an yes or now question.
     * @param question The question to ask the user.
     * @return True if they answer yes, false otherwise.
     */
    public static boolean yesNoQuestion(String question){
        while(true) {
            System.out.println(question);
            String answer = Util.SCAN.nextLine().toUpperCase();
            if (answer.equals("N")) {
                return false;
            } else if (answer.equals("Y")) {
                return true;
            } else {
                System.out.println("Please use (Y/N) only!");
            }
        }
    }

    /**
     * Prints the request and asks the user for an integer input. It will keep asking until the input is a number and in bounds.
     * @param request The message to be printed.
     * @param max the max value (exclusive)
     * @param min the min value (exclusive)
     * @return the number read
     */
    public static int readInteger(String request, int max, int min) {
        while (true){
            System.out.println(request);
            if (!Util.SCAN.hasNextInt()) {
                Util.printErrorln("Please input numbers only!");
                Util.SCAN.nextLine();
            } else {
                int answer = Util.SCAN.nextInt();
                Util.SCAN.nextLine();
                if (answer <= min) {
                    Util.printErrorln("Please input numbers over " + min + "!");
                } else if (answer >= max) {
                    Util.printErrorln("Please input numbers under " + max +"!");
                } else {
                    return answer;
                }
            }
        }
    }

    /**
     * Prints the request and asks the user for an integer input. It will keep asking until the input is a number.
     * @param request The message to be printed.
     * @return the number read.
     */
    public static int readInteger(String request) {
        return readInteger(request, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }
        /**
         * Sets the value of useColor to the newValue.
         * @param newValue The value to be used.
         */
    public static void setUseColor(boolean newValue){
        useColor = newValue;
    }

    /**
     * Gets the value of the useColor value.
     * @return the value of useColor.
     */
    public static boolean getUseColor(){
        return useColor;
    }

    /**
     * Prints an error message and then inserts a new line character.
     * At the end it fluches the System.err buffer, so to gurantee that the output of this and System.out will not be tangled.
     * @param error The error message to print.
     */
    public static void printErrorln(String error){
        printError(error + "\n");
    }
    /**
     * Prints an error message.
     * At the end it fluches the System.err buffer, so to gurantee that the output of this and System.out will not be tangled.
     * @param error The error message to print.
     */
    public static void printError(String error){
        System.out.flush();
        System.err.print(error);
        System.err.flush();
    }

}
