package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class provaFormController {
	@FXML
	private TextField txtParola;
	
	@FXML
	public void btnSalva(ActionEvent event) {
		Stage mainWindow = (Stage) txtParola.getScene().getWindow();
		String title = txtParola.getText();
		mainWindow.setTitle(title);
		
	}
}
