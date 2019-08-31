package carmaze.view;

import carmaze.Car;
import carmaze.Cell;
import carmaze.Direction;
import com.codingame.game.Referee;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.SpriteAnimation;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class CarView {
    private GraphicEntityModule graphics;
    private Cell previousLocation;
    private Car car;
    private Sprite sprite;
    private int rotateDirection = 0;
    private BoardView boardView;

    public CarView(Car car, GraphicEntityModule graphics, TooltipModule tooltips, BoardView view) {
        this.car = car;
        previousLocation = car.getPosition();
        this.graphics = graphics;
        this.boardView = view;
        sprite = graphics.createSprite().setImage("car" + car.getId() + ".png");
        sprite.setX(BoardView.TILE_SIZE * car.getPosition().getX() + BoardView.TILE_SIZE / 2);
        sprite.setY(BoardView.TILE_SIZE * car.getPosition().getY() + BoardView.TILE_SIZE / 2);
        sprite.setAnchor(0.5);
        view.getBoardGroup().add(sprite);
        tooltips.setTooltipText(sprite, "car\nid: " + car.getId());
    }

    public void update(boolean turnAround, boolean crash) {
        int moveTime = 600;
        int turnTime = 0;
        if (turnAround) {
            int dirX = (int) Math.signum(car.getPosition().getX() - previousLocation.getX());
            int dirY = (int) Math.signum(car.getPosition().getY() - previousLocation.getY());
            int direction = 0;
            while (dirX != Direction.dx[direction] || dirY != Direction.dy[direction]) direction++;
            sprite.setRotation(direction * Math.PI / 2);
            turnTime = (rotateDirection + 4 - direction) % 4;
            turnTime = Math.min(turnTime, 4 - turnTime);
            rotateDirection = direction;
            turnTime *= 200;
        }

        Referee.manager.setFrameDuration(turnTime + moveTime);
        graphics.commitEntityState((double)turnTime / (moveTime + turnTime), sprite);
        if (!car.getPosition().isGround() && car.getPosition().getPlatform() == null) {
            sprite.setX(BoardView.TILE_SIZE * (car.getPosition().getX() + previousLocation.getX()) / 2 + BoardView.TILE_SIZE / 2);
            sprite.setY(BoardView.TILE_SIZE * (car.getPosition().getY() + previousLocation.getY()) / 2 + BoardView.TILE_SIZE / 2);
            graphics.commitEntityState((turnTime + 0.5 * moveTime) / (moveTime + turnTime), sprite);
            sprite.setScale(0.5);
            sprite.setAlpha(0);
        }
        sprite.setX(BoardView.TILE_SIZE * car.getPosition().getX() + BoardView.TILE_SIZE / 2);
        sprite.setY(BoardView.TILE_SIZE * car.getPosition().getY() + BoardView.TILE_SIZE / 2);

        if (crash) {
            String[] explosions = graphics.createSpriteSheetSplitter()
                    .setSourceImage("explosion.png")
                    .setName("x")
                    .setImageCount(15)
                    .setOrigRow(0)
                    .setOrigCol(0)
                    .setImagesPerRow(4)
                    .setWidth(3100 / 4)
                    .setHeight(3100 / 4)
                    .split();
            // transpose, spritesheet is in wrong order
            for (int x = 0; x < 4; x++) {
                for (int y = x+1; y < 4; y++) {
                    int i1 = 4*x+y;
                    int i2 = 4*y+x;
                    String tmp = explosions[i1];
                    explosions[i1] = explosions[i2];
                    explosions[i2] = tmp;
                }
            }
            SpriteAnimation explosion = graphics.createSpriteAnimation().setImages(explosions)
                    .setScale(BoardView.TILE_SIZE / (3100 / 4.0))
                    .setX(BoardView.TILE_SIZE * car.getPosition().getX())
                    .setY(BoardView.TILE_SIZE * car.getPosition().getY());
            boardView.getBoardGroup().add(explosion);
            graphics.commitEntityState((turnTime + 0.5 * moveTime) / (moveTime + turnTime), explosion, boardView.getBoardGroup());
            explosion.setDuration(moveTime/2).setLoop(false).play();
        }

        previousLocation = car.getPosition();
    }
}
