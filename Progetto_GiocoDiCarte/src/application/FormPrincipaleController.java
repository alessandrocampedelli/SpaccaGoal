package application;

import javafx.fxml.FXML;
import java.io.IOException;
import javafx.event.ActionEvent;
import classi.Alert_cambiaForm;

//classe del form principale che contiene i tre bottoni principali a cui può accedere l'utente
public class FormPrincipaleController 
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	
	//bottone per passare alla modalità ammistratore al suo click
	@FXML
	public void btnModalitaAmministratore(ActionEvent event) throws IOException
	{
		//passo al form di modalità amministratore
		alert.passaAlForm("/application/FormModalitaAmministratore.fxml",event);
	}
	
	//bottone per passare alla modalità giocatore al suo click
	@FXML
	public void btnModalitaGiocatore(ActionEvent event) throws IOException 
	{
		//passo al form di modalità giocatore
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml",event);
	}
	
	//bottone per passare alla visualizzazione della leaderboard al suo click
	@FXML
	public void btnVisualizzaLeaderboard(ActionEvent event) throws IOException
	{
		//passo al form di visualizzazione della leaderboard
		alert.passaAlForm("/application/FormLeaderboard.fxml",event);
	}
}