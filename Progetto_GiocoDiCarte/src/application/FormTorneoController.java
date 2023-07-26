package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import classi.Alert_cambiaForm;
public class FormTorneoController 
{
	Alert_cambiaForm alert = new Alert_cambiaForm();
	
	@FXML
	private TextField txtCodiceTorneo;

	// Event Listener on Button.onAction
	@FXML
	public void btnGiocaTorneo(ActionEvent event) 
	{
		//da fare
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnTornaFormModalitaGiocatore(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}
