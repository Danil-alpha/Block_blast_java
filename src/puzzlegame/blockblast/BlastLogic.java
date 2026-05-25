package puzzlegame.blockblast;

import puzzlegame.core.Shape;
import java.awt.Color;
import java.util.List;

public class BlastLogic {
    private final int GRID_SIZE = 8;
    private final Color[][] board = new Color[GRID_SIZE][GRID_SIZE];

    public boolean canPlaceShape(int[][] coords, int startX, int startY) {
        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[row].length; col++) {
                if (coords[row][col] == 1) {
                    int targetX = startX + col;
                    int targetY = startY + row;

                    if (targetX < 0 || targetX >= GRID_SIZE || targetY < 0 || targetY >= GRID_SIZE) {
                        return false;
                    }
                    if (board[targetY][targetX] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void placeShape(int[][] coords, Color color, int startX, int startY) {
        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[row].length; col++) {
                if (coords[row][col] == 1) {
                    board[startY + row][startX + col] = color;
                }
            }
        }
    }

    public int clearLines() {
        boolean[] rowsToRemove = new boolean[GRID_SIZE];
        boolean[] colsToRemove = new boolean[GRID_SIZE];
        int clearedCount = 0;

        for (int row = 0; row < GRID_SIZE; row++) {
            boolean isFull = true;
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == null) { isFull = false; break; }
            }
            if (isFull) { rowsToRemove[row] = true; clearedCount++; }
        }

        for (int col = 0; col < GRID_SIZE; col++) {
            boolean isFull = true;
            for (int row = 0; row < GRID_SIZE; row++) {
                if (board[row][col] == null) { isFull = false; break; }
            }
            if (isFull) { colsToRemove[col] = true; clearedCount++; }
        }

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (rowsToRemove[row] || colsToRemove[col]) board[row][col] = null;
            }
        }
        return clearedCount;
    }

    public int[][] generateSmartShapeMatrix(Shape baseShape) {
        int[][] currentMatrix = baseShape.coords;
        int rotations = (int) (Math.random() * 4);
        for (int i = 0; i < rotations; i++) {
            currentMatrix = rotateMatrix(currentMatrix);
        }
        return currentMatrix;
    }

    private int[][] rotateMatrix(int[][] matrix) {
        int size = matrix.length;
        int[][] rotated = new int[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                rotated[c][size - 1 - r] = matrix[r][c];
            }
        }
        return rotated;
    }

    public Shape getBaseShape() {
        int filledCells = 0;
        for (Color[] row : board) {
            for (Color cell : row) if (cell != null) filledCells++;
        }

        if (filledCells > (GRID_SIZE * GRID_SIZE) / 2) {
            Shape[] easyShapes = {Shape.DOT, Shape.O, Shape.I};
            return easyShapes[(int) (Math.random() * easyShapes.length)];
        }
        return Shape.getRandomBlastShape();
    }

    public boolean isGameOver(List<int[][]> availableMatrices) {
        if (availableMatrices == null || availableMatrices.isEmpty()) return false;

        boolean hasAtLeastOneRealShape = false;
        for (int[][] matrix : availableMatrices) {
            if (matrix == null) continue;
            hasAtLeastOneRealShape = true;

            for (int row = -3; row < GRID_SIZE; row++) {
                for (int col = -3; col < GRID_SIZE; col++) {
                    if (canPlaceShape(matrix, col, row)) return false;
                }
            }
        }
        return hasAtLeastOneRealShape;
    }

    public Color[][] getBoard() { return board; }
    public int getGridSize() { return GRID_SIZE; }
}
