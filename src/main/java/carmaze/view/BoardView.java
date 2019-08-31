package carmaze.view;

import carmaze.Board;
import carmaze.Cell;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class BoardView {
    public static int TILE_SIZE = 64;
    public static int FRAME_SIZE = 20;
    private Group boardGroup;
    private String[] grounds;
    private GraphicEntityModule graphics;
    final int[] targetColors = {0xf8b203, 0x20a3e9, 0xfe195d};

    public BoardView(Board board, GraphicEntityModule graphics, TooltipModule tooltips) {
        this.graphics = graphics;
        graphics.createSprite()
                .setImage("background.png")
                .setBaseWidth(graphics.getWorld().getWidth())
                .setBaseHeight(graphics.getWorld().getHeight());

        grounds = graphics.createSpriteSheetSplitter()
                .setSourceImage("tile.png")
                .setName("t")
                .setImageCount(2)
                .setOrigRow(0)
                .setOrigCol(0)
                .setImagesPerRow(1)
                .setWidth(64)
                .setHeight(64)
                .split();

        boardGroup = graphics.createGroup();
        double scale = Math.min((double) (graphics.getWorld().getWidth() - 2 * FRAME_SIZE) / (TILE_SIZE * board.getWidth()),
                (double) (graphics.getWorld().getHeight() - 2 * FRAME_SIZE) / (TILE_SIZE * board.getHeight()));
        boardGroup.setScale(scale).setX(FRAME_SIZE).setY(FRAME_SIZE);

        BufferedGroup bufferedGroup = graphics.createBufferedGroup();
        boardGroup.add(bufferedGroup);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Cell cell = board.getCell(x, y);
                if (!cell.isGround()) continue;
                Sprite cellSprite = getTileSprite().setX(TILE_SIZE * x).setY(TILE_SIZE * y);
                Entity tooltipSprite = graphics.createRectangle().setX(TILE_SIZE * x).setY(TILE_SIZE * y).setWidth(TILE_SIZE).setHeight(TILE_SIZE).setAlpha(0);
                boardGroup.add(tooltipSprite); // can't place tooltip in BufferedGroup
                tooltips.setTooltipText(tooltipSprite, "ground tile\nx: " + cell.getX() + "\ny: " + cell.getY());
                bufferedGroup.add(cellSprite);
                if (cell.getTargetId() != -1) {
                    Sprite target = graphics.createSprite()
                            .setImage("target.png")
                            .setX(TILE_SIZE * x).setY(TILE_SIZE * y)
                            .setBaseWidth(TILE_SIZE).setBaseHeight(TILE_SIZE)
                            .setTint(targetColors[cell.getTargetId()]);
                    bufferedGroup.add(target);
                    tooltips.setTooltipText(tooltipSprite, "target tile " + cell.getTargetId() + "\nx: " + cell.getX() + "\ny: " + cell.getY());
                }
            }
        }
    }

    public Sprite getTileSprite() {
        return graphics.createSprite().setImage(grounds[Math.random() >= 0.5 ? 1 : 0]);
    }

    public Group getBoardGroup() {
        return boardGroup;
    }
}
