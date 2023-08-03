package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.IOException;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import classi.Gare;

public class FormTorneoController {
	@FXML
	private TextField txtCodiceTorneo;
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Gare gare = new Gare();

	// Event Listener on Button.onAction
	@FXML
	public void btnGiocaTorneo(ActionEvent event) throws IOException
	{
		String codiceInseritoUtente = txtCodiceTorneo.getText();
		//controllo se il codice inserito dall'utente è funzionante
		if(gare.cercaCodice(codiceInseritoUtente)) 
		{			
			alert.mostraConfermaCodiceTorneo(gare, codiceInseritoUtente);
			alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
		}
		else 
		{
			alert.mostraErroreCodiceTorneo();
		}
	}
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void btnTornaFormModalitaGiocatore(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}