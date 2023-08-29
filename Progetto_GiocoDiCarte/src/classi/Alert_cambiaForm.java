package classi;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
	public void mostraErrore(String setContent, String setHeader) {
		AlertType message = AlertType.ERROR;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText(setContent);
		alert.getDialogPane().setHeaderText(setHeader);
		alert.showAndWait();
	}
	public void mostraInformazione(Gare g, String setContent, String[] setHeader,String codiceUtente) {

		AlertType message = AlertType.INFORMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText(setContent);
		boolean nuovoTorneo = g.cercaCodice(codiceUtente);
		if(nuovoTorneo)
		{
			alert.getDialogPane().setContentText(setHeader[0]);
		}
		else
		{
			alert.getDialogPane().setContentText(setHeader[1]);
		}
		alert.showAndWait();
	}
	public void mostraInformazione(String setContent, String setHeader) 
	{
		AlertType message = AlertType.INFORMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText(setHeader);
		alert.getDialogPane().setContentText(setContent);
		alert.showAndWait();
	}
	public boolean chiediConferma(String setContent, String setHeader) {
		AlertType message = AlertType.CONFIRMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText(setHeader);
		alert.getDialogPane().setContentText(setContent);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK)
			return true;
		else
			return false;
	}
}