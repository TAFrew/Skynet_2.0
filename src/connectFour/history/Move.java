package connectFour.history;

import java.io.Serializable;

import connectFour.model.Square;

public class Move implements Serializable{
	private Square _square;
	private String _player;
	private int _moveNumber;
	
	public Move(Square square, String player, int move){
		_square = square;
		_player = player;
		_moveNumber = move;
	}
	
	public String toString(){
		return _square.toString() + "," +_player + "," + _moveNumber;
	}

	public boolean isSameMove(Move move) {
		return this.toString().equals(move.toString());
	}
	
	public Square getLocation(){
		return _square;
	}
	
	public int getMoveNumber(){
		return _moveNumber;
	}
}
