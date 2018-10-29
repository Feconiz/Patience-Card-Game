package pak27.playingCards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pak27.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The deck is like a stack but can also be shuffled and cards can be added in places other than the top.
 */
public class Deck extends Stack {
    /**
     * Creates an empty deck by calling the contractor of pak27.playingCards.Stack.
     */
    public Deck() {
        super();
    }

    /**
     * Reads the cards from a json file.
     * If the file is not there the user is asked if they want a new file with that name to be generated.
     * @param file The file to read from.
     * @param numberOfDecks The number of decks to return.
     * @return True if the reading was successful, false otherwise.
     */
    public boolean readCards(int numberOfDecks, String file) {
        if(numberOfDecks <= 0){
            Util.printErrorln("numberOfDecks Must be at least 1!");
            return false;
        }
        try (FileReader fr = new FileReader(file)) {
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

            Gson gson = gsonBuilder.create();
            ArrayList<Card> read = gson.fromJson(fr,new TypeToken<ArrayList<Card>>(){}.getType());

            for(Card c : read){
                c.setImg();//since having the paths saved in json wouldn't be good, if the project was transferred to another location
            }

            for(int i = 0; i < numberOfDecks;i++) {
                for (Card c : read) {
                    add(c.copy(true));
                }
            }

            return true;
        } catch (IOException e) {
            Util.printErrorln("[ERROR] Failed to load cards! cards.json is missing!");
            Util.printErrorln("Do you want me to generate it for you?(Y/N)");
            String choice = Util.SCAN.nextLine().toUpperCase();
            while(!choice.equals("Y") && !choice.equals("N")) {
                Util.printErrorln("Use Y or N only!");
                choice = Util.SCAN.nextLine().toUpperCase();
            }
            if(choice.equals("Y")) {
                generateCardsFile(file);
                return readCards(numberOfDecks, file);
            }else {
                return false;
            }
        }
    }
    private static void generateCardsFile(String file){
        ArrayList<Card> list = new ArrayList<>();
        for (Value v : Value.allExceptJoker()) {
            for(Suit s : Suit.values()){
                list.add(new Card(s,v));
            }
        }
        try (PrintWriter outfile = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(list, outfile);
        } catch (IOException e) {
            Util.printErrorln("[ERROR] Failed to save!");
        }
    }

    /**
     * Shuffles the Deck.
     */
    public void shuffle() {
        Collections.shuffle(getCards());
    }

    //This is a shuffle method i wrote before learning about Collections.shuffle
    //    I had Radonm r definded
//   /**
//    * Implementation of Durstenfeld's version of Fisher–Yates shuffle.
//    **/
//    public void shuffle() {//i chose this algorithm since its quite efficient, and does not require extra space.
//        ArrayList<Card> cards = getCards();//makes it easier by not having to use getters and setters
//        for (int i = cards.size(); i > 0; i--) {
//            int index = r.nextInt(i);//get a random index between 0 (inclusive) and i (exclusive)
//            //swap the element in the "index"th place with the last un-shuffled element
//            Card selected = cards.get(index);
//            cards.set(index, cards.get(i - 1));
//            cards.set(i - 1, selected);
//        }
//        setCards(cards);
//    }


    /**
     * Provides a string containing all the relevant information for this Deck in a formatted way.
     * @return a string containing all the relevant information for this Deck in a formatted way.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int columns = (int) Math.ceil(Math.sqrt(getCards().size()));//i wanted to have a nice format for any deck
        int i = 0;
        for (Card c : getCards()) {
            sb.append(c.toString());
            sb.append(" ");
            if (++i >= columns) {
                sb.append("\n");
                i = 0;
            }
        }
        return sb.toString();
    }
}
