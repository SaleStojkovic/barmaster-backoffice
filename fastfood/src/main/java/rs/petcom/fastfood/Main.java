package rs.petcom.fastfood;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Log;

public class Main extends Application {
	
	private ScreensController screensController = null;
	StackPane root = new StackPane();
	
	@Override
	public void start(Stage primaryStage) {
		
		Locale.setDefault(new Locale("sr"));
		ResourceBundle bundle = ResourceBundle.getBundle("locale");
		screensController = new ScreensController(ScreensController.FASTFOOD);
		screensController.setRoot(root);
		screensController.setBundle(bundle);
		
		screensController.getScreen(ScreensController.SCREEN_FASTFOOD, root, "");
		screensController.getScreen(ScreensController.SCREEN_LOGIN, root,"");
		
		screensController.loadDialog(ScreensController.SCREEN_PAYMENT_METHOD_SELECTION);
		
		screensController.loadDialog(ScreensController.SCREEN_AUTHORISATION);
		
		try {			
			Scene scene = new Scene(root);
			
			primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			primaryStage.setFullScreen(screensController.getSettings().isFullscreen());
			
			primaryStage.setTitle("Rmaster " + Main.class.getPackage().getImplementationVersion());
			
			/*
			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();

			primaryStage.setX(bounds.getMinX());
			primaryStage.setY(bounds.getMinY());
			primaryStage.setWidth(bounds.getWidth());
			primaryStage.setHeight(bounds.getHeight());
			*/
			/*
			primaryStage.setMaximized(true);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			*/
			primaryStage.setScene(scene);
			
			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(600);
			
			
			
			
			primaryStage.setTitle("Rmaster");
			primaryStage.getIcons().add(new Image("rs/petcom/master/img/R.jpg"));
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		        	  if (screensController.getUser() != null)
			              Log.logOut(screensController.getUser().getUserID(), 
			            		  	 screensController.getPointOfSale().getPointOfSaleID(), 
			            		     screensController.getSettings().getConnection());
		          }
		      });  			
		} catch(Exception e) {
			Log.writeLog(screensController.getUser(), "Error: " + e.getMessage(), 
						 screensController.getSettings().getConnection());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
