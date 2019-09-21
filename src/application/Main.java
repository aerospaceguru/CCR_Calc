package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Calculator.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);		
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("CCR Calculator");
			

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
