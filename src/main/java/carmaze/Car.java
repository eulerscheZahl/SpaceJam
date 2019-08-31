package carmaze;

import carmaze.view.BoardView;
import carmaze.view.CarView;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.util.List;

public class Car {
    private int id;
    private Cell position;
    private CarView view;

    public Car(int id, Cell cell) {
        this.id = id;
        position = cell;
    }

    public void move(String direction, List<Car> cars) throws Exception {
        Platform oldPlatform = position.getPlatform();
        if (oldPlatform != null) oldPlatform.setCar(null);

        Cell next = position.getNeighbor(direction);
        boolean crash = false;
        for (Car car : cars) {
            if (car.getPosition() == next) crash = true;
        }
        position = next;

        Platform newPlatform = position.getPlatform();
        if (newPlatform != null) newPlatform.setCar(this);

        view.update(true, crash);
        if (crash) throw new Exception("Car " + getId() + " crashed into another car");
    }

    public void updatePlatformPostion(Platform platform) {
        position = platform.getPosition();
        view.update(false, false);
    }

    public int getId() {
        return id;
    }

    public Cell getPosition() {
        return position;
    }

    public void createView(GraphicEntityModule graphics, TooltipModule tooltips, BoardView boardView) {
        view = new CarView(this, graphics, tooltips, boardView);
    }

    public String getInput() {
        return id + " " + position.getInput() + " CAR";
    }
}
