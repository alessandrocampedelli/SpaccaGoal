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

public class FormCreaTorneoController{
	@FXML
	private TextField txtAlias;
	@FXML
	private CheckBox chbRobot;
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

	private ArrayList<Giocatore> giocatori = new ArrayList<>();
	private Salvataggio salvaGara;
	static Torneo t;
	Leaderboard leaderboard = new Leaderboard();
	public String getGiocatori() {
		String output = "";
		for(Giocatore g: giocatori) {
			output += g.getAlias() + "\n";
		}
		return output;
	}
	public String[] nomiGiocatori() {
		String[] g = new String[giocatori.size()];
		for(int i = 0; i < g.length; i++) {
			g[i] = giocatori.get(i).getAlias();
		}
		return g;
	}
	private Alert_cambiaForm alert = new Alert_cambiaForm();
	// Event Listener on Button.onAction
	@FXML
	public void btnAggiungiGiocatore(ActionEvent event) {
		try {
			boolean robot = false;
			if(txtAlias.getText().trim().equals("")) {
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IOException();
			}
			String nome = txtAlias.getText();
			if(nomeGiaUsato(nome)) {
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IllegalArgumentException();
			}
			if(chbRobot.isSelected()) {
				robot = true;
				txtGiocatoriInseriti.getItems().add(txtGiocatoriInseriti.getItems().size(), nome+" (Robot)");
			}else
				txtGiocatoriInseriti.getItems().add(txtGiocatoriInseriti.getItems().size(), nome);
			
			Giocatore nuovoGiocatore = leaderboard.giocatoreGiaCreato(nome);
			//se è vero significa che questo alias non è mai stato usato e non è collegato a nessun giocatore
			if(nuovoGiocatore == null) {
				nuovoGiocatore = new Giocatore(nome,robot);
				//aggiungo il giocatore alla lista di giocatori globali
				leaderboard.addPlayers(nuovoGiocatore);
			}
			int i = leaderboard.getPlayers().indexOf(nuovoGiocatore);
			giocatori.add(leaderboard.getPlayers().get(i));
			setLabel();
		}catch (IOException e) {
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}
	}
	@FXML
	public void btnCreaTorneo(ActionEvent event) throws IOException
	{	
		if(alert.chiediConferma("Sei sicuro di creare questo torneo con i seguenti giocatori:\n"+getGiocatori(), "MESSAGGIO DI CONFERMA")) {
			String codice = getRandomString(6,'a', 'z');
			leaderboard.salvaPlayers();
			codice = "t"+codice;
			Collections.shuffle(giocatori);
			t = new Torneo(giocatori,codice);
			//creazione delle partite
			t.creazionePartite();
			//forse nn serve
			FXMLLoader loader =new FXMLLoader(getClass().getResource("FormPrincipale.fxml"));
			loader.load();
			FormPrincipaleController formPrincipale = loader.getController();
			formPrincipale.aggiungiGara(t);
			//creo la cartella del torneo
			salvaGara = new Salvataggio(t);
			salvaGara.createDirectory("tornei");
			salvaGara.salvaGiocatoriTorneo();
			//dentro la cartella del torneo creo tante cartelle per ogni partita, ognuna con tutte le sue info
			for(Partita p : t.getPartite()) 
			{
				salvaGara = new Salvataggio(p);
				String percorso = "tornei/"+t.getCodiceGara();
				p.distribuzioneCarte();
				salvaGara.salvaNomiGiocatori(percorso);
				salvaGara.salvaMazzo(percorso);
				salvaGara.salvaMani(percorso);
				salvaGara.salvaPunteggio(percorso);
				p.salvaTurno(percorso);
			}

			alert.mostraInformazione("Codice del torneo: "+codice,"TORNEO CREATO CON SUCCESSO");
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
			giocatori.clear();
		}
	}
	
	private String getRandomString(int len, char minChar, char maxChar) {
		String s = "";
		for (int i = 0; i < len; ++i)
			s += (char) ((Math.random() * (maxChar - minChar)) + minChar);
		return s;
	}
	private boolean nomeGiaUsato(String nome) {
		for(Giocatore g : giocatori) {
			if(g.getAlias().equals(nome))	
				return true;
		}
		return false;
	}
	private void setLabel() {
		txtAlias.clear();
		chbRobot.setSelected(false);

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

		txtAlias.clear();
		chbRobot.setSelected(false);

		if(giocatori.size() == 8) {
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
	@FXML
	public void btnVaiIndietro1(MouseEvent event) throws IOException
	{
		giocatori.clear();
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
	}
	@FXML
	public void eliminaGiocatore(MouseEvent event) throws IOException {
		// TODO Autogenerated
		int indiceEliminato = txtGiocatoriInseriti.getSelectionModel().getSelectedIndex();
		String alias  =txtGiocatoriInseriti.getSelectionModel().getSelectedItem();
		if(alert.chiediConferma("Sei sicuro di voler eliminare il giocatore di nome: "+alias, "MESSAGGIO DI CONFERMA")){
			giocatori.remove(indiceEliminato);
			txtGiocatoriInseriti.getItems().clear();
			txtGiocatoriInseriti.getItems().addAll(nomiGiocatori());
			setLabel();
		}
	}
}