package classi;

import java.io.IOException;

import java.util.Optional;

import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class Alert_cambiaForm 
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	public Stage getStage() {
		return stage;
	}
	public void setStage(ActionEvent event) {
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	}
	public void setStage(MouseEvent event) {
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	}
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public Parent getRoot() {
		return root;
	}
	public void setRoot(String form) throws IOException {
		this.root = FXMLLoader.load(getClass().getResource(form));;
	}
	public void chiudiProgramma(String form) {
	}
	public void passaAlForm(String form, ActionEvent event)  throws IOException
	{
		try {
			if(((Node)event.getSource()).getScene().equals(null)){
				throw new NullPointerException();
			}
			setRoot(form);
		    setStage(event);
		    this.scene = new Scene(getRoot());
		    this.stage.setScene(this.scene);
		    this.stage.show();
		}catch(NullPointerException e) {
			event.consume();
		}
	}
	
	public void passaAlForm(String form, MouseEvent event)  throws IOException
	{
		try {
			if(((Node)event.getSource()).getScene().equals(null)){
				throw new NullPointerException();
			}
			setRoot(form);
		    setStage(event);
		    this.scene = new Scene(getRoot());
		    this.stage.setScene(this.scene);
		    this.stage.show();
		}catch(NullPointerException e) {
			event.consume();
		}
	}
	
	public void mostraErrore(String setContent, String setHeader) 
	{
		AlertType message = AlertType.ERROR;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText(setContent);
		alert.getDialogPane().setHeaderText(setHeader);
		alert.showAndWait();
	}
	
	public void mostraInformazione(Gare g, String setContent, String[] setHeader,String codiceUtente) 
	{
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
	public boolean chiediConferma(String setContent, String setHeader) 
	{
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