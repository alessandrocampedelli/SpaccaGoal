package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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
			primaryStage.setTitle("GIOCO");
			primaryStage.setScene(scene);
			// Ottieni le dimensioni dello schermo primario
	        Screen screen = Screen.getPrimary();
	        double screenWidth = screen.getBounds().getWidth();
	        double screenHeight = screen.getBounds().getHeight();
	        // Imposta le dimensioni del contenuto uguale a quelle dello stage
	        root.setPrefWidth(screenWidth);
	        root.setPrefHeight(screenHeight);

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