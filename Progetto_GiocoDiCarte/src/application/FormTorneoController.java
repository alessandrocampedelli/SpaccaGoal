package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import java.io.IOException;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import classi.Gare;

//classe per inserire il codice di un torneo per avviare un torneo
public class FormTorneoController 
{
	//le "TextField" e "PasswordField" in cui andrà inserito l'username e la password dell'amministratore
	@FXML
	private TextField txtCodiceTorneo;
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Gare gare = new Gare();

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
				alert.mostraInformazione("Codice inserito corretto! E' in corso l'avvio di un nuovo torneo...","AVVIO TORNEO IN CORSO");
				//classe da cui partono i dati
				FXMLLoader loader = new FXMLLoader(getClass().getResource("FormTabelloneTorneo.fxml"));
				loader.load();
				FormTabelloneTorneoController form = loader.getController();
				form.copiaInfo(codiceInseritoUtente);
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

	//metodo che viene scatenato al click della freccia indietro sull'interfaccia grafica
	@FXML
	public void btnTornaFormModalitaGiocatore(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}