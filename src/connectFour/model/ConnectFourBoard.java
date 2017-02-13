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

	//TODO this breaks
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
