package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class SonarGameAction {

    @JsonProperty
    private Game game;
    @JsonProperty
    private int x;
    @JsonProperty
    private char y;
    @JsonProperty
    private int numSonars;

    public Game getGame() {
        return game;
    }

    public int getActionRow() {
        return x;
    }

    public char getActionColumn() {
        return y;
    }

    public int getNumSonars() {
        return numSonars;
    }
}

