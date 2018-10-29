package uk.ac.aber.dcs.cs12320.cards.gui.javafx;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pak27.playingCards.Stack;

import java.util.ArrayList;

/**
 * Displays the card images on a table (the Javafx stage)
 *
 * @author Chris Loftus, Lynda Thomas and Panagiotis Karapas
 * @version 2.0
 */
public class CardTable {
    private final Stage stage;
    private boolean startingPileEmpty;

    private final Image CARD_BACK = new Image(getClass().getClassLoader().getResource("cards/B.png").toString(), false);//have it loaded since loading it every time doesn't make sense
    private final float PERCENT_SHOWN = 0.15f;

    /**
     * The constractor.
     * @param stage The stage used to render the cards.
     * @param title The title shown at the top of the window.
     */
    public CardTable(Stage stage, String title) {
        this.stage = stage;
        stage.setTitle(title);
    }

    /**
     * Gets the value of the boolean startingPileEmpty.
     * @return the value of the boolean startingPileEmpty.
     */
    public boolean isStartingPileEmpty() {
        return startingPileEmpty;
    }

    /**
     * Called when the starting pile is empty.
     * Sets the value of
     */
    public void allDone() {
        startingPileEmpty = true;
    }

    /**
     * Displays all the face-up cards (just the top showing cards)
     * and if the game is not over then also displays the face-down deck.
     * @param board The board to display.
     */
    public void cardDisplay(ArrayList<Stack> board) {

        // We need to do this within the GUI thread. We assume
        // that the method is called by a non-GUI thread
        Platform.runLater(() -> {
            HBox box = new HBox();
            if (board.size() >= 2) {//since if == 1 then it's just the stack
                for (int i = board.size() - 1; i > 0; i--) {
                    Stack s = board.get(i);
                    WritableImage img = new WritableImage((int) CARD_BACK.getWidth(), (int) CARD_BACK.getHeight() + (int) (Math.ceil((s.getCards().size() - 1) * CARD_BACK.getHeight() * PERCENT_SHOWN)));
                    for (int j = 0; j < s.getCards().size(); j++) {
                        img = drawImage(img, s.getCards().get(j).getImg(), j, j == s.getCards().size() - 1);
                    }
                    drawCards(box, img);
                }

            }

            if (!startingPileEmpty) {
                // Draws the face-down top card of our pack of cards
                drawCards(box, CARD_BACK);
            }


            ScrollPane sp = new ScrollPane();



            Scene scene = new Scene(sp, 1200, 600);
            sp.setContent(box);

            stage.setScene(scene);
            stage.show();
        });

    }

    private WritableImage drawImage(WritableImage writableImage, Image img, int cardNum, boolean isLast) {
        for (int y = 0; y < (isLast ? img.getHeight() : (img.getHeight() * PERCENT_SHOWN)); y++) {
            //i could replace this with a call of writableImage.getPixelWriter().setPixels, but my testing showed that this would make it ~3 times slower
            for (int x = 0; x < img.getWidth(); x++) {
                if (img.getPixelReader().getArgb(x, y) >> 24 != 0) {//if the pixel is transparent then don't overwrite it!
                    //the y is complicated since we have to render the current card under the ones before it
                    writableImage.getPixelWriter().setArgb(x, y + (int) ((cardNum * (img.getHeight() * PERCENT_SHOWN)) - (cardNum != 0 ? img.getHeight() * PERCENT_SHOWN * 0.2f : 0)), img.getPixelReader().getArgb(x, y));
                }
            }
        }
        return writableImage;
    }


    private void drawCards(HBox box, Image image) {
        ImageView iv;
        iv = new ImageView();
        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(100);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        box.getChildren().add(iv);
    }


}
