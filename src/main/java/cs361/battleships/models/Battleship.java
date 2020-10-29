package cs361.battleships.models;

public class Battleship extends Ship {
    public Battleship (){
        super();
        this.length = 4;
        this.armored = 1;
        this.floatstatus = true;
        this.kind = "BATTLESHIP";
        this.submerged = false;
    }
}