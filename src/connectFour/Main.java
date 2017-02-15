package connectFour;

import java.util.ArrayList;

import connectFour.gui.GUIConnectFourBoard;
import connectFour.history.Game;
import connectFour.history.Move;
import connectFour.history.GameHistory;
import connectFour.model.ConnectFourBoard;
import connectFour.model.NextMove;
import connectFour.model.Square;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * This is the Main class of the application. It launches it upon running and contains the logic for the AI.
 * @author Tim Frew
 */
public class Main extends Application{

	private ConnectFourBoard _board;
	private Game _game;
	private GameHistory _history;

	private static final String _file = System.getProperty("user.dir") + "/.Resources/history.ser";

	private GUIConnectFourBoard _GUI;
	private Stage _stage;
	private AnimationTimer _timer;

	private boolean _playersTurn = false;
	private int _turn = 1;

	/**
	 * The main method launches the application
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * This method is called when the application starts and initializes the GUI, mouse listener and animation timer
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();

		Scene scene = new Scene(_GUI.getPane());
		scene.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				handleMouseClick(event);
				_GUI.update();
			}
		});

		// stage
		_stage = primaryStage;
		_stage.setTitle("Connect Four");
		_stage.setScene(scene);
		_stage.show();

		// animation timer
		_timer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				if(!(_playersTurn)){
					AITurn();
				}
			}
		};

		_timer.start();
	}

	/*protected void AITurn() {
		ArrayList<NextMove> possibleNextMoves = new ArrayList<>();
		for(Square s : _board.getPossibleMoves()){
			possibleNextMoves.add(new NextMove(s,_turn));
		}

		if(!(possibleNextMoves.size() == 0)){

			String player = "";
			String desiredResult = "";
			String notDesiredResult = "";
			if(_turn % 2 == 1){
				player = "playerOne";
				desiredResult = "Player 1 Won";
				notDesiredResult = "Player 2 Won";
			}
			else{
				player = "playerTwo";
				desiredResult = "Player 2 Won";
				notDesiredResult = "Player 1 Won";
			}

			for(Game g : _history.getHistory()){
				if((_game.hasSameMoves(g)) && (_game.getMoves().size() < g.getMoves().size())){

					// get next move and result of game
					Move nextMove = g.getMoves().get(_game.getMoves().size());
					String result = g.getResult();

					// for each possible move record win percentage
					for(NextMove nm : possibleNextMoves){
						if(nm.isSameAs(nextMove)){
							nm.increment(result,desiredResult);

							// if next move results in winning, set wins()
							if(g.getResult().equals(desiredResult) && g.getMoves().size() == _game.getMoves().size() + 1){
								nm.wins();
								System.out.println("win");
							}
							// if move after next move has resulted in a loss, set loses()
							else if(g.getResult().equals(notDesiredResult) && g.getMoves().size() == _game.getMoves().size() + 2){
								nm.loses();
								System.out.println("loss");
							}
						}
					}

				}
			}



			// get move with the best rank
			NextMove moveToDo = null;
			int i = (int)(Math.random() * possibleNextMoves.size() + 1);
			moveToDo = possibleNextMoves.get(i - 1);
			for(NextMove move : possibleNextMoves){
				if(move.isBetterThan(moveToDo)){
					moveToDo = move;
				}
			}

			// if it is the first move just go randomly for now
			if(_turn == 1){
				i = (int)(Math.random() * possibleNextMoves.size() + 1);
				moveToDo = possibleNextMoves.get(i - 1);
			}
		}
	}
	 */
	/**
	 * This method contains the logic for the AI's turn. It reads from the history of previous games to find the best 
	 * move to take, or blocks the opponent if it can. If it can win it does so.
	 */
	protected void AITurn() {
		// set player identity
		String player = "";
		String opponent = "";
		String desiredResult = "";
		String notDesiredResult = "";
		if(_turn % 2 == 0){
			player = "P2";
			opponent = "P1";
			desiredResult = "P2W";
			notDesiredResult = "P1W";
		}
		else{
			player = "P1";
			opponent = "P2";
			desiredResult = "P1W";
			notDesiredResult = "P2W";
		}

		// if AI can win, then do it
		Square toGo = _board.playerCanWin(player);

		// if player can win, stop them
		if(toGo == null){
			toGo = _board.playerCanWin(opponent);
		}
		// else go randomly for now
		//TODO
		if(toGo == null){
			ArrayList<NextMove> possibleNextMoves = new ArrayList<>();
			for(Square s : _board.getPossibleMoves()){
				possibleNextMoves.add(new NextMove(s,_turn));
			}

			rankPossibleMoves(possibleNextMoves,desiredResult,notDesiredResult);

			// get move with the best rank
			NextMove moveToDo = null;
			int i = (int)(Math.random() * possibleNextMoves.size() + 1);
			moveToDo = possibleNextMoves.get(i - 1);
			for(NextMove move : possibleNextMoves){
				if(move.isBetterThan(moveToDo)){
					moveToDo = move;
				}
			}

			toGo = moveToDo.getSquare();
		}

		// set square as filled
		toGo.setAsFilled(player);

		// store move
		_game.addMove(new Move(toGo, player, _turn));

		// remove from possible squares
		_board.updatePossibleMoves();

		// increment move number
		_turn++;
		_playersTurn = true;

		// update GUI
		_GUI.update();

		// if game is over, end game
		checkResult();
	}

	private void rankPossibleMoves(ArrayList<NextMove> possibleNextMoves, String desiredResult, String notDesiredResult) {
		for(Game g : _history.getHistory()){
			if((_game.hasSameMoves(g)) && (_game.getMoves().size() < g.getMoves().size())){

				// get next move and result of game
				Move nextMove = g.getMoves().get(_game.getMoves().size());
				String result = g.getResult();

				// for each possible move record win percentage
				for(NextMove nm : possibleNextMoves){
					if(nm.isSameAs(nextMove)){
						nm.increment(result,desiredResult);

						// if next move results in winning, set wins()
						if(g.getResult().equals(desiredResult) && g.getMoves().size() == _game.getMoves().size() + 1){
							nm.wins();
							System.out.println("win");
						}
						// if move after next move has resulted in a loss, set loses()
						else if(g.getResult().equals(notDesiredResult) && g.getMoves().size() == _game.getMoves().size() + 2){
							nm.loses();
							System.out.println("loss");
						}
					}
				}

			}
		}
	}

	/**
	 * This method handles a players turn. It gets the square that they have clicked on, and if they can go here it does so.
	 * @param event
	 */
	protected void handleMouseClick(Event event) {
		if(_playersTurn){
			// get coordinates of mouse click event
			MouseEvent me = (MouseEvent) event;
			double horizontal = me.getSceneX();
			double vertical = me.getSceneY();

			// get square
			int count = 0;
			int finalCount = -1;
			for(int y = 5; y >= 0; y--){
				for(int x = 1; x < 8; x++){
					if(horizontal <= x * 100 && vertical >= y * 100){
						if(finalCount == -1){
							finalCount = count;
						}
					}
					count++;
				}
			}

			// get square to go in
			Square s = _board.getSquares().get(finalCount);

			if(_board.getPossibleMoves().contains(s)){
				// set square as filled
				String player = "";
				if(_turn % 2 == 0){
					player = "P2";
					s.setAsFilled("P2");
				}
				else{
					player = "P1";
					s.setAsFilled("P1");
				}

				// store move
				_game.addMove(new Move(s, player, _turn));

				// remove from possible squares
				_board.updatePossibleMoves();

				// increment move number
				_turn++;
				_playersTurn = false;

				// if game is over, end game
				checkResult();
			}
		}

	}

	/**
	 * This method checks if the game is over, and if it is, ends the application and stores the games history
	 */
	private void checkResult(){
		// check if someone has won
		if(_board.getResult().equals("P1W") || _board.getResult().equals("P2W") || _turn > 42){
			_board.endGame();
			// get result of game
			String result = _board.getResult();

			// set result
			_game.setResult(result);

			// record game
			_history.addGame(_game);

			// write history to file
			FileManipulator.writeHistoryTofile(_history, _file);

			// TODO make this so a pop up or something appears. maybe with an option to start another game
			// close GUI
			_stage.close();
		}
	}

	/**
	 * This method is called when the application starts. It initializes some fields for the main class
	 */
	private void initialize() {
		_history = FileManipulator.readHistoryFromFile(_file);
		_board = new ConnectFourBoard();
		_game = new Game();
		_GUI = new GUIConnectFourBoard(_board);
	}

	/**
	 * This method clears the history file
	 */
	private static void clearData(){
		GameHistory gh = new GameHistory();
		FileManipulator.writeHistoryTofile(gh, _file);
		System.out.println("Game history cleared");
	}
}

