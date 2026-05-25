package puzzlegame.core;

public class ScoreEngine {
    private int currentScore = 0;
    private int highScore = 0;

    public void addPointsForPlacement(int blockCount) {
        currentScore += blockCount;
        updateHighScore();
    }

    public void addPointsForLines(int linesCount) {
        if (linesCount <= 0) return;
        currentScore += (linesCount * (linesCount + 1)) * 5;
        updateHighScore();
    }

    private void updateHighScore() {
        if (currentScore > highScore) {
            highScore = currentScore;
        }
    }

    public void reset() { currentScore = 0; }
    public int getCurrentScore() { return currentScore; }
    public int getHighScore() { return highScore; }
}
