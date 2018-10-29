package pak27.autoplay;

import pak27.playingCards.Card;
import pak27.playingCards.Stack;

import java.util.ArrayList;

import static pak27.util.Util.dealAll;

/**
 * AutoPlayer's solve method takes an board as input and brute-forces a way to reduce it to "maxNumberOfStacks" , returning instructions
 */
public class AutoPlayer {
    //it's made to return instructions and not just apply the moves, because i wanted it to be able to be used in cases like stepping through it etc.

    /**
     * @param inputBoard        The board to solve.
     * @param maxNumberOfStacks The amount of stacks to reduce the board to!
     * @return the moves needed to made to get the requested number of stacks!
     */
    public static int[][] solve(ArrayList<Stack> inputBoard, int maxNumberOfStacks) {
        //Copying the input so we don't change the actual input board
        if(maxNumberOfStacks <= 0) return null;
        ArrayList<Stack> board = new ArrayList<>();
        for (Stack s : inputBoard) {
            board.add(s.copy(false));//since stacks are mutable, false because i won't render the cards so their images are irrelevant, this saves time
        }
        dealAll(board);//deals all the cards
        if(maxNumberOfStacks > board.size()) return null;
        ArrayList<Card> topCards = new ArrayList<>();
        for (int i = 1; i < board.size(); i++) {
            topCards.add(board.get(i).lookAtTopMost());
        }
        int[][] result = allNextMoves(topCards, new ArrayList<>(), new int[0][], topCards.size(), maxNumberOfStacks);
        return topCards.size() - result.length <= maxNumberOfStacks ? result : null;
    }


    private static int[][] allNextMoves(ArrayList<Card> board, ArrayList<int[]> prevMoves, int[][] bestMoves, int initialSize, int maxNumberOfStacks) {

        for (int i = 0; i < board.size(); i++) {
            ArrayList<Card> temp = new ArrayList<>(board);

            if (move(i, i + 1, temp)) {
                ArrayList<int[]> newMoves = new ArrayList<>(prevMoves);
                newMoves.add(new int[]{i, i + 1});
                if (initialSize - newMoves.size() <= maxNumberOfStacks) {
                    return newMoves.toArray(new int[newMoves.size()][]);
                }
                int[][] result = allNextMoves(temp, newMoves, bestMoves, initialSize, maxNumberOfStacks);
                if (result != null && result.length > bestMoves.length) {
                    if (initialSize - result.length <= maxNumberOfStacks) {
                        return result;
                    }
                    bestMoves = result;
                }
            }
            temp = board;

            if (move(i, i + 3, temp)) {
                ArrayList<int[]> newMoves = new ArrayList<>(prevMoves);
                newMoves.add(new int[]{i, i + 3});
                if (initialSize - newMoves.size() <= maxNumberOfStacks) {
                    return newMoves.toArray(new int[newMoves.size()][]);
                }
                int[][] result = allNextMoves(temp, newMoves, bestMoves, initialSize, maxNumberOfStacks);
                if (result != null && result.length > bestMoves.length) {
                    if (initialSize - result.length <= maxNumberOfStacks) {
                        return result;
                    }
                    bestMoves = result;
                }
            }
        }
        return prevMoves.toArray(new int[prevMoves.size()][]);
    }

    private static boolean move(int from, int to, ArrayList<Card> gameBoard) {
        if (from >= gameBoard.size() || to >= gameBoard.size() || from < 0 || to < 0) {
            return false;
        }
        if (Math.abs(to - from) > 3) {//taking the absolute value since its bidirectional
            return false;
        }
        if (!gameBoard.get(to).sameValOrSuit(gameBoard.get(from))) {
            return false;
        }
        gameBoard.set(to, gameBoard.get(from));
        gameBoard.remove(from);
        return true;
    }

}

