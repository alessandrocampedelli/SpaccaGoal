package application;

import javafx.fxml.FXML;
import classi.Alert_cambiaForm;
import classi.Amministratore;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;

//classe che permette l'inserimento delle credenziali dell'amministratore
public class FormModalitaAmministratoreController 
{
	//le "TextField" e "PasswordField" in cui andrà inserito l'username e la password dell'amministratore
	@FXML
	private TextField txtUsername;
	
	@FXML
	private PasswordField txtPassword;
	
	//il bottone del click che controllerà se le credenziali sono corrette oppure no
	@FXML 
	private Button btnAccedi = new Button();
	
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();

	//il bottone "accedi" utile per controllare se le credenziali inserite sono corrette oppure no
	@FXML
	public void accedi(ActionEvent event) throws IOException
	{
		Amministratore admin = new Amministratore();
		//tramite il metodo controllo se le credenziali inserite (username e password) sono le stesse dell'oggetto "Amministratore"
		if(admin.getUserName().equals(txtUsername.getText()) && admin.getPassword().equals(txtPassword.getText())) 
		{
			//passo al form di modalità admin menu
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml", event);
		}
		else 
		{
			//mando un errore all'utente che le credenziali inserite sono sbagliate
			alert.mostraErrore("Username e/o password errati","ERRORE");
		}
	}
	
	//metodo che viene scatenato al click della freccia indietro sull'interfaccia grafica
	public void btnTornaFormPrincipale(MouseEvent event) throws IOException
	{
		//passo al form modalità principale
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
}