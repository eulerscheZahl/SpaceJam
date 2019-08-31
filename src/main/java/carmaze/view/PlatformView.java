package carmaze.view;

import carmaze.Cell;
import carmaze.Platform;
import com.codingame.game.Referee;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class PlatformView {
    private Platform platform;
    private Group group;
    private Cell previousPosition;
    private TooltipModule tooltips;

    public PlatformView(Platform platform, GraphicEntityModule graphics, TooltipModule tooltips, BoardView view) {
        this.platform = platform;
        previousPosition = platform.getPosition();
        this.tooltips = tooltips;

        Sprite platformSprite =  view.getTileSprite();
        Sprite frameSprite = graphics.createSprite().setImage("frame.png");
        Sprite arrowSprite = graphics.createSprite().setImage(platform.getDirections() + ".png");
        arrowSprite.setScale(0.35).setX(7).setY(7);

        group = graphics.createGroup(platformSprite, arrowSprite, frameSprite);
        group.setX(BoardView.TILE_SIZE * platform.getPosition().getX());
        group.setY(BoardView.TILE_SIZE * platform.getPosition().getY());
        view.getBoardGroup().add(group);
        tooltips.setTooltipText(group, "platform\nid: " + platform.getId() +"\nx: " + platform.getPosition().getX() + "\ny: " + platform.getPosition().getY() + "\n directions: " + platform.getDirections());
    }

    public void update() {
        int dx = platform.getPosition().getX() - previousPosition.getX();
        int dy = platform.getPosition().getY() - previousPosition.getY();
        Referee.manager.setFrameDuration(150 * (Math.abs(dx) + Math.abs(dy)));
        group.setX(BoardView.TILE_SIZE * platform.getPosition().getX());
        group.setY(BoardView.TILE_SIZE * platform.getPosition().getY());
        previousPosition = platform.getPosition();
        tooltips.setTooltipText(group, "platform\nid: " + platform.getId() +"\nx: " + platform.getPosition().getX() + "\ny: " + platform.getPosition().getY() + "\n directions: " + platform.getDirections());
    }
}

