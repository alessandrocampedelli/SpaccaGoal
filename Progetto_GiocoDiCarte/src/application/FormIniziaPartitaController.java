package application;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.lang.InterruptedException;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import classi.Gare;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class FormIniziaPartitaController 
{
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Gare gare = new Gare();
	@FXML
	private Label lblGiocatore1;
	@FXML
	private Label lblGiocatore2;
	static String codiceUtente;
	//classe in cui voglio che arrivino i dati
	public void copiaInfo(String codice) {
		this.codiceUtente = codice;
	}
	String[] giocatori;
	public static void main(String[] args)
	{
		
	}@FXML
	public void btnAvviaPartita(ActionEvent event) throws IOException
	{	
		alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
	}
	@FXML
	public void mostraNomi(MouseEvent event) throws IOException, InterruptedException
	{
		giocatori = gare.restituisciDoppiGiocatori(codiceUtente);
		if(!lblGiocatore1.getText().equals(giocatori[0])) {
			lblGiocatore1.setText(giocatori[0]);
			lblGiocatore2.setText(giocatori[1]);
		}
	}
}