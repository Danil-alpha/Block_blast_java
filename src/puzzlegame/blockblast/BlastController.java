package puzzlegame.blockblast;

import puzzlegame.core.ScoreEngine;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BlastController extends MouseAdapter {
    private final BlastLogic logic;
    private final BlastView view;
    private final ScoreEngine scoreEngine;

    private int dragOffsetX, dragOffsetY;

    public BlastController(BlastLogic logic, BlastView view, ScoreEngine scoreEngine) {
        this.logic = logic;
        this.view = view;
        this.scoreEngine = scoreEngine;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (view.isGameOver) return;

        int mx = e.getX();
        int my = e.getY();

        int dockY = 330 + 90;
        int dockXStart = 25;

        for (int i = 0; i < view.availableMatrices.size(); i++) {
            int[][] matrix = view.availableMatrices.get(i);
            if (matrix == null) continue;

            int slotX = dockXStart + i * 100 + 15;
            int slotY = dockY + 25;

            if (mx >= slotX && mx <= slotX + 70 && my >= slotY && my <= slotY + 70) {
                view.draggedShapeIndex = i;

                view.dragX = mx - (matrix.length * view.TILE_SIZE) / 2;
                view.dragY = my - (matrix.length * view.TILE_SIZE) / 2;

                dragOffsetX = mx - view.dragX;
                dragOffsetY = my - view.dragY;
                view.repaint();
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (view.draggedShapeIndex == -1 || view.isGameOver) return;

        view.dragX = e.getX() - dragOffsetX;
        view.dragY = e.getY() - dragOffsetY;
        view.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (view.draggedShapeIndex == -1 || view.isGameOver) return;

        int[][] matrix = view.availableMatrices.get(view.draggedShapeIndex);

        int gridX = (int) Math.round((double)(view.dragX - 25) / view.TILE_SIZE);
        int gridY = (int) Math.round((double)(view.dragY - 90) / view.TILE_SIZE);

        if (logic.canPlaceShape(matrix, gridX, gridY)) {
            logic.placeShape(matrix, view.availableColors.get(view.draggedShapeIndex), gridX, gridY);

            int blocks = 0;
            for (int[] row : matrix) {
                for (int cell : row) if (cell == 1) blocks++;
            }
            scoreEngine.addPointsForPlacement(blocks);

            int clearedLines = logic.clearLines();
            scoreEngine.addPointsForLines(clearedLines);

            if (clearedLines > 0) {
                view.triggerScreenShake();
            }

            view.availableMatrices.set(view.draggedShapeIndex, null);
            view.replenishShapes();
        }

        view.draggedShapeIndex = -1;
        view.repaint();
    }
}
