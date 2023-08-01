package application;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import classi.Alert_cambiaForm;
import classi.Codice;
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
	private int numGiocatori;
	Alert_cambiaForm alert = new Alert_cambiaForm();
	// Event Listener on Button.onAction
	@FXML
	public void btnAvanti(ActionEvent event) {
		try {
			Codice codice = new Codice(txtCodice.getText());
			
			if(rdbGiocatori2.isSelected())
				numGiocatori = 2;
			else if(rdbGiocatori3.isSelected())
				numGiocatori = 3;
			else if(rdbGiocatori4.isSelected())
				numGiocatori = 4;
			else
				numGiocatori = 5;
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/FormCreaPartita2.fxml"));
			Parent root = loader.load();
			FormCreaPartita2Controller controller = loader.getController();
			controller.creaForm();
			System.out.println(codice.getCodice()+" "+numeroGiocatori);
			alert.passaAlForm("/application/FormCreaPartita2.fxml",event);
		}catch (IOException e) {
			alert.mostraErrore();
		}
	}
	@FXML
	public void btnVaiIndietro1(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
	}
	public int getNumGiocatori() {
		return numGiocatori;
	}
}