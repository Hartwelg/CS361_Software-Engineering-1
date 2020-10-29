package cs361.battleships.models;

public class Destroyer extends Ship {
    public Destroyer() {
        super();
        this.length = 3;
        this.armored = 1;
        this.floatstatus = true;
        this.kind = "DESTROYER";
        this.submerged = false;

    }
}