package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

public class FormModalitaGiocatoreController
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// Event Listener on Button.onAction
	@FXML
	public void btnTornaFormPrincipale(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("FormPrincipale.fxml"));
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnPartitaSingola(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("FormPartitaSingola.fxml"));
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnTorneo(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("FormTorneo.fxml"));
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
}
