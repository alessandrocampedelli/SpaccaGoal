package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import classi.Alert_cambiaForm;
import classi.Amministratore;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import classi.Alert_cambiaForm;
public class FormModalitaAmministratoreController {
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	Alert_cambiaForm alert = new Alert_cambiaForm();
	// Event Listener on Button.onAction
	@FXML
	public void btnAccedi(ActionEvent event) throws IOException{
		Amministratore admin = new Amministratore();
		if(admin.getUserName().equals(txtUsername.getText()) && admin.getPassword().equals(txtPassword.getText())) {
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml", event);
		}
		else {
			alert.mostraErrore();
		}
	}
	public void btnFormPrincipale(ActionEvent event){
		try {
			alert.passaAlForm("/application/FormPrincipale.fxml",event);
		}catch (IOException e) {
			alert.mostraErrore();
		}
	}
}