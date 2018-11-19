/**
 * Controller.java
 * John Sherer, 2018
 *
 * Receives and sends user input to the model, and tells the model when a frame has passed
 */
package snake;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.TimerTask;
import java.util.Timer;

public class Controller implements EventHandler<KeyEvent> {
    @FXML private View view;
    @FXML private ScoreView scoreView;
    private SnakeModel model;
    private Timer timer;
    private static int FRAMES_PER_SECOND = 4;

    public Controller() {
    }

    public void initialize() {
        this.model = new SnakeModel(this.view.getRowCount(), this.view.getColumnCount());
        this.view.update(this.model);
        this.scoreView.update(this.model);
        startTimer();
    }

    public void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        tick();
                    }
                });
            }
        };

        long frameTimeInMilliseconds = (long)(1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }

    public double getBoardWidth() {
        return View.CELL_WIDTH * this.view.getColumnCount();
    }

    public double getBoardHeight() {
        return View.CELL_WIDTH * this.view.getRowCount();
    }

    private void tick() {
        this.model.getTick(this.view, this.scoreView);
    }

    /**
     * Tracks relevant keypresses and sends them to the model as a string
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        String inp = "none";
        if (code == KeyCode.LEFT || code == KeyCode.A) {
            inp = "left";
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            inp = "right";
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            inp = "up";
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            inp = "down";
        } else if (code == KeyCode.SPACE){
            inp = "space";
        }
        if(!inp.equals("none")) {
            model.receiveInput(inp);
        }
    }
}
