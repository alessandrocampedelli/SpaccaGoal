package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import classi.Gare;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class FormIniziaPartitaController 
{
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Gare gare = new Gare();
	@FXML
	private Label lblGiocatore1;
	@FXML
	private Label lblGiocatore2;
	
	public static void main(String[] args)
	{
		
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnAvviaPartita(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormPartitaSingola.fxml"));
		Parent root = loader.load();
		FormPartitaSingolaController formPartitaSingola = loader.getController();
		String codice = formPartitaSingola.getCodiceUtente();

		String[] giocatori = gare.restituisciDoppiGiocatori("abcd");
		lblGiocatore1.setText(giocatori[0]);
		lblGiocatore2.setText(giocatori[1]);
		//alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
	}
}