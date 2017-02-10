package connectFour.model;

import java.util.ArrayList;

public class ConnectFourBoard {
	private ArrayList<Square> _squares;

	private ArrayList<Square> _possibleSquares;

	public ConnectFourBoard(){
		_squares = new ArrayList<>();

		for(int cols = 0; cols < 7; cols++){
			for(int rows = 0; rows < 6; rows++){
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
					toAdd.add(getNextSquare(s));
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
		if(playerHasWon("Player One")){
			return "Player 1 Won";
		}
		else if(playerHasWon("Player Two")){
			return "Player 2 Won";
		}
		else{
			return "Draw";
		}
	}

	private boolean playerHasWon(String player) {
		// TODO Auto-generated method stub

		// perhaos have input of four to check for win, (four in a row) cna then use input of three, to see if can win next turn

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

		// Vetical
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
		for(Square s : _squares){
			int row = s.getX();
			int col = s.getY();
			if(s.filledBy().equals(player) && getSquare(row, col).filledBy().equals(player) && s.filledBy().equals(player) && s.filledBy().equals(player)){
				
			}
			/*catch(IndexOutOfBoundsException e){
				// not win in this square
			}*/
		}
		
		// if at any point out of range, return false for win



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

	private ArrayList<Square> getRow(int i) {
		ArrayList<Square> squares = new ArrayList<>();
		for(Square s : _squares){
			if(s.isInRow(i)){
				squares.add(s);
			}
		}
		return squares;
	}

	private ArrayList<Square> getCol(int i) {
		ArrayList<Square> squares = new ArrayList<>();

		for(Square s : _squares){
			if(s.isInColumn(i)){
				squares.add(s);
			}
		}

		return squares;
	}

	private ArrayList<Square> getDiag(int i) {
		ArrayList<Square> squares = new ArrayList<>();

		if(i == 0){
			for(Square s: _squares){
				if(s.isInRow(0) && s.isInColumn(0)){
					squares.add(s);
				}
				if(s.isInRow(1) && s.isInColumn(1)){
					squares.add(s);
				}
				if(s.isInRow(2) && s.isInColumn(2)){
					squares.add(s);
				}
			}
		}
		else if(i == 1){
			for(Square s: _squares){
				if(s.isInRow(0) && s.isInColumn(2)){
					squares.add(s);
				}
				if(s.isInRow(1) && s.isInColumn(1)){
					squares.add(s);
				}
				if(s.isInRow(2) && s.isInColumn(0)){
					squares.add(s);
				}
			}

		}

		return squares;
	}

	private boolean isSelectedByPlayerOne(ArrayList<Square> squares) {
		for(Square s : squares){
			if(!(s.filledBy().equals("Player One"))){
				return false;
			}
		}
		return true;
	}

	private boolean isSelectedByPlayerTwo(ArrayList<Square> squares) {
		for(Square s : squares){
			if(!(s.filledBy().equals("Player Two"))){
				return false;
			}
		}
		return true;
	}

	public void endGame() {
		for(Square s : _possibleSquares){
			s.setAsFilled("Nobody");
		}
	}

	public ArrayList<Square> getSquares() {
		return _squares;
	}
}
