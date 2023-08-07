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
	
	/*public FormIniziaPartitaController()
	{
		Thread t = new Thread();
		t.start();
	}*/
	
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		progressBarPartita.setStyle("-fx-accent: #00FF00;");

		for(double progress = 0.0; progress < 1; progress = progress + 0.1)
		{			
			try
			{
				System.out.println(progress);	
				progressBarPartita.setProgress(progress);
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
