package connectFour;

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
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{

	private ConnectFourBoard _board;
	private Game _game;
	private GameHistory _history;

	private static final String _file = System.getProperty("user.dir") + "/.Resources/history.ser";

	private GUIConnectFourBoard _GUI;
	private Stage _stage;
	private AnimationTimer _timer;

	private boolean _playersTurn = true;
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
				handleMouseClick(event);
				_GUI.update();
				// TODO this is just a test
				//_board.getSquares().get(0).setAsFilled("P1");
				//_board.getSquares().get(1).setAsFilled("P2");
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
*/
	protected void AITurn() {
		Square toGo = _board.getPossibleMoves().get(0);
		// set square as filled
		String player = "";
		if(_turn % 2 == 0){
			player = "P2";
			toGo.setAsFilled("P2");
		}
		else{
			player = "P1";
			toGo.setAsFilled("P1");
		}

		// store move
		_game.addMove(new Move(toGo, player, _turn));

		// remove from possible squares
		_board.updatePossibleMoves();

		// increment move number
		_turn++;

		_playersTurn = true;
		
		_GUI.update();
		
		checkResult();
	}

	//TODO put this in GUI class
	protected void handleMouseClick(Event event) {
		if(_playersTurn){
			MouseEvent me = (MouseEvent) event;
			double horizontal = me.getSceneX();
			double vertical = me.getSceneY();

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

				checkResult();
			}
		}

	}

	private void checkResult(){
		// check if someone has won
		if(_board.getResult().equals("P1W") || _board.getResult().equals("P2W") || _turn > 9){
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

	private void initialize() {
		_history = FileManipulator.readHistoryFromFile(_file);
		//_history = new GameHistory();
		_board = new ConnectFourBoard();
		_game = new Game();
	}

	private static void clearData(){
		GameHistory gh = new GameHistory();

		FileManipulator.writeHistoryTofile(gh, _file);

		System.out.println("Game history cleared");
	}
}

