package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        for (int i = 0; i < 10; i ++) {
            assertFalse(board.placeShip(new Minesweeper(), i+11, 'C', true)); // place some ships out of bounds
        }
        // Test overlapping
        assertTrue(board.placeShip(new Minesweeper(), 1, 'C', true));
        assertFalse(board.placeShip(new Minesweeper(), 1, 'C', true));

        // Test more than one of same type
        assertFalse(board.placeShip(new Minesweeper(), 1, 'D', true));
    }

    @Test
    public void testValidPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Minesweeper(), 1, 'C', false)); // place some ships
        assertTrue(board.placeShip(new Battleship(), 2, 'C', false)); // place some ships
        assertTrue(board.placeShip(new Destroyer(), 3, 'C', false)); // place some ships
    }

    @Test
    public void testAttackHit(){
        Board board = new Board();
        board.placeShip(new Minesweeper(), 5, 'C', true); //place a ship and hit it twice in a row
        assertTrue( board.attack(5, 'C').getResult() == AtackStatus.HIT );
        assertTrue( board.attack(6, 'C').getResult() == AtackStatus.SUNK );

        board.placeShip(new Destroyer(), 5, 'B', true);
        assertTrue( board.attack(5, 'B').getResult() == AtackStatus.HIT ); //Testing sink with misses in between
        assertTrue( board.attack(6, 'B').getResult() == AtackStatus.MISS );
        board.attack(5, 'A');
        board.attack(1, 'A');
        board.attack(3, 'B');
        assertTrue( board.attack(6, 'B').getResult() == AtackStatus.SUNK );
    }

    @Test
    public void testAttackMiss(){
        Board board = new Board();
        board.placeShip(new Minesweeper(), 5, 'C', true); //place 2 ships, miss 1 attack hit the next 2
        board.placeShip(new Battleship(),  4, 'E', true);
        assertTrue( board.attack(5, 'D').getResult() == AtackStatus.MISS); //miss
        assertFalse( board.attack(4, 'E').getResult() == AtackStatus.MISS); //hit battleship
        assertFalse( board.attack(6, 'C').getResult() == AtackStatus.MISS); //hit minesweeper
    }

    @Test
    public void testSonarConditions() {
        Board board = new Board();
        assertFalse(board.sonar(5, 'c', 2));
        assertFalse(board.sonar(5, 'c', 0));
    }

    @Test
    public void testSonarPlacement() {
        Board board = new Board();
        List<Result> atks = new ArrayList<>();
        Result res = new Result();
        res.setResult(AtackStatus.SUNK);
        atks.add(res);
        board.setAttacks(atks);
        assertTrue(board.sonar(5, 'c', 2));
    }

    @Test
    public void testGameEnd(){
        Board board = new Board();
        board.placeShip(new Minesweeper(), 5, 'C', true); //place all 3 ships
        board.placeShip(new Battleship(),  4, 'E', true);
        board.placeShip(new Destroyer(),  4, 'F', true);
        board.placeShip(new Submarine(), 1, 'B', true);

        board.attack(5, 'C'); //sink minesweeper
        board.attack(6, 'C');

        board.attack(4, 'E'); //sink battleship
        board.attack(5, 'E');
        board.attack(6, 'E');
        board.attack(5, 'E');

        board.attack(4, 'F'); //sink destroyer
        board.attack(5, 'F');

        board.attack(1, 'B');
        board.attack(1, 'B');

        assertTrue( board.attack(5, 'F').getResult() == AtackStatus.SURRENDER ); //should end game on
    }

    @Test
    public void testMoveConditions() {
        Board board = new Board();
        assertFalse(board.checkMoveCondition());
        List<Result> atks = new ArrayList<>();
        Result res = new Result();
        res.setResult(AtackStatus.SUNK);
        atks.add(res);
        atks.add(res);
        board.setAttacks(atks);
        assertTrue(board.checkMoveCondition());
    }

    @Test
    public void testMovement() {
        Board board = new Board();
        List<Result> atks = new ArrayList<>();
        Result res = new Result();
        res.setResult(AtackStatus.SUNK);
        atks.add(res);
        atks.add(res);
        board.setAttacks(atks);
        board.placeShip(new Minesweeper(), 5, 'C', false); //place all 3 ships
        board.placeShip(new Submarine(), 2, 'C', false); //place all 3 ships
        assertTrue(board.move(0, 2));
        assertTrue(board.move(1, 2));
        assertTrue(board.move(2, 2));
        assertTrue(board.move(3, 2));
    }
}
