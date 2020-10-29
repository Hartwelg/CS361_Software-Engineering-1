package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cs361.battleships.models.AtackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;
        boolean opponentPlacedSuccessfully;
        Ship oppShip = new Ship(ship);
        //oppShip.setOccupiedSquares(new ArrayList<>());
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(oppShip, randRow(), randCol(), randVertical());
        } while (!opponentPlacedSuccessfully);
        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        return true;
    }

    public boolean sonar(int x, char y, int numSonars) {
        return opponentsBoard.sonar(x, y, numSonars);
    }

    public boolean move(int direction, int numMoves) {
        return playersBoard.move(direction, numMoves);
    }

    public boolean checkMoveCondition() {
        return opponentsBoard.checkMoveCondition();
    }

    char randCol() {
        return (char)(Math.random() * (opponentsBoard.getCols()) + 65);
    }

    int randRow() {
        return (int)((Math.random() * opponentsBoard.getRows()) + 1);
    }

    boolean randVertical() {
        if (Math.random() > 0.5)
            return true;
        else
            return false;
    }
}
