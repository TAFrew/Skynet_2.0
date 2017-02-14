package connectFour.model;

import java.util.ArrayList;

public class ConnectFourBoard {
	private ArrayList<Square> _squares;

	private ArrayList<Square> _possibleSquares;

	public ConnectFourBoard(){
		_squares = new ArrayList<>();

		for(int rows = 0; rows < 6; rows++){
			for(int cols = 0; cols < 7; cols++){
				_squares.add(new Square(rows, cols));
			}
		}

		_possibleSquares = new ArrayList<>();
		for(Square s : _squares){
			if(s.isInRow(0)){
				_possibleSquares.add(s);
			}
		}
	}

	public boolean isFull(){
		for(Square s : _squares){
			if(!(s.isFilled())){
				return false;
			}
		}
		return true;
	}

	public ArrayList<Square> getPossibleMoves(){
		return _possibleSquares;
	}

	public void updatePossibleMoves() {
		ArrayList<Square> toAdd = new ArrayList<>();
		ArrayList<Square> toRemove = new ArrayList<>();

		for(Square s : _possibleSquares){
			if(s.isFilled()){
				if(s.isInRow(5)){
					toRemove.add(s);
				}
				else{
					toRemove.add(s);
					Square nextUp = getSquare(s.getX() + 1, s.getY());
					toAdd.add(nextUp);
				}
			}
		}

		_possibleSquares.removeAll(toRemove);
		_possibleSquares.addAll(toAdd);
	}

	private Square getNextSquare(Square s) {
		for(Square next : _squares){
			if((next.getX() == s.getX() && (next.getY() == s.getY() + 1))){
				return next;
			}
		}
		return null;
	}

	public String getResult() {
		if(playerHasWon("P1")){
			return "P1W";
		}
		else if(playerHasWon("P2")){
			return "P2W";
		}
		else{
			return "Draw";
		}
	}

	/**
	 * This method will check whether a given player can win in their next turn.
	 * It will call the playerHasLine(int num) method with an input of 3 (ie one move from winning) and then will check
	 * if it is possible to go in the given square
	 * @param player
	 * @return : the square where the player can win
	 */
	public Square playerCanWin(String player){
		Square s = playerHasLine(player, 3);
		return s;
	}

	private Square playerHasLine(String player, int length){
		// for every square
		for(Square s : _squares){
			// get coordinates
			int row = s.getX();
			int col = s.getY();

			// set booleans of lines
			boolean posGradient = true;
			boolean negGradient = true;
			boolean horizontalLeft = true;
			boolean horizontalRight = true;
			boolean vertical = true;
			
			// for length of line you are checking
			for(int i = 0; i < length; i++){
				// check positive gradient diagonal
				try{
					if(row + i < 6 && col + i < 7){
						if(!(getSquare(row + i, col + i).filledBy().equals(player))){
							posGradient = false;
						}
					}
					else{
						posGradient = false;
					}
				}
				catch(IndexOutOfBoundsException e){
					// not win in this square
					posGradient = false;
				}
				
				// check negative gradient diagonal
				try{
					if(row > 0){
						if(!(getSquare(row - i, col + i).filledBy().equals(player))){
							negGradient = false;
						}
					}
					else{
						negGradient = false;
					}
				}
				catch(IndexOutOfBoundsException e){
					// not win in this square
					negGradient = false;
				}
				catch(NullPointerException e){
					// not win in this square
					negGradient = false;
				}

				// check horizontal left
				try{
					if(col - i >= 0){
						if(!(getSquare(row, col - i).filledBy().equals(player))){
							horizontalLeft = false;
						}
					}
					else{
						horizontalLeft = false;
					}
				}
				catch(IndexOutOfBoundsException e){
					// not win in this square
					horizontalLeft = false;
				}
				
				// check horizontal right
				try{
					if(col + i < 7){
						if(!(getSquare(row, col + i).filledBy().equals(player))){
							horizontalRight = false;
						}
					}
					else{
						horizontalRight = false;
					}
				}
				catch(IndexOutOfBoundsException e){
					// not win in this square
					horizontalRight = false;
				}
				
				// check vertical
				try{
					if(row + i < 6){
						if(!(getSquare(row + i, col).filledBy().equals(player))){
							vertical = false;
						}
					}
					else{
						vertical = false;
					}
				}
				catch(IndexOutOfBoundsException e){
					// not win in this square
					vertical = false;
				}
			}
			
			if(posGradient){
				Square toGo = getSquare(s.getX() + length, s.getY() + length);
				if(_possibleSquares.contains(toGo)){
					return toGo;
				}
			}
			else if(negGradient){
				Square toGo = getSquare(s.getX() + length, s.getY() - length);
				if(_possibleSquares.contains(toGo)){
					return toGo;
				}
			}
			else if(horizontalLeft){
				Square toGo = getSquare(s.getX(), s.getY() - length);
				if(_possibleSquares.contains(toGo)){
					return toGo;
				}
			}
			else if(horizontalRight){
				Square toGo = getSquare(s.getX(), s.getY() + length);
				if(_possibleSquares.contains(toGo)){
					return toGo;
				}
			}
			else if(vertical){
				Square toGo = getSquare(s.getX() + length, s.getY());
				if(_possibleSquares.contains(toGo)){
					return toGo;
				}
			}
		}

		return null;
	}

	// possibly change this to return a square? Actually probably have it call the playerHasLine method	with 4 and if it
	// returns null say false else say true
	private boolean playerHasWon(String player) {

		// if at any point out of range, return false for win
		//TODO perhaps have input of four to check for win, (four in a row) can then use input of three, to see if can win next turn

		// Horizontal
		// for rows = all
		// for cols = 0 -> 3
		// if sq[cols] , cols +1 cols +2 and cols +3 are selected by player, they have won
		for(int row = 0; row < 6; row++){
			for(int col = 0; col < 4; col++){
				if(getSquare(row, col).filledBy().equals(player) && getSquare(row, col + 1).filledBy().equals(player) && getSquare(row, col + 2).filledBy().equals(player) && getSquare(row, col + 3).filledBy().equals(player)){
					return true;
				}
			}
		}

		// Vertical
		// for cols = all
		// for rows = 0 -> 2
		// if sq[rows] , rows +1 rows +2 and rows +3 are selected by player, they have won
		for(int col = 0; col < 7; col++){
			for(int row = 0; row < 3; row++){
				if(getSquare(row, col).filledBy().equals(player) && getSquare(row + 1, col).filledBy().equals(player) && getSquare(row + 2, col).filledBy().equals(player) && getSquare(row + 3, col).filledBy().equals(player)){
					return true;
				}
			}
		}

		// Diagonal
		// for all sqs
		// if sq , +1,+1,+2,+2,+3,+3 selected, player wins 
		// or if sq , -1,+1,-2,+2,-3,+3 selected, player wins 
		// TODO here Tim
		for(Square s : _squares){
			int row = s.getX();
			int col = s.getY();

			boolean posGradient = true;
			boolean negGradient = true;
			for(int i = 0; i < 4; i++){
				try{
					if(row + i < 6 && col + i < 7){
						if(!(getSquare(row + i, col + i).filledBy().equals(player))){
							posGradient = false;
						}
					}
					else{
						posGradient = false;
					}
				}
				catch(IndexOutOfBoundsException e){
					// not win in this square
					posGradient = false;
				}
				try{
					if(row > 0){
						if(!(getSquare(row - i, col + i).filledBy().equals(player))){
							negGradient = false;
						}
					}
					else{
						negGradient = false;
					}
				}
				catch(IndexOutOfBoundsException e){
					// not win in this square
					negGradient = false;
				}
				catch(NullPointerException e){
					// not win in this square
					negGradient = false;
				}
			}
			if(posGradient || negGradient){
				return true;
			}
		}




		return false;
	}

	private Square getSquare(int row, int col){
		for(Square s : _squares){
			if(s.isInColumn(col) && s.isInRow(row)){
				return s;
			}
		}
		return null;
	}

	public void endGame() {
		for(Square s : _squares){
			if(!(s.isFilled())){
				s.setAsFilled("Nobody");
			}
		}
	}

	public ArrayList<Square> getSquares() {
		return _squares;
	}
}
