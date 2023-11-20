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
import java.io.*;
import classi.Salvataggio;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import classi.Partita;
import classi.Gara;
import classi.Gare;
import classi.Leaderboard;

public class FormCreaPartitaController implements Initializable
{
	@FXML
	private ListView<String> txtGiocatoriInseriti = new ListView<String>();
	@FXML
	private Label lblGiocatoriInseriti;
	@FXML
	private Label lblGiocatoriDaInserire;
	@FXML
	private Button btnAggiungiGiocatore = new Button();
	@FXML
	private Button btnCreaPartita = new Button();
	@FXML
	private ComboBox<String> cmbSelectPlayer = new ComboBox<>();
	private ArrayList<Giocatore> giocatori = new ArrayList<>();
	private Alert_cambiaForm alert = new Alert_cambiaForm();
	static Partita p;
	Leaderboard leaderboard = new Leaderboard();
	Gare gare = new Gare();

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
				lblGiocatoriInseriti.setText("Giocatori inseriti: "+giocatori.size());
				lblGiocatoriDaInserire.setText("Giocatori che puoi ancora inserire: "+(4 - giocatori.size()));
				if(giocatori.size() != 4) 
				{
					btnAggiungiGiocatore.setVisible(true);
				}
				if(giocatori.size() >= 2) 
				{
					btnCreaPartita.setVisible(true);
				}
				else 
				{
					btnCreaPartita.setVisible(false);
				}
			}
		}
	}

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

			lblGiocatoriInseriti.setText("Giocatori inseriti: "+giocatori.size());
			lblGiocatoriDaInserire.setText("Giocatori che puoi ancora inserire: "+(4 - giocatori.size()));
			if(giocatori.size() == 4) 
			{
				btnAggiungiGiocatore.setVisible(false);
			}
			if(giocatori.size() >= 2) 
			{
				btnCreaPartita.setVisible(true);
			}
			else 
			{
				btnCreaPartita.setVisible(false);
			}
			cmbSelectPlayer.setValue(null);
		}
		catch (IOException e) 
		{
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}
		catch (IllegalArgumentException e) 
		{
			alert.mostraErrore("Il giocatore selezionato è gia stato inserito","ERRORE");
		}
	}
	
	@FXML
	public void btnCreaPartita(ActionEvent event) throws IOException
	{	
		if(alert.chiediConferma("Sei sicuro di creare questa partita con i seguenti giocatori:\n"+getGiocatori(), "MESSAGGIO DI CONFERMA")) 
		{
			//salvo la leaderboard con gli eventuali nuovi giocatori creati
			leaderboard.salvaPlayers();
			String codice = getRandomString(6,'a', 'z');
			codice = "p"+codice;
			p = new Partita(giocatori,codice);
			gare.aggiungiGara(p);
			Salvataggio salvaGara = new Salvataggio(p);
			salvaGara.salvaPartita(p, "partite");
			alert.mostraInformazione("Codice della partita: "+codice,"PARTITA CREATA CON SUCCESSO");
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
			giocatori.clear();
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