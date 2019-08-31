package carmaze;

public class Direction {
    public static final int[] dx = {0, 1, 0, -1};
    public static final int[] dy = {-1, 0, 1, 0};
    public static final String[] names = {"U", "R", "D", "L"};

    public static int getDirectionIndex(String direction) {
        for (int i = 0; i < 4; i++) {
            if (names[i].equals(direction)) return i;
        }
        return -1;
    }
}
