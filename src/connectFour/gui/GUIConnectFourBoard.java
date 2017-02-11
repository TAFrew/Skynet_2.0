package connectFour.gui;

import java.util.ArrayList;

import connectFour.history.Game;
import connectFour.model.ConnectFourBoard;
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
		
	}
	
	private void initialize(ConnectFourBoard board){
		_board = board;
		

		Rectangle rect = new Rectangle(700,600, Color.GREY);
		_gamePane.getChildren().add(rect);

		// TODO needs to be 6x7 not 3x3
		for(int x = 0; x < 7; x++){
			for(int y = 0; y < 6; y++){
				_squares.add(createEntity(x*100 + 10, y*100 + 10, 80, 80, Color.WHITE));
			}
		}
		
		_app.getChildren().add(_gamePane);
		/*_squares.add(createEntity(10, 10, 180, 180, Color.WHITE));
		_squares.add(createEntity(210, 10, 180, 180, Color.WHITE));
		_squares.add(createEntity(410, 10, 180, 180, Color.WHITE));
		_squares.add(createEntity(10, 210, 180, 180, Color.WHITE));
		_squares.add(createEntity(210, 210, 180, 180, Color.WHITE));
		_squares.add(createEntity(410, 210, 180, 180, Color.WHITE));
		_squares.add(createEntity(10, 410, 180, 180, Color.WHITE));
		_squares.add(createEntity(210, 410, 180, 180, Color.WHITE));
		_squares.add(createEntity(410, 410, 180, 180, Color.WHITE));*/
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
