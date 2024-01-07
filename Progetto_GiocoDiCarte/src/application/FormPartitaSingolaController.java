package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import classi.Alert_cambiaForm;
import classi.Gare;

//classe per inserire il codice di una partita singola per avviarla
public class FormPartitaSingolaController 
{
	//l'oggetto "TextField" con il codice che verrà inserito dall'utente della partita
	@FXML
	private TextField txtCodicePartitaSingola;
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	//creo un oggetto di classe "Gare" che permetterà di ricercare il codice della partita in tutte le gare presenti nel programma
	Gare gare = new Gare();
	
	//il bottone che mi permette di cercare il codice della partita singola
	@FXML
	public void btnGiocaPartitaSingola(ActionEvent event) throws IOException
	{
		try
		{
			//controllo se l'utente non avesse inserito nessun carattere
			if(txtCodicePartitaSingola.getText().trim().equals("")) 
			{
				txtCodicePartitaSingola.clear();
				throw new IOException();
			}
			//il codice inserito dall'utente nella "TextField"
			String codiceUtente = txtCodicePartitaSingola.getText().trim();
			//controllo se il codice inserito dall'utente esiste ed è legato ad una partita
			if(gare.cercaCodice(codiceUtente, 'p')) 
			{			
				//stampo l'informazione all'utente che il codice che ha inserito è corretto
				alert.mostraInformazione("Codice inserito corretto! E' in corso l'avvio di una partita...","AVVIO PARTITA IN CORSO");
				//classe che permette di passare il codice della partita inserito dall'utente al form "iniziaPartita" 
				FXMLLoader loader = new FXMLLoader(getClass().getResource("FormIniziaPartita.fxml"));
				loader.load();
				FormIniziaPartitaController form = loader.getController();
				//copiaInfo è un metodo del form "iniziaPartita"
				form.copiaInfo(codiceUtente);
				//passo al form inzia partita
				alert.passaAlForm("/application/FormIniziaPartita.fxml", event);
			}
			else 
			{
				//mostro all'utente che il codice della partita inserito non è corretto
				alert.mostraErrore("Codice della partita singola errata!","CODICE PARTITA ERRATO");
			}
		}
		catch(IOException e) 
		{
			//mostro all'utente che il codice della partita inserito non è corretto
			alert.mostraErrore("Codice della partita singola errata!","CODICE PARTITA ERRATO");
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