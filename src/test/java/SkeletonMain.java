import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {

        SoloGameRunner gameRunner = new SoloGameRunner();
        //gameRunner.setAgent(Agent1.class);
        gameRunner.setTestCase("test3.json");

        // Another way to add a player
        gameRunner.setAgent("mono /home/eulerschezahl/Dokumente/Programmieren/challenges/CodinGame/CarMaze/bin/Release/CarMaze.exe");

        gameRunner.start();
    }
}
