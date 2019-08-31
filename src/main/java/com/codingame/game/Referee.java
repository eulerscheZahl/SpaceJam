package com.codingame.game;

import carmaze.Board;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject
    private SoloGameManager<Player> gameManager;
    @Inject
    private GraphicEntityModule graphicEntityModule;
    @Inject
    TooltipModule tooltips;
    private Board board;

    public static SoloGameManager<Player> manager;

    @Override
    public void init() {
        manager = gameManager;
        String input = gameManager.getTestCaseInput().get(0);
        board = new Board(input, graphicEntityModule, tooltips);
    }

    @Override
    public void gameTurn(int turn) {
        Player player = gameManager.getPlayer();
        for (String line : board.getInput(turn == 1)) player.sendInputLine(line);
        player.execute();

        try {
            String output = player.getOutputs().get(0);
            String[] parts = output.split(" ");
            if (parts.length < 2) throw new Exception("You have to print an ID and direction");
            int id = Integer.parseInt(parts[0]);
            String direction = parts[1];
            if (!direction.equals("R") && !direction.equals("L") && !direction.equals("U") && !direction.equals("D"))
                throw new Exception("invalid direction: " + direction);
            board.applyAction(id, direction);
            if (board.lose()) gameManager.loseGame("You lost the car");
            if (board.win()) gameManager.winGame("You guided the car to the target");

        } catch (TimeoutException e) {
            gameManager.loseGame("timeout");
        } catch (Exception e) {
            gameManager.loseGame(e.getMessage());
        }
    }
}
