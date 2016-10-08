package rs.petcom.kitchenmonitor;
	
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import rs.petcom.master.ScreensController;


public class Main extends Application {
	
	private ScreensController screensController = null;
	private StackPane root = new StackPane();
	
	@Override
	public void start(Stage primaryStage) {
		Locale.setDefault(new Locale("sr"));
		ResourceBundle bundle = ResourceBundle.getBundle("locale");
		screensController = new ScreensController(ScreensController.KITCHEN_MONITOR);
		screensController.setRoot(root);
		screensController.setBundle(bundle);
		
		screensController.getScreen(ScreensController.SCREEN_KITCHEN_MONITOR, root,"");
		
		try {
			Scene scene = new Scene(root,800,580);
			primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			primaryStage.setTitle("Rmaster " + Main.class.getPackage().getImplementationVersion());
			primaryStage.setScene(scene);
			//primaryStage.setMinWidth(800);
			//primaryStage.setMinHeight(600);
						
			primaryStage.getIcons().add(new Image("rs/petcom/master/img/R.jpg"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
