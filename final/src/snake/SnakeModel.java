package snake;

public class SnakeModel {

    private int score;
    private int hiscore;
    private int[][] map;
    private boolean gameOver;

    /**
     * Initialized the object and fetches the high score
     */
    public SnakeModel(){}

    /**
     * Advances the snake and updates the map. Also checks to see if the snake has
     * collected a point or a game over has occured
     * @param direction the input provided by the Controller (1=up, 2=down, 3=left, 4=right, 0=straight)
     */
    public void tick(int direction){}

    /*Various get methods*/
    public int getScore(){
        return score;
    }

    public int getHiscore(){
        return hiscore;
    }

    public boolean isGameOver(){
        return gameOver;
    }
}
