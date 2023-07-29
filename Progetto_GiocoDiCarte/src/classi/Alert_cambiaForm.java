package classi;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert_cambiaForm {
	private Stage stage;
	private Scene scene;
	private Parent root;
	public void passaAlForm(String form, ActionEvent event)  throws IOException{
		root = FXMLLoader.load(getClass().getResource(form));
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	public void passaAlForm(String form, MouseEvent event)  throws IOException{
		root = FXMLLoader.load(getClass().getResource(form));
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	
	public void mostraErrore() {
		AlertType message = AlertType.INFORMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText("Errore di Input/Output");
		alert.getDialogPane().setHeaderText("ERRORE");
		alert.showAndWait();
	}
	
	public void mostraErroreAccessoAmministratore() {
		AlertType message = AlertType.ERROR;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText("Username e/o password errati!");
		alert.getDialogPane().setHeaderText("ERRORE");
		alert.showAndWait();
	}
	
	public void mostraErroreCodicePartitaSingola() {
		AlertType message = AlertType.ERROR;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText("Codice della partita singola errata!");
		alert.getDialogPane().setHeaderText("CODICE PARTITA ERRATO");
		alert.showAndWait();
	}
	
	public void mostraConfermaCodicePartitaSingola(Partite p, String codiceUtente) 
	{
		AlertType message = AlertType.CONFIRMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText("AVVIO IN CORSO");
		if(p.partitaNuovaRicominciata(codiceUtente))
		{
			alert.getDialogPane().setContentText("Codice inserito corretto! E' in corso l'avvio di una nuova partita...");
		}
		else
		{
			alert.getDialogPane().setContentText("Codice inserito corretto! E' in corso il riavvio della partita non terminata...");
		}
		alert.showAndWait();
	}
}
