/**
 * View.java
 * John Sherer, Nov 12, 2018
 *
 */
package snake;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ScoreView extends Label {

    /**
     * Initialize the object
     */
    public ScoreView() {
        this.setTextFill(Color.WHITE);
    }

    /**
     * Check for score updates
     */
    public void update(SnakeModel model){
        String baseText;
        if(!model.isGameStarted()) {
            baseText = "Use the arrow keys or WASD to move. Press the spacebar to begin!";
        }else if(model.isGameOver()) {
            if(model.getScore() == model.getHighScore()) {
                baseText = "High score! Press the spacebar to start a new game";
            }else {
                baseText = "Game over. Press the spacebar to start a new game";
            }
        }else {
            baseText = "Current length: " + model.getScore();
        }

        this.setText(baseText + "\nHigh Score: " + model.getHighScore());
    }
}
