package puzzlegame.core;

import java.awt.*;

public enum Shape {
    I(new int[][]{
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    }),
    O(new int[][]{
            {1, 1},
            {1, 1}
    }),
    T(new int[][]{
            {0, 1, 0},
            {1, 1, 1},
            {0, 0, 0}
    }),
    L(new int[][]{
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
    }),
    J(new int[][]{
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
    }),
    S(new int[][]{
            {0, 1, 1},
            {1, 1, 0},
            {0, 0, 0}
    }),
    Z(new int[][]{
            {1, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
    }),
    DOT(new int[][]{
            {1}
    }),
    BIG_O(new int[][]{
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    }),
    CORNER(new int[][]{
            {1, 0},
            {1, 1}
    });

    public final int[][] coords;
    public Color color;

    Shape(int[][] coords) {
        this.coords = coords;
        this.color = Color.WHITE;
    }

    private static Color generateRandomColor() {
        int r = 100 + (int) (Math.random() * 156);
        int g = 100 + (int) (Math.random() * 156);
        int b = 100 + (int) (Math.random() * 156);
        return new Color(r, g, b);
    }

    public static Shape getRandomTetrisShape() {
        int index = (int) (Math.random() * 7);
        Shape shape = values()[index];
        shape.color = generateRandomColor();
        return shape;
    }

    public static Shape getRandomBlastShape() {
        int index = (int) (Math.random() * values().length);
        Shape shape = values()[index];
        shape.color = generateRandomColor();
        return shape;
    }
}
