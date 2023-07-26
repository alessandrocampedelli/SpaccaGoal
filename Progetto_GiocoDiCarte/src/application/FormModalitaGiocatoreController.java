package application;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import classi.Alert_cambiaForm;
public class FormModalitaGiocatoreController
{
	Alert_cambiaForm alert = new Alert_cambiaForm();
	
	// Event Listener on Button.onAction
	@FXML
	public void btnTornaFormPrincipale(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnPartitaSingola(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPartitaSingola.fxml", event);
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnTorneo(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormTorneo.fxml", event);
	}
}
