package carmaze;

public class Cell {
    private int x;
    private int y;
    private char cellChar;
    private boolean ground;
    private int targetId = -1;
    private boolean outOfMap;
    private Cell[] neighbors = new Cell[4];
    private Platform platform;

    public Cell(int x, int y, char c) {
        cellChar = c;
        this.x = x;
        this.y = y;
        ground = c != '#';
        if (c >= '0' && c <= '9') targetId = c-'0';
    }

    public void initNeighbors(Board board) {
        for (int dir = 0; dir < 4; dir++) {
            int x = this.x + Direction.dx[dir];
            int y = this.y + Direction.dy[dir];
            neighbors[dir] = board.getCell(x,y);
        }
    }

    public boolean isGround() {
        return ground;
    }

    public int getTargetId() {
        return targetId;
    }

    public boolean isOutOfMap() {
        return outOfMap;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Cell getNeighbor(String direction) {
        int dirIndex = Direction.getDirectionIndex(direction);
        Cell result = neighbors[dirIndex];
        if (result == null) {
            result = new Cell(x + 2 * Direction.dx[dirIndex], y + 2 * Direction.dy[dirIndex], '#');
            result.outOfMap = true;
        }
        return result;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getCellChar() {
        return cellChar;
    }

    public String getInput() {
        return x + " " + y;
    }
}
