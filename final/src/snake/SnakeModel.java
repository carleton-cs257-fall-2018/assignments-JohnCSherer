/**
 * SnakeModel.java
 * John Sherer, 2018
 *
 * A model of the world the snake lives in
 */


package snake;


import java.util.Random;

public class SnakeModel {
    //within the grid, 0=empty, a positive integer represents part of the snakes body, and -1 is an apple
    private int[][] grid;

    private int snakeHeadRow;
    private int snakeHeadColumn;
    private String direction;

    private boolean actionQueued = false;
    private String queuedDirection = "";
    private boolean inputReceived = false;

    private boolean gameStarted = false;
    private boolean gameOver = false;
    private int snakeLength = 5;

    private int highScore = 0;

    /**
     * Initialize the object using the FXML parameters
     * @param rowCount number of rows
     * @param columnCount number of columns
     */
    public SnakeModel(int rowCount, int columnCount) {
        assert rowCount > 0 && columnCount > 0;
        this.grid = new int[rowCount][columnCount];
        this.startNewGame();
    }

    /**
     * Reset all the game parameters to their starting conditions, including the snake position and orientation
     */
    public void startNewGame() {
        this.gameOver = false;
        this.snakeLength = 5;
        this.snakeHeadRow = 2;
        this.snakeHeadColumn = 2;
        this.direction = "right";
        this.initializeLevel();
    }

    /**
     * Clears the grid and populates it with a single apple
     */
    private void initializeLevel() {
        int rowCount = this.grid.length;
        int columnCount = this.grid[0].length;

        //Clear the map and spawn the starting apple
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                this.grid[row][column] = 0;
            }
        }
        this.grid[5][5] = -1;
    }

    /**
     * Called whenever a frame ticks. Tries to advance to the next state of the game, unless
     * the game is over or hasn't started yet.
     * @param v reference to the main view
     * @param s reference to the score view
     */
    public void getTick(View v, ScoreView s){
        if(!gameOver && gameStarted){
            advanceState(v,s);
        }
    }

    /**
     * Advance the state of the world. This causes the snake head to move in the intended direction
     * if possible, update each segment of the snake, and potentially eat an apple (which halts
     * the aging process for one frame) and spawn a new one.
     * @param v reference to the main view
     * @param s reference to the score view
     */
    public void advanceState(View v, ScoreView s) {
        inputReceived = false;
        this.moveSnake(direction);
        if(isColliding()){
            gameOver = true;
            if(snakeLength > highScore){
                highScore = snakeLength;
            }
            v.update(this);
            s.update(this);
            return;
        }

        if(this.eatsApple()){
            snakeLength++;
        }else{
            this.ageSnake();
        }
        v.update(this);
        s.update(this);
    }

    /**
     * Apply the given input from the controller, unless 1) the input is in
     * the exact opposite direction of the snake's current motion or 2) we
     * have already received a valid input for this frame
     *
     * If we have already received an input, then extra command will be stored
     * and automatically called on the next frame, if it is still valid
     * @param inp the command passed by the controller
     */
    public void receiveInput(String inp){
        if(inp.equals("space")){
            if(!gameStarted){
                gameStarted = true;
            }else if(gameOver){
                gameOver = false;
                startNewGame();
            }
        }else {
            if (inputReceived) {
                if (!actionQueued) {
                    queuedDirection = inp;
                    actionQueued = true;
                }
            } else if (
                    !((direction.equals("up") && inp.equals("down")) ||
                            (direction.equals("down") && inp.equals("up")) ||
                            (direction.equals("left") && inp.equals("right")) ||
                            (direction.equals("right") && inp.equals("left")))) {
                direction = inp;
                inputReceived = true;
            }
        }
    }

    /**
     * Move the snake in the current direction. If no inputs were
     * detected this frame, then we check to see if there was one
     * queued up in the previous frame, and if so, we use it.
     * @param direction
     */
    public void moveSnake(String direction) {
        if(!inputReceived&&actionQueued) {
            receiveInput(queuedDirection);
            inputReceived = false;
            actionQueued = false;
        }
        if(direction.equals("up")){
            snakeHeadRow --;
        }else if(direction.equals("down")){
            snakeHeadRow ++;
        }else if(direction.equals("left")){
            snakeHeadColumn --;
        }else if(direction.equals("right")){
            snakeHeadColumn ++;
        }
    }

    /**
     * Checks to see if the snake's head has landed on an apple tile, and updates
     * that tile.
     * @return If an apple was eaten
     */
    private boolean eatsApple(){
        boolean eaten = false;
        if(grid[snakeHeadRow][snakeHeadColumn] == -1){
            eaten = true;
            spawnNewApple();
        }
        grid[snakeHeadRow][snakeHeadColumn] = snakeLength;
        return eaten;
    }

    /**
     * Creates a new apple on a white (0) tile, if possible
     *
     * Each white tile in the grid should have an equal chance to spawn an apple. To
     * do this, each row is given a weight according to how many white tiles it has,
     * and then a row is selected randomly but influenced by this weight. Then, a random
     * white tile within that row is selected.
     */
    private void spawnNewApple(){
        Random r = new Random();
        int[] rowWeights = new int[grid.length];
        int rowWeightsSum = 0;
        for(int row = 0; row < grid.length; row++){
            for(int col = 0; col < grid[0].length; col++){
                if(grid[row][col] == 0){
                    rowWeights[row] ++;
                    rowWeightsSum ++;
                }
            }
        }
        int randomInt = r.nextInt(rowWeightsSum + 1);
        int selectedRow = 0;
        while(randomInt > rowWeights[selectedRow]){
            randomInt -= rowWeights[selectedRow];
            selectedRow++;
        }

        randomInt = r.nextInt(rowWeights[selectedRow]);
        for(int c = 0; c < grid[0].length; c++) {
            if(grid[selectedRow][c] == 0){
                randomInt--;
                if(randomInt==-1){
                    grid[selectedRow][c] = -1;
                }
            }
        }
    }

    /**
     * Reduce the lifespan of each segment of snake, and remove the tail segment to
     * simulate the snake moving forward
     */
    public void ageSnake(){
        for(int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if(grid[r][c] > 0){
                    grid[r][c] --;
                }
            }
        }
    }

    /**
     * See if the snake is overlapping part of its body or going off the edge of the
     * screen. If so, game over. Segments with an age of 1 do not count since they will
     * expire before the tick is over.
     */
    private boolean isColliding(){
        if(snakeHeadRow >= 0 && snakeHeadRow < grid.length && snakeHeadColumn >= 0 && snakeHeadColumn < grid[0].length){
            return (grid[snakeHeadRow][snakeHeadColumn] > 1);
        }
        return true;
    }

    public int getSnakeHeadColumn() {
        return snakeHeadColumn;
    }

    public int getSnakeHeadRow(){
        return snakeHeadRow;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public boolean isGameStarted(){
        return gameStarted;
    }

    public int getScore() {
        return this.snakeLength;
    }

    public int getHighScore(){
        return highScore;
    }

    public int getGridValue(int row, int column){
        return this.grid[row][column];
    }
}

