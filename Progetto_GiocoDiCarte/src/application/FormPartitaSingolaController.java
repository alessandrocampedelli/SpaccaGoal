package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import classi.Alert_cambiaForm;
import classi.Partite;

public class FormPartitaSingolaController 

{
	@FXML
	private TextField txtCodicePartitaSingola;
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Partite partite = new Partite();
	// Event Listener on Button.onAction
	@FXML
	public void btnGiocaPartitaSingola(ActionEvent event) throws IOException
	{
		String codiceInseritoUtente = txtCodicePartitaSingola.getText();
		//controllo se il codice inserito dall'utente è funzionante
		if(partite.cercaCodice(codiceInseritoUtente)) 
		{			
			alert.mostraConfermaCodicePartitaSingola(partite, codiceInseritoUtente);
			alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
		}
		else 
		{
			alert.mostraErroreCodicePartitaSingola();
		}
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnTornaFormModalitaGiocatore(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}