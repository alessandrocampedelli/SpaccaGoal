package application;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import java.io.IOException;

import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;

import javafx.scene.input.MouseEvent;

public class FormTorneoController {
	@FXML
	private TextField txtCodiceTorneo;
	Alert_cambiaForm alert = new Alert_cambiaForm();

	// Event Listener on Button.onAction
	@FXML
	public void btnGiocaTorneo(ActionEvent event) 
	{
		//da fare
	}
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void btnTornaFormModalitaGiocatore(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}