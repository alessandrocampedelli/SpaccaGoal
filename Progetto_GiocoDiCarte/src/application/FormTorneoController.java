package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import java.io.IOException;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import classi.Gare;

//classe per inserire il codice di un torneo per avviarlo
public class FormTorneoController 
{
	//l'oggetto "TextField" con il codice che verrà inserito dall'utente del torneo
	@FXML
	private TextField txtCodiceTorneo;
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	//creo un oggetto di classe "Gare" che permetterà di ricercare il codice della partita in tutte le gare presenti nel programma
	Gare gare = new Gare();

	//il bottone che mi permette di cercare il codice del torneo
	@FXML
	public void btnGiocaTorneo(ActionEvent event) throws IOException
	{
		try
		{
			//controllo se l'utente non avesse inserito nessun carattere
			if(txtCodiceTorneo.getText().trim().equals("")) 
			{
				txtCodiceTorneo.clear();
				throw new IOException();
			}
			//il codice inserito dall'utente nella "TextField"
			String codiceUtente = txtCodiceTorneo.getText();
			//controllo se il codice inserito dall'utente esiste ed è legato ad un torneo
			if(gare.cercaCodice(codiceUtente, 't')) 
			{	
				//stampo l'informazione all'utente che il codice che ha inserito è corretto
				alert.mostraInformazione("Codice inserito corretto! E' in corso l'avvio di un nuovo torneo...","AVVIO TORNEO IN CORSO");
				//classe che permette di passare il codice della partita inserito dall'utente al form "tabelloneTorneo" 
				FXMLLoader loader = new FXMLLoader(getClass().getResource("FormTabelloneTorneo.fxml"));
				loader.load();
				FormTabelloneTorneoController form = loader.getController();
				//copiaInfo è un metodo del form "tabelloneTorneo"
				form.copiaInfo(codiceUtente);
				//passo al form tabellone torneo
				alert.passaAlForm("/application/FormTabelloneTorneo.fxml", event);
			}
			else 
			{
				//mostro all'utente che il codice del torneo inserito non è corretto
				alert.mostraErrore("Codice del torneo errato!","CODICE TORNEO ERRATO");
			}
		}
		catch(IOException e) 
		{
			//mostro all'utente che il codice del torneo inserito non è corretto
			alert.mostraErrore("Codice del torneo errato!","CODICE TORNEO ERRATO");
		}
	}

	//metodo che viene scatenato al click della freccia indietro sull'interfaccia grafica
	@FXML
	public void btnTornaFormModalitaGiocatore(MouseEvent event) throws IOException
	{
		//passo al form di modalità giocatore
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}