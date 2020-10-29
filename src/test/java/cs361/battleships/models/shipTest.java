package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class shipTest {
    @Test
    public void testLengthKind() {
        Ship ship = new Minesweeper();
        assertTrue(ship.getKind() == "MINESWEEPER");
        assertTrue(ship.getLength() == 2);
        ship = new Destroyer();
        assertTrue(ship.getKind() == "DESTROYER");
        assertTrue(ship.getLength() == 3);
        ship = new Battleship();
        assertTrue(ship.getKind() == "BATTLESHIP");
        assertTrue(ship.getLength() == 4);
        ship = new Submarine();
        assertTrue(ship.getKind() == "SUBMARINE");
        assertTrue(ship.getLength() == 4);
    }

    @Test
    public void testOccupiedSquares() {
        Ship ship = new Ship("MINESWEEPER");
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(2, 'B'));
        ship.setOccupiedSquares(sqs);
        List<Square> check = ship.getOccupiedSquares();
        assertTrue(check.equals(sqs));
    }

    @Test
    public void testDefault() {
        Ship ship = new Ship();
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(2, 'B'));
        ship.setOccupiedSquares(sqs);

        assertTrue(ship.getKind() == null);
    }

    @Test
    public void testBattleshipHorizontalCaptainQuarter() {
        Ship ship = new Battleship();
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(1, 'B'));
        sqs.add(new Square(1, 'C'));
        sqs.add(new Square(1, 'D'));

        ship.setOccupiedSquares(sqs);

        ship.changeCQ(1);
        assertTrue(ship.getCaptainsQuarters() == ship.getOccupiedSquares().get(1));
        assertTrue(ship.getArmored() == 1);

    }


    @Test
    public void testBattleshipVerticalCaptainQuarter() {
        Ship ship = new Battleship();
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(2, 'A'));
        sqs.add(new Square(3, 'A'));

        ship.setOccupiedSquares(sqs);

        ship.changeCQ(1);
        assertTrue(ship.getCaptainsQuarters() == ship.getOccupiedSquares().get(1));
        assertTrue(ship.getArmored() == 1);

    }

    @Test
    public void testDestroyerHorizontalCaptainQuarter()
    {
        Ship ship = new Destroyer();
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(1, 'B'));
        sqs.add(new Square(1, 'C'));

        ship.setOccupiedSquares(sqs);

        ship.changeCQ(1);
        assertTrue(ship.getCaptainsQuarters() == ship.getOccupiedSquares().get(1));
        assertTrue(ship.getArmored() == 1);
    }

    @Test
    public void testMinesweeperVerticalCaptainQuarter()
    {
        Ship ship = new Minesweeper();
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(2, 'A'));

        ship.setOccupiedSquares(sqs);

        ship.changeCQ(1);
        assertTrue(ship.getCaptainsQuarters() == ship.getOccupiedSquares().get(1));
        assertTrue(ship.getArmored() == 0);
    }

    @Test
    public void testMinesweeperHorizontalCaptainQuarter()
    {
        Ship ship = new Minesweeper();
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(1, 'B'));

        ship.setOccupiedSquares(sqs);

        ship.changeCQ(1);
        assertTrue(ship.getCaptainsQuarters() == ship.getOccupiedSquares().get(1));
        assertTrue(ship.getArmored() == 0);
    }

    @Test
    public void testDestroyerVerticalCaptainQuarter()
    {
        Ship ship = new Destroyer();
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(2, 'A'));
        sqs.add(new Square(3, 'A'));
        sqs.add(new Square(4, 'A'));

        ship.setOccupiedSquares(sqs);


        ship.changeCQ(1);
        assertTrue(ship.getCaptainsQuarters() == ship.getOccupiedSquares().get(1));
        assertTrue(ship.getArmored() == 1);
    }

    @Test
    public void testNewSubmarine(){
        Submarine x = new Submarine();
        assertTrue(x.getKind() == "SUBMARINE");
        assertTrue(x.getLength() == 4);
    }

    @Test
    public void testOccupiedSquaresHorizontal() {
        Ship ship = new Ship("SUBMARINE");
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(1, 'B'));
        sqs.add(new Square(1, 'C'));
        sqs.add(new Square(1, 'D'));
        sqs.add(new Square(2, 'C'));
        ship.setOccupiedSquares(sqs);
        List<Square> check = ship.getOccupiedSquares();
        assertTrue(check.equals(sqs));
    }

    @Test
    public void testOccupiedSquaresVertical() {
        Ship ship = new Ship("SUBMARINE");
        ArrayList<Square> sqs = new ArrayList<>();
        sqs.add(new Square(1, 'A'));
        sqs.add(new Square(2, 'A'));
        sqs.add(new Square(3, 'A'));
        sqs.add(new Square(4, 'A'));
        sqs.add(new Square(3, 'B'));
        ship.setOccupiedSquares(sqs);
        List<Square> check = ship.getOccupiedSquares();
        assertTrue(check.equals(sqs));
    }

    @Test
    public void testHitPreLaser() {

        Board board = new Board();
        board.placeShip(new Submarine(), 1, 'B', true);
        assertTrue( board.attack(1, 'B').getResult() == AtackStatus.MISS );
        assertTrue( board.attack(2, 'B').getResult() == AtackStatus.MISS );
        assertTrue( board.attack(3, 'A').getResult() == AtackStatus.MISS );
        assertTrue( board.attack(3, 'B').getResult() == AtackStatus.MISS );
    }

    @Test
    public void testHitPostLaser() {
        Board board = new Board();
        board.placeShip(new Submarine(), 1, 'B', true);
        board.placeShip(new Destroyer(),  4, 'F', true);
        board.attack(5, 'F');
        board.attack(5, 'F');
        assertTrue( board.attack(1, 'B').getResult() == AtackStatus.MISS );
        assertTrue( board.attack(2, 'B').getResult() == AtackStatus.HIT );
        assertTrue( board.attack(2, (char)((int)'B' - 1)).getResult() == AtackStatus.HIT );
        assertTrue( board.attack(3, 'B').getResult() == AtackStatus.HIT );
    }

    @Test
    public void testSinkPostLaser() {
        Board board = new Board();
        board.placeShip(new Submarine(), 1, 'B', true);
        board.placeShip(new Destroyer(),  4, 'F', true);
        board.attack(5, 'F');
        board.attack(5, 'F');
        assertTrue( board.attack(1, 'B').getResult() == AtackStatus.MISS );
        assertTrue( board.attack(2, 'B').getResult() == AtackStatus.HIT );
        assertTrue( board.attack(2, (char)((int)'B' - 1)).getResult() == AtackStatus.HIT);
        assertTrue( board.attack(3, 'B').getResult() == AtackStatus.HIT );
        assertTrue( board.attack(1, 'B').getResult() == AtackStatus.SUNK );
    }

    @Test
    public void testHitOverlap() {
        Board board = new Board();
        board.placeShip(new Submarine(), 1, 'B', true);
        board.placeShip(new Destroyer(),  4, 'F', true);
        board.placeShip(new Minesweeper(), 1, 'B', true);

        assertTrue( board.attack(1, 'B').getResult() == AtackStatus.HIT ); //hit minesweeper with laser off
        board.attack(5, 'F'); //sink destroyer to activate laser
        board.attack(5, 'F');

        assertTrue( board.attack(1, 'B').getResult() == AtackStatus.HIT ); //hit sub with laser on
        assertTrue( board.attack(1, 'B').getResult() == AtackStatus.HIT ); //can sink ship with sub underneath, it is added in attack, not returned
        assertTrue( board.attack(2, 'A').getResult() == AtackStatus.HIT );
        assertTrue( board.attack(3, 'B').getResult() == AtackStatus.HIT );

    }


    @Test
    public void testCapsQuartersOverlap() {
        Board board = new Board();
        board.placeShip(new Submarine(), 2, 'B', true);
        board.placeShip(new Destroyer(),  1, 'B', true);
        board.placeShip(new Minesweeper(), 2, 'F', true);

        assertTrue( board.attack(3, 'F').getResult() == AtackStatus.SUNK );
        //assertTrue( board.attack(2, 'A').getResult() == AtackStatus.HIT ); //hit minesweeper with laser off

        assertTrue( board.attack(2, 'B').getResult() == AtackStatus.MISS ); //hit both capsquarters
        assertTrue( board.attack(2, 'B').getResult() == AtackStatus.SUNK ); //hits both again

    }
}