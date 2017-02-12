package connectFour.gui;

import java.util.ArrayList;

import connectFour.history.Game;
import connectFour.model.ConnectFourBoard;
import connectFour.model.Square;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GUIConnectFourBoard {
	private ConnectFourBoard _board;
	private Pane _app = new Pane();
	private Pane _gamePane = new Pane();

	private ArrayList<Node> _squares = new ArrayList<>();

	public Pane getPane(){
		return _app;
	}

	public GUIConnectFourBoard(ConnectFourBoard board){
		initialize(board);
	}

	public void update(){
		for(int i = 0; i < _board.getSquares().size(); i++){
			Square s = _board.getSquares().get(i);
			if(s.isFilled()){
				Rectangle r = (Rectangle)_squares.get(i);

				if(s.filledBy().equals("P1")){
					r.setFill(Color.BLUE);
				}
				else if(s.filledBy().equals("P2")){
					r.setFill(Color.RED);
				}
			}
		}
	}

	private void initialize(ConnectFourBoard board){
		_board = board;


		Rectangle rect = new Rectangle(700,600, Color.GREY);
		_gamePane.getChildren().add(rect);

		// TODO needs to be 6x7 not 3x3
		for(int y = 6; y > 0; y--){
			for(int x = 0; x < 7; x++){
				_squares.add(createEntity(x*100 + 10, y*100 - 90, 80, 80, Color.WHITE));
			}
		}

		_app.getChildren().add(_gamePane);
	}

	private Node createEntity(int x, int y, int w, int h, Color colour) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(colour);

		_gamePane.getChildren().add(entity);
		return entity;
	}

}
