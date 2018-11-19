/**
 * View.java
 * John Sherer, 12 Nov, 2018
 *
 */
package snake;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class View extends Group {
    public final static double CELL_WIDTH = 20.0;

    private int rowCount;
    private int columnCount;
    private Rectangle[][] display;

    public View() {
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }

    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.display = new Rectangle[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    Rectangle rectangle = new Rectangle();
                    rectangle.setX((double)column * CELL_WIDTH);
                    rectangle.setY((double)row * CELL_WIDTH);
                    rectangle.setWidth(CELL_WIDTH);
                    rectangle.setHeight(CELL_WIDTH);
                    rectangle.setFill(Color.GREEN);
                    this.display[row][column] = rectangle;
                    this.getChildren().add(rectangle);
                }
            }
        }
    }

    public void update(SnakeModel model) {
        //assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                int val = model.getGridValue(row, column);
                if(val == 0) {
                    this.display[row][column].setFill(Color.WHITE);
                }else if(val > 0){
                    this.display[row][column].setFill(Color.GREEN);
                }else{
                    this.display[row][column].setFill((Color.RED));
                }
            }
        }
        int r = model.getSnakeHeadRow();
        int c = model.getSnakeHeadColumn();
        if(r >= 0 && r < rowCount && c >= 0 && c < columnCount)
        this.display[r][c].setFill(Color.BLACK);
    }
}
