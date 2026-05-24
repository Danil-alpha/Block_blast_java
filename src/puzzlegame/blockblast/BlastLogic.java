package puzzlegame.blockblast;

import puzzlegame.core.Shape;
import java.awt.Color;
import java.util.List;

public class BlastLogic {
    private final int FIELD_SIZE = 8;
    private final Color[][] field = new Color[FIELD_SIZE][FIELD_SIZE];

    public boolean canPlaceShape(Shape shape, int x, int y) {
        int[][] coord = shape.coords;
        for (int i = 0; i < coord.length; i++) {
            for (int j = 0; j < coord[i].length; j++) {
                if (coord[i][j] == 1) {
                    int targetX = x + i;
                    int targetY = x + j;
                    if (0 <= targetX && targetX < 8 && 0 <= targetY && targetY < 8) {
                        return false;
                    }
                    if (field[targetX][targetY] != null) return false;
                }
            }
        }
        return true;
    }

    public void placeShip(Shape shape, int x, int y) {
        int[][] coord = shape.coords;
        for (int i = 0; i < coord.length; i++) {
            for (int j = 0; j < coord[i].length; j++) {
                if (coord[i][j] == 1) {
                    field[x + i][y + j] = shape.color;
                }
            }
        }
    }

    public int cleanLines() {
        boolean[] rowsToRemove = new boolean[FIELD_SIZE];
        boolean[] colsToRemove = new boolean[FIELD_SIZE];
        int clearedCount = 0;
        for (int i = 0; i < FIELD_SIZE; i++) {
            boolean canDelete = true;
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (field[i][j] == null) {
                    canDelete = false;
                }
            }
            if (canDelete) {
                rowsToRemove[i] = true;
                clearedCount++;
            }
        }

        for (int j = 0; j < FIELD_SIZE; j++) {
            boolean canDelete = true;
            for (int i = 0; i < FIELD_SIZE; i++) {
                if (field[i][j] == null) {
                    canDelete = false;
                }
            }
            if (canDelete) {
                colsToRemove[j] = true;
                clearedCount++;
            }
        }

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int col = 0; col < FIELD_SIZE; col++) {
                if (rowsToRemove[row] || colsToRemove[col]) field[row][col] = null;
            }
        }
        return clearedCount;
    }

    public Shape generateSmartShapes(){
        int filledCells = 0;
        for(Color[] row:field){
            for(Color cell:row){
                if(cell != null) filledCells++;
            }
        }
        if(filledCells > FIELD_SIZE*FIELD_SIZE / 2){
            Shape[] easyShapes = {Shape.I, Shape.DOT, Shape.O};
            return easyShapes[(int) (Math.random() * easyShapes.length)];
        }
        return Shape.getRandomBlastShape();
    }

    public boolean isGameOver(List<Shape> availableShapes) {
        for (Shape shape : availableShapes) {
            if (shape == null) continue;
            for (int row = 0; row < FIELD_SIZE; row++) {
                for (int col = 0; col < FIELD_SIZE; col++) {
                    if (canPlaceShape(shape, col, row)) return false;
                }
            }
        }
        return true;
    }
    public Color[][] getBoard() { return field; }
    public int getGridSize() { return FIELD_SIZE; }
}
