package application;

import javafx.fxml.FXML;
import classi.Amministratore;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FormModalitaAmministratoreController {
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;

	private Stage stage;
	private Scene scene;
	private Parent root;
	// Event Listener on Button.onAction
	@FXML
	public void btnAccedi(ActionEvent event) throws IOException{
		Amministratore admin = new Amministratore();
		if(admin.getUserName().equals(txtUsername.getText()) && admin.getPassword().equals(txtPassword.getText())) {
			root = FXMLLoader.load(getClass().getResource("FormModalitaAdminMenu.fxml"));
		    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		    scene = new Scene(root);
		    stage.setScene(scene);
		    stage.show();
		}
		else {
			System.out.println("ERRORE");
		}
	}
}