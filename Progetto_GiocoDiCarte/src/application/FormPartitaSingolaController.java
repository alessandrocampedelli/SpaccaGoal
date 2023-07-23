package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

import classi.Amministratore;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class FormPartitaSingolaController 

{
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML
	private TextField txtCodicePartitaSingola;

	// Event Listener on Button.onAction
	@FXML
	public void btnGiocaPartitaSingola(ActionEvent event) throws IOException
	{
		String codiceInseritoUtente = txtCodicePartitaSingola.getText();
		if() 
		{			
			//Controllare se era un vecchio codice o un codice appena inserito (variabile booleana true or false)
			//per successiva stampa nuovo codice o partita sospesa
			
			AlertType message = AlertType.CONFIRMATION;
			Alert alert = new Alert(message, "");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(stage);
			alert.getDialogPane().setHeaderText("AVVIO IN CORSO");
			if()
			{
				alert.getDialogPane().setContentText("Codice inserito corretto! E' in corso l'avvio di una nuova partita...");
			}
			else
			{
				alert.getDialogPane().setContentText("Codice inserito corretto! E' in corso il riavvio della partita non terminata...");
			}
			alert.showAndWait();
			
			/*root = FXMLLoader.load(getClass().getResource("FormGiocaPartita.fxml"));
		    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		    scene = new Scene(root);
		    stage.setScene(scene);
		    stage.show();*/
		}
		else 
		{
			AlertType message = AlertType.ERROR;
			Alert alert = new Alert(message, "");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(stage);
			alert.getDialogPane().setContentText("Codice della partita singola errata!");
			alert.getDialogPane().setHeaderText("CODICE PARTITA ERRATO");
			alert.showAndWait();
		}	
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnTornaFormModalitaGiocatore(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("FormModalitaGiocatore.fxml"));
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
}