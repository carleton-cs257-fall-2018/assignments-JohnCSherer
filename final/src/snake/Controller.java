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
    @FXML private Label scoreLabel;
    @FXML private Label messageLabel;
    @FXML private View view;
    private SnakeModel model;
    private Timer timer;
    private static int FRAMES_PER_SECOND = 4;

    public Controller() {
    }

    public void initialize() {
        this.startTimer();
        this.model = new SnakeModel(this.view.getRowCount(), this.view.getColumnCount());
        this.update();
    }

    private void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        update();
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

    private void update() {
        this.model.advanceState();
        if(this.model.isGameOver()){

        }
        if(!model.isGameOver()) {
            this.view.update(this.model);
        }
    }

    /**
     * Tracks relevant keypresses and sends them to the model as a string
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
/*
        String s = code.getChar();
        if (s.length() > 0) {
            char theCharacterWeWant = s.charAt(0);
        }*/
        String inp = "none";
        if (code == KeyCode.LEFT || code == KeyCode.A) {
            inp = "left";
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            inp = "right";
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            inp = "up";
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            inp = "down";
        }
        if(!inp.equals("none")) {
            model.receiveInput(inp);
        }
    }
}
