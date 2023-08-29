package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
		try
		{
			if(txtCodiceTorneo.getText().trim().equals("")) 
			{
				txtCodiceTorneo.clear();
				throw new IOException();
			}
			
			String codiceInseritoUtente = txtCodiceTorneo.getText();
			codiceInseritoUtente = codiceInseritoUtente.trim();
			//controllo se il codice inserito dall'utente è funzionante
			if(gare.cercaCodice(codiceInseritoUtente, 't')) 
			{		
				String[] output = new String[] {"Codice inserito corretto! E' in corso l'avvio di un nuovo torneo...",
						"Codice inserito corretto! E' in corso il riavvio del torneo non terminato..."};
				alert.mostraInformazione(gare, "AVVIO TORNEO IN CORSO", output, codiceInseritoUtente);
				alert.passaAlForm("/application/FormTabelloneTorneo.fxml", event);
			}
			else 
			{
				alert.mostraErrore("Codice del torneo errato!","CODICE TORNEO ERRATO");
			}
		}
		catch(IOException e) 
		{
			alert.mostraErrore("Codice del torneo errato!","CODICE TORNEO ERRATO");
		}
	}
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void btnTornaFormModalitaGiocatore(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}