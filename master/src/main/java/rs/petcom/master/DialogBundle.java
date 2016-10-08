package rs.petcom.master;

import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rs.petcom.master.dal.Settings;

public class DialogBundle {
	Stage stage;
	DialogController controller;
	Settings settings;
	
	public DialogBundle(String resource, ResourceBundle bundle, Settings settings){
		this.settings = settings;
		stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		try {
			BorderPane loadedScreen = null;
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource),bundle);
			loadedScreen = (BorderPane) fxmlLoader.load();
			controller = fxmlLoader.getController();
			controller.setModalResult(ScreensController.MODAL_RESULT_CANCEL);
			stage.setScene(new Scene(loadedScreen));
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					controller.setModalResult(ScreensController.MODAL_RESULT_CANCEL);
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public HashMap<String, Object> getParameters() {
		return controller.getParameter();
	}
	public void setParameters(HashMap<String, Object> parameters) {
		controller.setParameter(parameters);
	}
	public int getModalResult(){
		return controller.getModalResult();
	}
	
	public int showAndWait(){
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.setFullScreen(settings.isFullscreen());
		stage.showAndWait();	
		stage.hide();
		return controller.getModalResult();
	}
}
