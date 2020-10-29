package cs361.battleships.models;

public class Submarine extends Ship{
    public Submarine (){
        super();
        this.length = 4;
        this.armored = 1;
        this.floatstatus = true;
        this.kind = "SUBMARINE";
        this.submerged = true;
    }
    void setCaptainsQuarters()
    {
        this.captainsQuarters = this.occupiedSquares.get(3);
    }
}
