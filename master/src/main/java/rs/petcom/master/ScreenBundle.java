package rs.petcom.master;

import javafx.scene.layout.BorderPane;

public class ScreenBundle {
	
	private BorderPane mainPane;
	private ControlledScreen controller;
	
	public ScreenBundle(BorderPane mainPane, ControlledScreen controller){
		this.mainPane = mainPane;
		this.controller= controller;
	}

	public BorderPane getMainPane() {
		return mainPane;
	}

	public void setMainPane(BorderPane mainPane) {
		this.mainPane = mainPane;
	}

	public ControlledScreen getController() {
		return controller;
	}

	public void setController(ControlledScreen controller) {
		this.controller = controller;
	}

}
