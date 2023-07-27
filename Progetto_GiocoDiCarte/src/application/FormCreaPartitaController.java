package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import classi.Alert_cambiaForm;
public class FormCreaPartitaController {
	@FXML
	private TextField txtCodice;
	@FXML
	private RadioButton rdbGiocatori2;
	@FXML
	private ToggleGroup numeroGiocatori;
	@FXML
	private RadioButton rdbGiocatori3;
	@FXML
	private RadioButton rdbGiocatori4;
	@FXML
	private RadioButton rdbGiocatori5;
	Alert_cambiaForm alert = new Alert_cambiaForm();
	// Event Listener on Button.onAction
	@FXML
	public void btnAvanti(ActionEvent event) {
		try {
			alert.passaAlForm("/application/FormCreaPartita2.fxml",event);
		}catch (IOException e) {
			alert.mostraErrore();
		}
	}
	@FXML
	public void btnVaiIndietro1(ActionEvent event){
		try {
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
		}catch (IOException e) {
			alert.mostraErrore();
		}
	}
}
