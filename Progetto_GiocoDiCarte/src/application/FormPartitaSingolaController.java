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
import classi.Gare;

public class FormPartitaSingolaController 

{
	@FXML
	private TextField txtCodicePartitaSingola;
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Gare gare = new Gare();
	// Event Listener on Button.onAction
	@FXML
	public void btnGiocaPartitaSingola(ActionEvent event) throws IOException
	{
		String codiceInseritoUtente = txtCodicePartitaSingola.getText();
		//controllo se il codice inserito dall'utente è funzionante
		if(gare.cercaCodice(codiceInseritoUtente)[0]) 
		{			
			String[] output = new String[] {"Codice inserito corretto! E' in corso l'avvio di una nuova partita...",
					"Codice inserito corretto! E' in corso il riavvio della partita non terminata..."};
			alert.mostraInformazione(gare, "AVVIO PARTITA IN CORSO", output, codiceInseritoUtente);
			alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
		}
		else 
		{
			alert.mostraErrore("Codice della partita singola errata!","CODICE PARTITA ERRATO");
		}
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnTornaFormModalitaGiocatore(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}