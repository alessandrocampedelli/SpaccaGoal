package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application 
{
	Stage stage;
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			AnchorPane root = FXMLLoader.load(getClass().getResource("FormPrincipale.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UTILITY);
			primaryStage.setResizable(false);
			primaryStage.show();
			/*
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		        @Override
		        public void handle(WindowEvent event) {
		            // Mostra un avviso di conferma prima di chiudere la finestra
		            event.consume();
		            //Thread.currentThread().interrupt();
		        }
		    });*/
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args) 
	{
		launch(args);
	}
}