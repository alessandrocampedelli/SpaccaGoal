package application;

import javafx.fxml.FXML;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import classi.Salvataggio;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import com.sun.tools.javac.Main;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import classi.Alert_cambiaForm;
import classi.Giocatore;
import classi.Torneo;
import classi.Partita;
import classi.Gara;
import classi.Gare;
import classi.Leaderboard;

public class FormCreaTorneoController implements Initializable
{
	@FXML
	private ListView<String> txtGiocatoriInseriti = new ListView<String>();
	@FXML
	private Label lblGiocatoriInseriti;
	@FXML
	private Label lblSemifinale;
	@FXML
	private Label lblQuartiDiFinale;
	@FXML
	private Button btnAggiungiGiocatore = new Button();
	@FXML
	private Button btnCreaTorneo = new Button();
	@FXML
	private ComboBox<String> cmbSelectPlayer = new ComboBox<>();

	private ArrayList<Giocatore> giocatori = new ArrayList<>();
	private Salvataggio salvaGara;
	static Torneo t;
	Gare gare = new Gare();
	Leaderboard leaderboard = new Leaderboard();

	private Alert_cambiaForm alert = new Alert_cambiaForm();

	@FXML
	public void btnAggiungiGiocatore(ActionEvent event) 
	{
		try 
		{
			if(cmbSelectPlayer.getSelectionModel().getSelectedItem() == null) 
			{
				throw new IOException();
			}
			String nome = cmbSelectPlayer.getSelectionModel().getSelectedItem();

			if(nomeGiaUsato(nome)) 
			{
				cmbSelectPlayer.setValue(null);
				throw new IllegalArgumentException();
			}
			Giocatore nuovoGiocatore = leaderboard.getPlayers(nome);
			if(nuovoGiocatore.isRobot()) 
			{
				txtGiocatoriInseriti.getItems().add(txtGiocatoriInseriti.getItems().size(), nome+" (Robot)");
			}
			else
			{
				txtGiocatoriInseriti.getItems().add(txtGiocatoriInseriti.getItems().size(), nome);
			}
			int i = leaderboard.getPlayers().indexOf(nuovoGiocatore);
			giocatori.add(leaderboard.getPlayers().get(i));
			setLabel();
			cmbSelectPlayer.setValue(null);
		}catch(IllegalArgumentException e){
			alert.mostraErrore("Il giocatore selezionato è gia stato inserito","ERRORE");
		}
		catch (IOException e) 
		{
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}
	}

	@FXML
	public void btnCreaTorneo(ActionEvent event) throws IOException
	{	
		if(alert.chiediConferma("Sei sicuro di creare questo torneo con i seguenti giocatori:\n"+getGiocatori(), "MESSAGGIO DI CONFERMA")) 
		{
			String codice = getRandomString(6,'a', 'z');
			leaderboard.salvaPlayers();
			codice = "t"+codice;
			//mischio l'ordine in cui i giocatori giocano la partita
			Collections.shuffle(giocatori);
			t = new Torneo(giocatori,codice);
			//creazione delle partite
			t.creazionePartite();
			gare.aggiungiGara(t);
			//creo la cartella del torneo
			salvaGara = new Salvataggio(t);
			salvaGara.createDirectory("tornei");
			salvaGara.salvaGiocatoriTorneo();
			t.setTabelloneGiocatori(giocatori);
			salvaGara.salvaTabelloneTorneo();
			//dentro la cartella del torneo creo tante cartelle per ogni partita, ognuna con tutte le sue info
			for(Partita p : t.getPartite()) 
			{
				Salvataggio salvaGara = new Salvataggio(p);
				salvaGara.salvaPartita(p, "tornei/"+t.getCodiceGara());
			}
			alert.mostraInformazione("Codice del torneo: "+codice,"TORNEO CREATO CON SUCCESSO");
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
			giocatori.clear();
		}
	}

	@FXML
	public void btnVaiIndietro1(MouseEvent event) throws IOException
	{
		giocatori.clear();
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
	}

	@FXML
	public void eliminaGiocatore(MouseEvent event) throws IOException 
	{
		int indiceEliminato = txtGiocatoriInseriti.getSelectionModel().getSelectedIndex();
		if(indiceEliminato != -1) 
		{
			String alias  =txtGiocatoriInseriti.getSelectionModel().getSelectedItem();
			if(alert.chiediConferma("Sei sicuro di voler eliminare il giocatore di nome: "+alias, "MESSAGGIO DI CONFERMA"))
			{
				giocatori.remove(indiceEliminato);
				txtGiocatoriInseriti.getItems().clear();
				txtGiocatoriInseriti.getItems().addAll(nomiGiocatori());
				setLabel();
			}	
		}
	}

	private String getRandomString(int len, char minChar, char maxChar) 
	{
		String s = "";
		for (int i = 0; i < len; ++i)
		{
			s += (char) ((Math.random() * (maxChar - minChar)) + minChar);
		}
		return s;
	}

	private boolean nomeGiaUsato(String nome) 
	{
		for(Giocatore g : giocatori) 
		{
			if(g.getAlias().equals(nome))	
			{
				return true;
			}
		}
		return false;
	}

	private void setLabel() 
	{
		lblGiocatoriInseriti.setText("Giocatori inseriti: "+giocatori.size());

		if(giocatori.size() <= 4)
		{
			lblSemifinale.setText("" + (4 - giocatori.size()));
		}
		else
		{
			lblSemifinale.setText("");
		}
		lblQuartiDiFinale.setText("" + (8 - giocatori.size()));

		if(giocatori.size() == 8) 
		{
			btnAggiungiGiocatore.setVisible(false);
		}
		if(giocatori.size() == 4 || giocatori.size() == 8) 
		{
			btnCreaTorneo.setVisible(true);
		}
		else 
		{
			btnCreaTorneo.setVisible(false);
		}
	}

	public String getGiocatori() 
	{
		String output = "";
		for(Giocatore g: giocatori) 
		{
			output += g.getAlias() + "\n";
		}
		return output;
	}

	public String[] nomiGiocatori() 
	{
		String[] g = new String[giocatori.size()];
		for(int i = 0; i < g.length; i++) 
		{
			g[i] = giocatori.get(i).getAlias();
		}
		return g;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		for(Giocatore g : leaderboard.getPlayers()) 
		{
			if(g.isRobot())
				cmbSelectPlayer.getItems().add(g.getAlias()+" (Robot)");
			else
				cmbSelectPlayer.getItems().add(g.getAlias());
		}
	}
}