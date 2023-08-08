package application;
import javafx.fxml.FXML;


import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.ProgressBar;

public class FormIniziaPartitaController implements Initializable{
	@FXML
	private ProgressBar progressBarPartita;
	@FXML
	private Label txtGiocatore1;
	@FXML
	private Label txtGiocatore2;
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		progressBarPartita.setStyle("-fx-accent: #00FF00;");
	}
	public void entraMouse() throws InterruptedException{
		for(double progress = 0.0; progress < 1.0; progress += 0.1) {
			Thread.sleep(1000);
			progressBarPartita.setProgress(progress);
		}
	}
	
}
