package cs361.battleships.models;

public class Minesweeper extends Ship {
    public Minesweeper() {
        super();
        this.length = 2;
        this.armored = 0;
        this.floatstatus = true;
        this.kind = "MINESWEEPER";
        this.submerged = false;
    }
}