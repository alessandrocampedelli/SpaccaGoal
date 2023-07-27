package application;

import javafx.fxml.FXML;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import classi.Alert_cambiaForm;

public class FormModalitaGiocatoreController
{
	Alert_cambiaForm alert = new Alert_cambiaForm();

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
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void btnTornaModalitaPrincipale(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
}