package carmaze;

import carmaze.view.BoardView;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int width;
    private int height;
    private List<Car> cars = new ArrayList<>();
    private List<Platform> platforms = new ArrayList<>();
    private Cell[][] grid;
    private BoardView view;

    public Board(String input, GraphicEntityModule graphics, TooltipModule tooltips) {
        String[] lines = input.split(";");
        width = lines[0].length();
        height = lines.length;
        grid = new Cell[width][height];
        for (int y = 0; y < height; y++) {
            String line = lines[y];
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x);
                char mapChar = '#';
                if (c >= '0' && c <= '9') mapChar = c;
                if (c >= 'A' && c <= 'Z' || c == '.') mapChar = '.';
                grid[x][y] = new Cell(x, y, mapChar);
                if (c >= 'A' && c <= 'Z') cars.add(new Car(c - 'A', grid[x][y]));
                if (c == '>') platforms.add(new Platform(platforms.size(), grid[x][y], "R"));
                if (c == '<') platforms.add(new Platform(platforms.size(), grid[x][y], "L"));
                if (c == '^') platforms.add(new Platform(platforms.size(), grid[x][y], "U"));
                if (c == 'v') platforms.add(new Platform(platforms.size(), grid[x][y], "D"));
                if (c == '-') platforms.add(new Platform(platforms.size(), grid[x][y], "RL"));
                if (c == '|') platforms.add(new Platform(platforms.size(), grid[x][y], "UD"));
                if (c == '+') platforms.add(new Platform(platforms.size(), grid[x][y], "UDRL"));
            }
        }
        for (Platform platform : platforms) platform.incrementId(cars.size());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].initNeighbors(this);
            }
        }

        view = new BoardView(this, graphics, tooltips);
        for (Platform platform : platforms) platform.createView(graphics, tooltips, view);
        for (Car car : cars) car.createView(graphics, tooltips, view);
    }

    public boolean lose() {
        for (Car car : cars) {
            Cell cell = car.getPosition();
            if (cell == null || cell.isOutOfMap() || (!cell.isGround() && cell.getPlatform() == null)) return true;
        }
        return  false;
    }

    public boolean win() {
        for (Car car : cars) {
            if (car.getPosition().getTargetId() != car.getId()) return false;
        }
        return true;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BoardView getView() {
        return view;
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return null;
        return grid[x][y];
    }

    public ArrayList<String> getInput(boolean initial) {
        ArrayList<String> result = new ArrayList<>();
        if (initial) {
            result.add(width + " " + height);
            for (int y = 0; y < height; y++) {
                String line = "";
                for (int x = 0; x < width; x++) {
                    line += getCell(x,y).getCellChar();
                }
                result.add(line);
            }
        }

        result.add(String.valueOf(cars.size() + platforms.size()));
        for (Car car : cars) result.add(car.getInput());
        for (Platform platform : platforms) result.add(platform.getInput());

        return result;
    }

    public void applyAction(int id, String direction) throws Exception {
        for (Car car : cars) {
            if (id == car.getId()) {
                car.move(direction, cars);
                return;
            }
        }

        for (Platform platform : platforms) {
            if (platform.getId() == id){
                platform.move(direction, cars.size() > 1);
                if (platform.getPosition().isOutOfMap()) platforms.remove(platform);
                return;
            }
        }
        throw new Exception("ID not found: " + id);
    }
}
