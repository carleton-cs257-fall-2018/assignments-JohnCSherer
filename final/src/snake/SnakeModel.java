package snake;

/**
 * SnakeModel.java
 * John Sherer, 2018
 *
 * A model of the world the snake lives in
 */

import java.util.Random;

public class SnakeModel {
    //within the grid, 0=empty, a positive integer represents part of the snakes body, and -1 is an apple
    private int[][] grid;

    private int snakeHeadRow = 2;
    private int snakeHeadColumn = 2;
    private String direction = "right";

    private boolean actionQueued = false;
    private String queuedDirection = "";
    private boolean inputReceived = false;

    private boolean gameOver;
    private int snakeLength = 5;

    public SnakeModel(int rowCount, int columnCount) {
        assert rowCount > 0 && columnCount > 0;
        //this.cells = new CellValue[rowCount][columnCount];
        this.grid = new int[rowCount][columnCount];
        this.startNewGame();
    }

    public void startNewGame() {
        this.gameOver = false;
        this.snakeLength = 5;
        //this.level = 1;
        this.initializeLevel();
    }

    public boolean isLevelComplete() {
        return false;
    }

    private void initializeLevel() {
        int rowCount = this.grid.length;
        int columnCount = this.grid[0].length;

        // Empty all the cells
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                this.grid[row][column] = 0;
            }
        }
        this.grid[5][5] = -1;
    }

    /**
     * Advance the state of the world. This causes the snake head to move in the intended direction
     * if possible, update each segment of the snake, and potentially eat an apple (which halts
     * the aging process for one frame) and spawn a new one.
     */
    public void advanceState() {
        inputReceived = false;
        this.moveSnake(direction);
        if(gameOver) {

        }else if(this.eatsApple()){
            snakeLength++;
        }else{
            this.ageSnake();
        }
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
        if(inputReceived){
            if(!actionQueued){
                queuedDirection = inp;
                actionQueued = true;
            }
        }else if(
        !((direction.equals("up")&&inp.equals("down") ) ||
        (direction.equals("down")&&inp.equals("up") ) ||
        (direction.equals("left")&&inp.equals("right") ) ||
        (direction.equals("right")&&inp.equals("left") ))){
            direction = inp;
            inputReceived = true;
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
        if(snakeHeadRow < 0 || snakeHeadRow >= grid.length ||
           snakeHeadColumn < 0 || snakeHeadColumn >= grid[0].length) {
            gameOver = true;
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

    public int getRowCount() {
        return this.grid.length;
    }

    public int getColumnCount() {
        assert this.grid.length > 0;
        return this.grid[0].length;
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

    public int getScore() {
        return this.snakeLength;
    }

    public int getGridValue(int row, int column){
        return this.grid[row][column];
    }
}

/*
public class SnakeModel {

    private int score;
    private int hiscore;
    private int[][] map;
    private boolean gameOver;

    /**
     * Initialized the object and fetches the high score
     */
/*
    public SnakeModel(){}

    /**
     * Advances the snake and updates the map. Also checks to see if the snake has
     * collected a point or a game over has occured
     * @param direction the input provided by the Controller (1=up, 2=down, 3=left, 4=right, 0=straight)
     */
/*
    public void tick(int direction){}

    /*Various get methods*/
/*
    public int getScore(){
        return score;
    }

    public int getHiscore(){
        return hiscore;
    }
}
*/
