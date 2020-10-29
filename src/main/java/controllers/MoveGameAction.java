package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class MoveGameAction {

    @JsonProperty
    private Game game;
    @JsonProperty
    private int direction;
    @JsonProperty
    private int numMoves;

    public Game getGame() {
        return game;
    }

    public int getDirection() {
        return this.direction;
    }

    public int getNumMoves() {
        return this.numMoves;
    }
}

