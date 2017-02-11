package connectFour;

import java.util.ArrayList;

import connectFour.gui.GUIConnectFourBoard;
import connectFour.history.Game;
import connectFour.history.Move;
import connectFour.history.GameHistory;
import connectFour.model.ConnectFourBoard;
import connectFour.model.Square;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();

		_GUI = new GUIConnectFourBoard(_board);
		
		Scene scene = new Scene(_GUI.getPane());
		scene.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				//handleMouseClick(event);
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
					//AITurn();
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

			Square toGo = moveToDo.getSquare();
			System.out.println(moveToDo.getRank());

			int num = showAITurn(toGo);

			// store whether player went first or second
			// set square as filled
			Square square = _board.getSquares().get(num);
			if(player == "playerOne"){
				square.setAsFilled("Player One");
			}
			else{
				square.setAsFilled("Player Two");
			}

			// store move
			_game.addMove(new Move(square, player, _turn));

			// remove from possible squares
			//_board.removeFromPossibleMoves(square);
			_board.updatePossibleMoves();

			// increment move number
			_turn++;

			handleGameEnd();

			_playersTurn = true;
		}
	}

	// TODO put this in GUI class
	private int showAITurn(Square toGo){
		Integer num = -1;

		// first row
		Rectangle r = new Rectangle();
		if(toGo.toString().equals("0,0")){
			r = (Rectangle)_squares.get(0);
			num = 0;
		}

		else if(toGo.toString().equals("0,1")){
			r = (Rectangle)_squares.get(1);
			num = 1;
		}

		else if(toGo.toString().equals("0,2")){
			r = (Rectangle)_squares.get(2);
			num = 2;
		}

		else if(toGo.toString().equals("1,0")){
			r = (Rectangle)_squares.get(3);
			num = 3;
		}

		else if(toGo.toString().equals("1,1")){
			r = (Rectangle)_squares.get(4);
			num = 4;
		}

		else if(toGo.toString().equals("1,2")){
			r = (Rectangle)_squares.get(5);
			num = 5;
		}

		// third row
		else if(toGo.toString().equals("2,0")){
			r = (Rectangle)_squares.get(6);
			num = 6;
		}

		else if(toGo.toString().equals("2,1")){
			r = (Rectangle)_squares.get(7);
			num = 7;
		}

		else if(toGo.toString().equals("2,2")){
			r = (Rectangle)_squares.get(8);
			num = 8;
		}

		// show AI move
		r.setFill(Color.BLUE);

		return num;
	}

	private void handleGameEnd(){
		// check if someone has won
		if(_board.getResult().equals("Player 1 Won") || _board.getResult().equals("Player 2 Won") || _turn > 9){
			_board.endGame();
			// get result of game
			String result = _board.getResult();

			// set result
			_game.setResult(result);

			// record game
			_history.addGame(_game);

			// write history to file
			FileManipulator.writeHistoryTofile(_history, _file);

			// TODO end GUI too
			_stage.close();
		}
	}

	//TODO put this in GUI class
	protected void handleMouseClick(Event event) {
		// TODO
		if(_playersTurn){
			MouseEvent me = (MouseEvent) event;
			double horizontal = me.getSceneX();
			double vertical = me.getSceneY();

			Integer num = -1;

			// first row
			Rectangle r = new Rectangle();
			if(horizontal <= 200 && vertical <= 200){
				r = (Rectangle)_squares.get(0);
				num = 0;
			}

			else if(horizontal <= 400 && vertical <= 200){
				r = (Rectangle)_squares.get(1);
				num = 1;
			}

			else if(horizontal <= 600 && vertical <= 200){
				r = (Rectangle)_squares.get(2);
				num = 2;
			}

			else if(horizontal <= 200 && vertical <= 400){
				r = (Rectangle)_squares.get(3);
				num = 3;
			}

			else if(horizontal <= 400 && vertical <= 400){
				r = (Rectangle)_squares.get(4);
				num = 4;
			}

			else if(horizontal <= 600 && vertical <= 400){
				r = (Rectangle)_squares.get(5);
				num = 5;
			}

			// third row
			else if(horizontal <= 200 && vertical <= 600){
				r = (Rectangle)_squares.get(6);
				num = 6;
			}

			else if(horizontal <= 400 && vertical <= 600){
				r = (Rectangle)_squares.get(7);
				num = 7;
			}

			else if(horizontal <= 600 && vertical <= 600){
				r = (Rectangle)_squares.get(8);
				num = 8;
			}
			if(r.getFill().equals(Color.WHITE)){
				// show where player has gone
				r.setFill(Color.RED);

				// store whether player went first or second
				// set square as filled
				Square square = _board.getSquares().get(num);
				String player = "";
				if(_turn % 2 == 1){
					player = "playerOne";
					square.setAsFilled("Player One");
				}
				else{
					player = "playerTwo";
					square.setAsFilled("Player Two");
				}

				// store move
				_game.addMove(new Move(square, player, _turn));

				// remove from possible squares
				_board.updatePossibleMoves();

				// increment move number
				_turn++;

				// check if someone has won
				if(_board.getResult().equals("Player 1 Won") || _board.getResult().equals("Player 2 Won") || _turn > 9){
					_board.endGame();
					// get result of game
					String result = _board.getResult();

					// set result
					_game.setResult(result);

					// record game
					_history.addGame(_game);

					// write history to file
					FileManipulator.writeHistoryTofile(_history, _file);

					// TODO end GUI too
					_stage.close();
				}

				_playersTurn = false;
			}
		}

	}
*/
	private void initialize() {
		//_history = FileManipulator.readHistoryFromFile(_file);
		_board = new ConnectFourBoard();
		_game = new Game();
	}

	private static void clearData(){
		GameHistory gh = new GameHistory();

		FileManipulator.writeHistoryTofile(gh, _file);

		System.out.println("Game history cleared");
	}
}

