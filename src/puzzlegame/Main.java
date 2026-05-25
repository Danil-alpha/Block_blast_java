package puzzlegame;

import puzzlegame.core.ScoreEngine;
import puzzlegame.blockblast.BlastLogic;
import puzzlegame.blockblast.BlastView;
import puzzlegame.blockblast.BlastController;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Block Blast - Screen Shake Edition");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            ScoreEngine score = new ScoreEngine();
            BlastLogic logic = new BlastLogic();
            BlastView view = new BlastView(logic, score);
            BlastController controller = new BlastController(logic, view, score);

            view.addMouseListener(controller);
            view.addMouseMotionListener(controller);

            frame.add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
