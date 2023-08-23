package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.ArrayList;

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
	private Label lblNomeGiocatore1;
	@FXML
	private Label lblNomeGiocatore2;
	@FXML
	private Label lblNomeGiocatore3;
	@FXML
	private Label lblNomeGiocatore4;
	@FXML
	
	private ArrayList<String> giocatori;

	static String codiceUtente;
	
	public void copiaInfo(String codice) 
	{
		codiceUtente = codice;
	}
	@FXML
	public void btnAvviaPartita(ActionEvent event) throws IOException
	{	
		alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
		
		//passo il codice al controller GiocaPartita
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormGiocaPartita.fxml"));
		Parent root = loader.load();
		FormGiocaPartitaController form = loader.getController();
		form.copiaInfo(codiceUtente);
	}
	@FXML
	public void mostraNomi(MouseEvent event) throws IOException, InterruptedException
	{
		giocatori = gare.restituisciGiocatori(codiceUtente);
		if(!lblNomeGiocatore1.getText().equals(giocatori.get(0))) 
		{
			if(giocatori.size() >= 2)
			{
				lblNomeGiocatore1.setText(giocatori.get(0));
				lblNomeGiocatore2.setText(giocatori.get(1));
			}
			if(giocatori.size() >= 3)
			{
				lblNomeGiocatore3.setText(giocatori.get(2));
			}
			if(giocatori.size() == 4)
			{
				lblNomeGiocatore4.setText(giocatori.get(3));
			}
		}
	}
}
