package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

//classe Main che viene eseguita per avviare il programma
public class Main extends Application 
{	
	@Override
	public void start(Stage primaryStage) 
	{
		//con un blocco try/catch controllo se non ci sono errori nell'apertura del form
		try 
		{
			//apro il form principale 
			AnchorPane root = FXMLLoader.load(getClass().getResource("FormPrincipale.fxml"));
			Scene scene = new Scene(root);
			//aggiungo lo stile css creato esternamente al form
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//fisso la stage in primo piano sullo schermo
			primaryStage.setAlwaysOnTop(true);
			//determino il tipo di form scelto, abbiamo scelto un form in cui non è possibile modificare le dimensioni prescelte
			primaryStage.initStyle(StageStyle.UTILITY);
			primaryStage.setResizable(false);
			//mostro all'utente il form principale
			primaryStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	//metodo che permette di avviare il form
	public static void main(String[] args) 
	{
		launch(args);
	}
}