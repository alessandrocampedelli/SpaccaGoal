package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.ArrayList;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import classi.Gare;
import classi.Partita;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class FormTabelloneTorneoController 
{
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Gare gare = new Gare();
	@FXML
	private Label lblQuarto1;
	@FXML
	private Label lblQuarto2;
	@FXML
	private Label lblQuarto3;
	@FXML
	private Label lblQuarto4;
	@FXML
	private Label lblQuarto5;
	@FXML
	private Label lblQuarto6;
	@FXML
	private Label lblQuarto7;
	@FXML
	private Label lblQuarto8;
	@FXML
	private Label lblSemifinale1;
	@FXML
	private Label lblSemifinale2;
	@FXML
	private Label lblSemifinale3;
	@FXML
	private Label lblSemifinale4;
	@FXML
	private Label lblFinale2;
	@FXML
	private Label lblFinale1;
	@FXML
	private Label lblVincitore;

	private ArrayList<String> giocatori;

	static String codiceUtente;

	public void copiaInfo(String codice) 
	{
		codiceUtente = codice;
	}

	@FXML
	public void btnAvviaPartita(ActionEvent event) throws IOException
	{	
		alert.passaAlForm("/application/FormIniziaPartita.fxml", event);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormIniziaPartita.fxml"));
		Parent root = loader.load();
		FormIniziaPartitaController form = loader.getController();
		form.copiaInfo(codiceUtente);
	}

	public void mostraNomi(MouseEvent event) throws IOException, InterruptedException
	{
		giocatori = gare.restituisciGiocatori(codiceUtente);
		if(giocatori.size() == 4)
		{
			if(!lblSemifinale1.getText().equals(giocatori.get(0)))
			{
				lblSemifinale1.setText(giocatori.get(0));
				lblSemifinale2.setText(giocatori.get(1));
				lblSemifinale3.setText(giocatori.get(2));
				lblSemifinale4.setText(giocatori.get(3));
			}
		}
		else if(giocatori.size() == 8) 
		{
			if(!lblQuarto1.getText().equals(giocatori.get(0)))
			{
				lblQuarto1.setText(giocatori.get(0));
				lblQuarto2.setText(giocatori.get(1));
				lblQuarto3.setText(giocatori.get(2));
				lblQuarto4.setText(giocatori.get(3));
				lblQuarto5.setText(giocatori.get(4));
				lblQuarto6.setText(giocatori.get(5));
				lblQuarto7.setText(giocatori.get(6));
				lblQuarto8.setText(giocatori.get(7));
			}
		}
	}
}
