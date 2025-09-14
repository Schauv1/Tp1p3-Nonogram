package system;

public enum DifficultyLevel {
    EASY(5),
    MEDIUM(10),
    HARD(15),
    EXTREME(20);

    private int size;

    DifficultyLevel(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
