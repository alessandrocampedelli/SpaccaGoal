package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.net.URL;
import java.util.ArrayList;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import classi.Gare;
import classi.Torneo;
import classi.Partita;
import classi.Salvataggio;
import classi.Giocatore;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FormTabelloneTorneoController implements Initializable
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
	@FXML
	private Button btnMostraLeaderboard = new Button();
	@FXML
	private Button btnAvviaPartita = new Button();
	private ArrayList<Giocatore> giocatori;

	static String codiceUtente;

	public void copiaInfo(String codice) 
	{
		codiceUtente = codice;
	}

	@FXML
	public void btnAvviaPartita(ActionEvent event) throws IOException
	{	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormIniziaPartita.fxml"));
		loader.load();
		FormIniziaPartitaController form = loader.getController();
		form.copiaInfo(codiceUtente);
		alert.passaAlForm("/application/FormIniziaPartita.fxml", event);
	}

	@FXML
	public void btnMostraLeaderboard(ActionEvent event) throws IOException
	{	
		alert.passaAlForm("/application/FormLeaderboard.fxml", event);
	}

	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		try
		{
			if(!(codiceUtente == null))
			{
				Torneo t = gare.getTorneo(codiceUtente);
				if(t.getGiocatoreVincenti().size() == 1) 
				{
					btnMostraLeaderboard.setVisible(true);
					btnAvviaPartita.setVisible(false);
				}
				Salvataggio s = new Salvataggio(t);
				giocatori = s.leggiTabelloneTorneo();
				System.out.println(giocatori.size());
				/*if(giocatori.size() <= 1)
				{
					lblVincitore.setText(giocatori.get(0));
				}
				if(giocatori.size() <= 2)
				{
					lblFinale1.setText(giocatori.get(0));
					lblFinale2.setText(giocatori.get(1));
				}
				if(giocatori.size() <= 4)
				{
					lblSemifinale1.setText(giocatori.get(0));
					lblSemifinale2.setText(giocatori.get(1));
					lblSemifinale3.setText(giocatori.get(2));
					lblSemifinale4.setText(giocatori.get(3));
				}
				if(giocatori.size() <= 8) 
				{
					lblQuarto1.setText(giocatori.get(0));
					lblQuarto2.setText(giocatori.get(1));
					lblQuarto3.setText(giocatori.get(2));
					lblQuarto4.setText(giocatori.get(3));
					lblQuarto5.setText(giocatori.get(4));
					lblQuarto6.setText(giocatori.get(5));
					lblQuarto7.setText(giocatori.get(6));
					lblQuarto8.setText(giocatori.get(7));
				}*/
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}