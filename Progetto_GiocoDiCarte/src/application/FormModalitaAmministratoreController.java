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

public class FormModalitaAmministratoreController 
{
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	@FXML 
	private Button btnAccedi = new Button();
	Alert_cambiaForm alert = new Alert_cambiaForm();

	@FXML
	public void accedi(ActionEvent event) throws IOException
	{
		Amministratore admin = new Amministratore();
		if(admin.getUserName().equals(txtUsername.getText()) && admin.getPassword().equals(txtPassword.getText())) 
		{
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml", event);
		}
		else 
		{
			alert.mostraErrore("Username e/o password errati","ERRORE");
		}
	}
	@FXML void coloraBottone(MouseEvent event) {
		btnAccedi.setStyle("-fx-background-color: black;-fx-border-color: transparent; -fx-border-width: 0; -fx-text-fill: white");
	}
	@FXML void pulisciBottone(MouseEvent event) {
		btnAccedi.setStyle("-fx-background-color: white;-fx-border-color: black; -fx-border-width: 2");
	}
	public void btnTornaFormPrincipale(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
}