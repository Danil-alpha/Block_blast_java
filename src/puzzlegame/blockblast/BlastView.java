package puzzlegame.blockblast;

import puzzlegame.core.ScoreEngine;
import puzzlegame.core.Shape;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BlastView extends JPanel {
    private final BlastLogic logic;
    private final ScoreEngine scoreEngine;

    public final List<int[][]> availableMatrices = new ArrayList<>();
    public final List<Color> availableColors = new ArrayList<>();

    public int dragX, dragY;
    public int draggedShapeIndex = -1;
    public boolean isGameOver = false;

    public final int TILE_SIZE = 38;

    private int shakeX = 0;
    private int shakeY = 0;

    public BlastView(BlastLogic logic, ScoreEngine scoreEngine) {
        this.logic = logic;
        this.scoreEngine = scoreEngine;

        setPreferredSize(new Dimension(360, 580));
        setBackground(new Color(20, 21, 26));

        replenishShapes();
    }

    public void replenishShapes() {
        boolean allNull = true;
        for (int[][] m : availableMatrices) {
            if (m != null) { allNull = false; break; }
        }

        if (availableMatrices.isEmpty() || allNull) {
            availableMatrices.clear();
            availableColors.clear();
            for (int i = 0; i < 3; i++) {
                Shape base = logic.getBaseShape();
                availableMatrices.add(logic.generateSmartShapeMatrix(base));
                availableColors.add(base.color);
            }
        }
        checkGameOver();
    }

    public void checkGameOver() {
        List<int[][]> realMatrices = new ArrayList<>();
        for (int[][] m : availableMatrices) if (m != null) realMatrices.add(m);

        if (logic.isGameOver(realMatrices)) {
            isGameOver = true;
        }
    }

    public void triggerScreenShake() {
        new Thread(() -> {
            int duration = 200;
            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < duration) {
                shakeX = (int) (Math.random() * 11) - 5;
                shakeY = (int) (Math.random() * 11) - 5;
                repaint();
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            shakeX = 0;
            shakeY = 0;
            repaint();
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g2.drawString("" + scoreEngine.getCurrentScore(), 25, 40);

        g2.setColor(new Color(110, 115, 130));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.drawString("👑 РЕКОРД: " + scoreEngine.getHighScore(), 25, 62);

        g2.translate(25 + shakeX, 90 + shakeY);

        Color[][] board = logic.getBoard();
        for (int r = 0; r < logic.getGridSize(); r++) {
            for (int c = 0; c < logic.getGridSize(); c++) {
                if (board[r][c] != null) {
                    g2.setColor(board[r][c]);
                    g2.fillRoundRect(c * TILE_SIZE + 2, r * TILE_SIZE + 2, TILE_SIZE - 4, TILE_SIZE - 4, 8, 8);
                } else {
                    g2.setColor(new Color(33, 35, 44));
                    g2.fillRoundRect(c * TILE_SIZE + 2, r * TILE_SIZE + 2, TILE_SIZE - 4, TILE_SIZE - 4, 8, 8);
                }
            }
        }

        int dockY = 330;
        g2.setColor(new Color(26, 27, 35));
        g2.fillRoundRect(0, dockY, 310, 120, 16, 16);

        for (int i = 0; i < availableMatrices.size(); i++) {
            int[][] matrix = availableMatrices.get(i);
            if (matrix == null || i == draggedShapeIndex) continue;

            g2.setColor(availableColors.get(i));
            int startX = i * 100 + 15;
            int startY = dockY + 25;

            for (int r = 0; r < matrix.length; r++) {
                for (int c = 0; c < matrix[r].length; c++) {
                    if (matrix[r][c] == 1) {
                        g2.fillRoundRect(startX + c * 18, startY + r * 18, 15, 15, 4, 4);
                    }
                }
            }
        }

        g2.translate(-25 - shakeX, -90 - shakeY);

        if (draggedShapeIndex != -1) {
            int[][] matrix = availableMatrices.get(draggedShapeIndex);
            g2.setColor(availableColors.get(draggedShapeIndex));
            for (int r = 0; r < matrix.length; r++) {
                for (int c = 0; c < matrix[r].length; c++) {
                    if (matrix[r][c] == 1) {
                        g2.fillRoundRect(dragX + c * TILE_SIZE + 2, dragY + r * TILE_SIZE + 2, TILE_SIZE - 4, TILE_SIZE - 4, 8, 8);
                    }
                }
            }
        }

        if (isGameOver) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(Color.RED);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
            g2.drawString("ИГРА ОКОНЧЕНА", getWidth() / 2 - 120, getHeight() / 2 - 20);
        }
    }
}
