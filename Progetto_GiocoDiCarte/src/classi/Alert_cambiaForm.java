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
}
