package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Ship> ships;
    private List<Result> attacks;
    private List<Square> sonars;
    private int rows;
    private int cols;
    private int numSonars;
    private boolean laser;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.laser = false;
	    this.rows = 10;
	    this.cols = 10;
	    this.numSonars = 2;
	    this.ships = new ArrayList<Ship>();
		this.attacks = new ArrayList<Result>();
		this.sonars = new ArrayList<Square>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
	    int length;
	    ArrayList<Square> targetSquares = new ArrayList<>();
	    // Check that type of ship hasn't already been placed
		for (Ship s : this.ships) {
			if (s.getKind().equals(ship.getKind())) {
				return false;
			}
		}
		if (ship.kind.equals("SUBMARINE"))
		{
			length = ship.getLength();
			for (int i = 0; i < length; i++)
			{
				if (isVertical)
				{
					targetSquares.add(new Square(x + i, (char)((int)y)));
					if (i == 2)
					{
						targetSquares.add(new Square(x + 1, (char)((int)y - 1)));
					}
				}
				else
				{
					targetSquares.add(new Square(x, (char)((int)y + i)));
					if (i == 2)
					{
						targetSquares.add(new Square(x - 1, (char)((int)y + 2)));
					}
				}
			}
		}
		else
		{
			// Get ships length
			length = ship.getLength();
			// Add all target squares to list based on length of ship
			for (int i = 0 ; i < length; i++) {
				if (isVertical) {
					targetSquares.add(new Square(x + i, y));
				} else {
					// Convert char to int, add one, convert back
					targetSquares.add(new Square(x, (char)((int)y + i)));
				}
			}
		}

		// Check if the ship is in bounds
		for (Square sq : targetSquares) {
			// Not possible for the row to be less than 0 due to how placement works
			if (sq.getRow() > this.rows || sq.getRow() <= 0) {
				return false;
			}
			// Check column based on ascii value
			if (((int)sq.getColumn() - 65) >= this.cols || ((int)sq.getColumn() - 65) < 0) {
				return false;
			}
		}
		if(!ship.getKind().equals("SUBMARINE")) {
			// Check that the square isn't already occupied
			for (Ship s : this.ships) {
				for (Square occ : s.getOccupiedSquares()) {
					for (Square target : targetSquares) {
						if (occ.getRow() == target.getRow() && occ.getColumn() == target.getColumn()) {
							if(!s.getKind().equals("SUBMARINE")) {
								return false;
							}
						}
					}
				}
			}
		}
		ship.setOccupiedSquares(targetSquares);
		if(ship.getKind().equals("SUBMARINE")) {
			if(isVertical)
				ship.changeCQ(0);
			else
				ship.changeCQ(4);
		}else{
			ship.changeCQ(1);
		}
		this.ships.add(ship);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
	    Result result = new Result();
        Square attackLoc = new Square(x, y);
        result.setLocation(attackLoc);


        // Continue if attack is valid
		boolean hit = false;
		List<Ship> shipsHit = new ArrayList<Ship>();
		boolean resset = false;
	    for (Ship ship : this.ships) {
	        // Check if the attack hits a ship
            for (Square occSquare : ship.getOccupiedSquares()) {
                if (occSquare.getRow() == attackLoc.getRow() && occSquare.getColumn() == attackLoc.getColumn()) {
					hit = true;
					shipsHit.add(ship);

					if(!ship.getKind().equals("SUBMARINE") || !resset) {
						result.setShip(ship);
						result.setLocation(attackLoc);
						resset = true;
					}

                }
            }
	    }
		// Check if valid attack
		for(Result r : attacks){
			if(attackLoc.getRow() == r.getLocation().getRow() && attackLoc.getColumn() == r.getLocation().getColumn()){
				if(r.getResult() == AtackStatus.SUNK ){
					result.setResult(AtackStatus.INVALID);
					return result;
				}

			}
		}
            if (hit) {

				for(Result r : attacks){
					if(r.getResult() == AtackStatus.SUNK){
						laser = true;
						break;
					}

				}

				int sunk = 0;
				int cquartershit = 0;
				if (result.getShip().getCaptainsQuarters().getColumn() == attackLoc.getColumn() && result.getShip().getCaptainsQuarters().getRow() == attackLoc.getRow()) {
					if(result.getShip().getArmored() == 0){

						sunk = 1;
					}else {
						if(result.getShip().getSubmerged() == false) {
							result.getShip().setArmored(0);
							cquartershit = 1;
						}else if(laser){
							result.getShip().setArmored(0);
							cquartershit = 1;
						}
					}

				}
				if(shipsHit.size() == 2){	//if you hit two ships at the same time, and the subs captains quarters was hit, we need to add this to the attack array
					for(Ship s : shipsHit){
						if(s.getKind().equals("SUBMARINE")) {
							if (s.getCaptainsQuarters().getColumn() == attackLoc.getColumn() && s.getCaptainsQuarters().getRow() == attackLoc.getRow()) {
								if (s.getArmored() == 0) {
									Result subres = new Result();
									subres.setShip(s);
									subres.setLocation(s.getCaptainsQuarters());
									subres.setResult(AtackStatus.SUNK);
									this.attacks.add(subres);
								} else if (laser) {
									s.setArmored(0);

								}
							}
						}
					}
				}

				if(sunk == 1){ //if every tile in the ship has been hit, it was sunk or game is over
					int sunkShips = 0;

					for(Square s : result.getShip().getOccupiedSquares()) {
						boolean displayed = false;
						for (Result r : attacks) {
							if((r.getLocation().getColumn() == s.getColumn() && r.getLocation().getRow() == s.getRow())
							|| (s.getColumn() == attackLoc.getColumn() && s.getRow() == attackLoc.getRow())){
								displayed = true;
							}
						}
						if(!displayed){
							Result fillship = new Result();
							fillship.setShip(result.getShip());
							fillship.setLocation(s);
							fillship.setResult(AtackStatus.HIT);
							this.attacks.add(fillship);
						}
					}
					for(Result r : attacks){ //go through each attack, count up the number of sunk ships
						if( r.getResult() == AtackStatus.SUNK){
							sunkShips++;
						}
					}
					if(sunkShips > 2){ //if sunk ships was already at 3, now at 4 with current sink, end game
						result.setResult(AtackStatus.SURRENDER);
					} else { //otherwise, just sink the ship
						result.setResult(AtackStatus.SUNK);
					}

				} else { //if you didnt sink a ship, its just a hit, unless you hit captains quarters
					if(cquartershit == 1 || (!laser && result.getShip().getKind().equals("SUBMARINE"))){

						result.setResult(AtackStatus.MISS);
					}else {
						result.setResult(AtackStatus.HIT);
					}

					int subcheck = 0;
					for(Result r : attacks){ //go through each attack, count up the number of sunk ships
						if( r.getResult() == AtackStatus.SUNK){
							subcheck++;
						}
					}

					if(subcheck > 3) {
						result.setResult(AtackStatus.SURRENDER);
					}
				}


            } else {
                result.setResult(AtackStatus.MISS);
            }
        this.attacks.add(result);
        return result;
	}

	public boolean sonar(int x, char y, int numSonars) {
		if (!checkSonarCondition() || numSonars == 0)
			return false;
	    int radius = 2;
	    List<Square> squares = new ArrayList<>();
	    // Add circle pattern to squares
	    for (int i = 0; i < radius+1; i++) {
			squares.add(new Square(x+i, y));
			squares.add(new Square(x-i, y));
			squares.add(new Square(x, (char)((int)y+i)));
			squares.add(new Square(x, (char)((int)y-i)));
			squares.add(new Square(x-1, (char)((int)y-1)));
			squares.add(new Square(x-1, (char)((int)y+1)));
			squares.add(new Square(x+1, (char)((int)y-1)));
			squares.add(new Square(x+1, (char)((int)y+1)));
		}
	    for (Square s : squares) {
	    	// Check row and validity
			if (s.getRow() <= this.rows && s.getRow() > 0 && ((int)s.getColumn() - 65) < this.cols && ((int)s.getColumn() - 65) >= 0) {
				this.sonars.add(s);
			}
		}
		return true;
	}

	private boolean checkSonarCondition() {
	    for (Result r : this.attacks) {
	    	if (r.getResult() == AtackStatus.SUNK) {
	    		return true;
			}
		}
		return false;
	}

	public boolean move(int direction, int numMoves) {
	    if (numMoves == 0)
			return false;
		int moveX = 0;
		int moveY = 0;
		List<Ship> moveOrder = new ArrayList<>();
		int lowest = 12;
		Ship lowest_s = new Ship();
		switch(direction) {
			case 0:
				moveY = -1;
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE")) {
						if (s.getOccupiedSquares().get(0).getRow() < lowest) {
							lowest_s = s;
							lowest = s.getOccupiedSquares().get(0).getRow();
						}
					}
				}
				moveOrder.add(lowest_s);
				lowest = -5;
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE")) {
						if (s.getOccupiedSquares().get(0).getRow() > lowest) {
							lowest_s = s;
							lowest = s.getOccupiedSquares().get(0).getRow();
						}
					}
				}
				moveOrder.add(lowest_s);
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE") && !moveOrder.contains(s)) {
						moveOrder.add(1, s);
						break;
					}
				}
				break;
			case 1:
				moveX = 1;
				lowest = -5;
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE")) {
						if ((int)s.getOccupiedSquares().get(0).getColumn() - 65 > lowest) {
							lowest_s = s;
							lowest = s.getOccupiedSquares().get(0).getRow();
						}
					}
				}
				moveOrder.add(lowest_s);
				lowest = 12;
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE")) {
						if ((int)s.getOccupiedSquares().get(0).getColumn() - 65 < lowest) {
							lowest_s = s;
							lowest = s.getOccupiedSquares().get(0).getRow();
						}
					}
				}
				moveOrder.add(lowest_s);
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE") && !moveOrder.contains(s)) {
						moveOrder.add(1, s);
						break;
					}
				}
				break;
			case 2:
				moveY = 1;
				lowest = -5;
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE")) {
						if (s.getOccupiedSquares().get(0).getRow() > lowest) {
							lowest_s = s;
							lowest = s.getOccupiedSquares().get(0).getRow();
						}
					}
				}
				moveOrder.add(lowest_s);
				lowest = 12;
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE")) {
						if (s.getOccupiedSquares().get(0).getRow() < lowest) {
							lowest_s = s;
							lowest = s.getOccupiedSquares().get(0).getRow();
						}
					}
				}
				moveOrder.add(lowest_s);
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE") && !moveOrder.contains(s)) {
						moveOrder.add(1, s);
						break;
					}
				}
				break;
			case 3:
				moveX = -1;
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE")) {
						if ((int)s.getOccupiedSquares().get(0).getColumn() - 65 < lowest) {
							lowest_s = s;
							lowest = s.getOccupiedSquares().get(0).getRow();
						}
					}
				}
				moveOrder.add(lowest_s);
				lowest = -5;
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE")) {
						if ((int)s.getOccupiedSquares().get(0).getColumn() - 65 > lowest) {
							lowest_s = s;
							lowest = s.getOccupiedSquares().get(0).getRow();
						}
					}
				}
				moveOrder.add(lowest_s);
				for (Ship s : this.ships) {
					if (!s.getKind().equals("SUBMARINE") && !moveOrder.contains(s)) {
						moveOrder.add(1, s);
						break;
					}
				}
				break;
		}
		for (Ship s : this.ships) {
			if (s.getKind().equals("SUBMARINE")) {
				moveOrder.add(s);
				break;
			}
		}

		List<Ship> shipMoves = new ArrayList<>();
		boolean onEdge = false;
		for (Ship s : moveOrder) {
			Ship temp = new Ship(s);
			boolean canMove = true;
			for (Square tempsq : temp.getOccupiedSquares()) {
				if (tempsq.getRow() + moveY > this.rows || tempsq.getRow() + moveY < 1 || ((int)tempsq.getColumn() - 65) + moveX >= this.cols || ((int)tempsq.getColumn() - 65) + moveX < 0) {
				    canMove = false;
				    onEdge = true;
                }
			}
			if (canMove) {
				boolean hitSomething = false;
				if (onEdge && !s.getKind().equals("SUBMARINE")) {
					for (Ship i : moveOrder) {
                        if (i != s) {
                            for (Square sq1 : i.getOccupiedSquares()) {
                                for (Square sq2 : s.getOccupiedSquares()) {
                                    if (sq1.getRow() == sq2.getRow() + moveY && sq1.getColumn() == (char)((int)sq2.getColumn() + moveX)) {
                                        hitSomething = true;
                                    }
                                }
                            }
                        }
					}
					if (!hitSomething) {
						for (Square sq : temp.getOccupiedSquares()) {
							sq.setColumn((char) ((int) sq.getColumn() + moveX));
							sq.setRow(sq.getRow() + moveY);
						}
					}
		 		} else {
					for (Square sq : temp.getOccupiedSquares()) {
						sq.setColumn((char) ((int) sq.getColumn() + moveX));
						sq.setRow(sq.getRow() + moveY);
					}
				}
			}
			shipMoves.add(temp);
		}
		this.ships = shipMoves;
		return true;
	}

	public boolean checkMoveCondition() {
		int numSunk = 0;
		for (Result r : this.attacks) {
			if (r.getResult() == AtackStatus.SUNK) {
			    numSunk++;
			}
		}
		return numSunk == 2;
	}
	public List<Ship> getShips() {
		return this.ships;
	}

	public void setShips(List<Ship> ships) {
	    this.ships = ships;
	}

	public List<Result> getAttacks() {
	    return this.attacks;
	}

	public void setAttacks(List<Result> attacks) {
	    this.attacks = attacks;
	}

	public List<Square> getSonars() {
		return this.sonars;
	}

	public void setSonars(List<Square> sonars) {
		this.sonars = sonars;
	}

	public int getRows() {
	    return this.rows;
    }

    public int getCols() {
	    return this.cols;
    }
}
