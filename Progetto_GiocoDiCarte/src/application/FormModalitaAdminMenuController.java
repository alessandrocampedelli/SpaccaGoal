package application;

import javafx.fxml.FXML;
import java.io.IOException;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;

//classe che permette la visualizzazione delle scelte da parte dell'amministratore
public class FormModalitaAdminMenuController 
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();

	//il bottone per creare una nuovo giocatore
	@FXML
	public void btnCreaNewGiocatore(ActionEvent event) throws IOException
	{
		//passo al form di creazione di un nuovo giocatore
		alert.passaAlForm("/application/FormCreaGiocatore.fxml",event);
	}

	//il bottone per creare una nuova partita
	@FXML
	public void btnCreaNewPartita(ActionEvent event) throws IOException
	{
		//passo al form di avvio di una partita singola
		alert.passaAlForm("/application/FormCreaPartita.fxml",event);
	}

	//il bottone per creare un nuovo torneo
	@FXML
	public void btnCreaNewTorneo(ActionEvent event) throws IOException
	{
		//passo al form di avvio di un torneo
		alert.passaAlForm("/application/FormCreaTorneo.fxml",event);
	}

	//il bottone per eliminare un evento
	@FXML
	public void btnEliminaEvento(ActionEvent event) throws IOException
	{
		//passo al form di avvio di eliminazione evento
		alert.passaAlForm("/application/FormEliminaEvento.fxml", event);
	}

	//il bottone per effettuare il ritorno alla pagina principale
	@FXML
	public void btnLogout(ActionEvent event) throws IOException
	{
		//passo al form principale
		alert.passaAlForm("/application/FormPrincipale.fxml",event);
	}
}