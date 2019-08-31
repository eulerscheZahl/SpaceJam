package carmaze;

import carmaze.view.BoardView;
import carmaze.view.PlatformView;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class Platform {
    private PlatformView view;
    private Cell position;
    private int id;
    private String directions;
    private Car car;

    public Platform(int id, Cell cell, String directions) {
        position = cell;
        this.id = id;
        this.directions = directions;
        position.setPlatform(this);
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Cell getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public void incrementId(int amount) {
        id += amount;
    }

    public void createView(GraphicEntityModule graphics, TooltipModule tooltips, BoardView boardView) {
        view = new PlatformView(this, graphics, tooltips, boardView);
    }

    public void move(String direction, boolean needsCar) throws Exception {
        // check if platform can move in given direction
        if (!directions.contains(direction)) {
            throw new Exception("Can't move platform " + id + " in direction " + direction);
        }
        if (needsCar && car == null) {
            throw new Exception("Can't move platform " + id + " without a car on it");
        }
        position.setPlatform(null);

        // move in given direction as long as possible
        Cell previousPosition = position;
        while (!position.isOutOfMap()) {
            Cell next = position.getNeighbor(direction);
            if (next.isGround() || next.getPlatform() != null) break;
            position = next;
        }
        if (position == previousPosition) throw new Exception("The platform can't move in that direction, as there is an obstacle");

        if (position != null) position.setPlatform(this);
        if (car != null) car.updatePlatformPostion(this);

        view.update();
    }

    public String getDirections() {
        return directions;
    }

    public String getInput() {
        return id + " " + position.getInput() + " " + directions;
    }
}
