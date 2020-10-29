package cs361.battleships.models;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class gameTest {
    @Test
    public void testRandRow() {
        Game game = new Game();
        for (int i = 0; i < 100; i++) {
            int rand = game.randRow();
            assertTrue(rand <= 10 && rand >= 1);
        }
    }

    @Test
    public void testRandCol() {
        Game game = new Game();
        for (int i = 0; i < 100; i++) {
            int rand = (game.randCol() - 'A') + 1;
            assertTrue(rand <= 10 && rand >= 1);
        }
    }

    @Test
    public void testRandVertical() {
        Game game = new Game();
        boolean rand = game.randVertical();
        assertTrue(rand || !rand);
    }

    @Test
    public void testPlaceShip() {
        Game game = new Game();
        assertTrue(game.placeShip(new Minesweeper(), 1, 'A', false));
    }

    @Test
    public void testAttack() {
        Game game = new Game();
        assertTrue(game.attack(1, 'A'));
    }
}
