package application;

import javafx.fxml.FXML;
import java.io.IOException;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;

public class FormModalitaAdminMenuController 
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	@FXML
	public void btnCreaNewPartita(ActionEvent event) 
	{
		try 
		{
			alert.passaAlForm("/application/FormCreaPartita.fxml",event);
		}
		catch (IOException e)
		{
			alert.mostraErrore("Si è verificato un errore!","ERRORE");
		}
	}
	@FXML
	public void btnCreaNewGiocatore(ActionEvent event) 
	{
		try 
		{
			alert.passaAlForm("/application/FormCreaGiocatore.fxml",event);
		}
		catch (IOException e)
		{
			alert.mostraErrore("Si è verificato un errore!","ERRORE");
		}
	}
	@FXML
	public void btnCreaNewTorneo(ActionEvent event) 
	{
		try 
		{
			alert.passaAlForm("/application/FormCreaTorneo.fxml",event);
		}
		catch (IOException e)
		{
			alert.mostraErrore("Si è verificato un errore!","ERRORE");
		}
	}
	
	@FXML
	public void btnEliminaEvento(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormEliminaEvento.fxml", event);
	}

	@FXML
	public void btnLogout(ActionEvent event) 
	{
		try 
		{
			alert.passaAlForm("/application/FormPrincipale.fxml",event);
		}
		catch (IOException e)
		{
			alert.mostraErrore("Si è verificato un errore!","ERRORE");
		}
	}
}