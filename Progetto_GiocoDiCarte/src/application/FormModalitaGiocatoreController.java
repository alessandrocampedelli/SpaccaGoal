package application;

import javafx.fxml.FXML;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import classi.Alert_cambiaForm;

public class FormModalitaGiocatoreController
{
	Alert_cambiaForm alert = new Alert_cambiaForm();
	@FXML
	private Button partita = new Button();
	@FXML
	private Button torneo = new Button();
	@FXML
	public void coloraBottoneP(MouseEvent event) {
		partita.setStyle("-fx-background-color: white;-fx-border-color: transparent; -fx-border-width: 0");
	}
	@FXML
	public void pulisciBottoneP(MouseEvent event) {
		partita.setStyle("-fx-background-color: 255,0,0;-fx-border-color: black; -fx-border-width: 2");
	}
	@FXML
	public void coloraBottoneT(MouseEvent event) {
		torneo.setStyle("-fx-background-color: white;-fx-border-color: transparent; -fx-border-width: 0");
	}
	@FXML
	public void pulisciBottoneT(MouseEvent event) {
		torneo.setStyle("-fx-background-color: 255,0,0;-fx-border-color: black; -fx-border-width: 2");
	}
	@FXML
	public void btnPartitaSingola(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPartitaSingola.fxml", event);
	}

	@FXML
	public void btnTorneo(ActionEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormTorneo.fxml", event);
	}

	@FXML
	public void btnTornaModalitaPrincipale(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
}