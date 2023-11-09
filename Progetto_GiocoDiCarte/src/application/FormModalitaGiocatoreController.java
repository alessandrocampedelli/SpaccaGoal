package application;

import javafx.fxml.FXML;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import classi.Alert_cambiaForm;

public class FormModalitaGiocatoreController
{
	Alert_cambiaForm alert = new Alert_cambiaForm();

	@FXML
	public void btnPartitaSingola(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPartitaSingola.fxml", event);
	}

	@FXML
	public void btnTorneo(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormTorneo.fxml", event);
	}

	@FXML
	public void btnTornaModalitaPrincipale(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
}