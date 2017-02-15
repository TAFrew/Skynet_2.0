package connectFour.history;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a serializable class that stores the history of all the games on connect four that have been played in a list
 * @author Tim Frew
 */
public class GameHistory implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Game> _games;

	/**
	 * This constructor creates an empty list of games
	 */
	public GameHistory(){
		_games = new ArrayList<>();
	}
	
	/**
	 * This method adds a game to the history
	 * @param game
	 */
	public void addGame(Game game){
		_games.add(game);
	}

	/**
	 * This method returns the list of games
	 * @return
	 */
	public ArrayList<Game> getHistory() {
		return _games;
	}
}
