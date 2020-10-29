package cs361.battleships.models;

public class Result {

	private AtackStatus result;
	private Ship ship;
	private Square location;

	public AtackStatus getResult() {
	    return this.result;
	}

	public void setResult(AtackStatus result) {
	    this.result = result;
	}

	public Ship getShip() {
	    return this.ship;
	}

	public void setShip(Ship ship) {
	    this.ship = ship;
	}

	public Square getLocation() {
	    return this.location;
	}

	public void setLocation(Square square) {
	    this.location = square;
	}
}
