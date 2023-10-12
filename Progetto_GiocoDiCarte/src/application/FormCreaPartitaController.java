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
import classi.Codice;
import classi.Giocatore;
import classi.Partita;
import classi.Gara;
import classi.Gare;
import classi.Leaderboard;
public class FormCreaPartitaController{
	@FXML
	private TextField txtAlias;
	@FXML
	private CheckBox chbRobot;
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
	
	private ArrayList<Giocatore> giocatori = new ArrayList<>();
	private Salvataggio salvaGara;
	static Gara g;
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
			
			lblGiocatoriInseriti.setText("Giocatori inseriti: "+giocatori.size());
			lblGiocatoriDaInserire.setText("Giocatori che puoi ancora inserire: "+(4 - giocatori.size()));
			
			txtAlias.clear();
			chbRobot.setSelected(false);
			
			if(giocatori.size() == 4) {
				btnAggiungiGiocatore.setVisible(false);
			}
			if(giocatori.size() >= 2) {
				btnCreaPartita.setVisible(true);
			}else {
				btnCreaPartita.setVisible(false);
			}
		}catch (IOException e) {
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}catch (IllegalArgumentException e) {
			alert.mostraErrore("Nome già utilizzato. Non sono ammessi omonimi","ERRORE");
		}
	}
	private boolean nomeGiaUsato(String nome) {
		for(Giocatore g : giocatori) {
			if(g.getAlias().equals(nome))	
				return true;
		}
		return false;
	}
	@FXML
	public void btnCreaPartita(ActionEvent event) throws IOException
	{	
		if(alert.chiediConferma("Sei sicuro di creare questa partita con i seguenti giocatori:\n"+getGiocatori(), "MESSAGGIO DI CONFERMA")) {
			//salvo la leaderboard con gli eventuali nuovi giocatori creati
			leaderboard.salvaPlayers();
			String codice = getRandomString(6,'a', 'z');
			codice = "p"+codice;
			g = new Partita(giocatori,codice);
			g.distribuzioneCarte();
			FXMLLoader loader =new FXMLLoader(getClass().getResource("FormPrincipale.fxml"));
			loader.load();
			FormPrincipaleController formPrincipale = loader.getController();
			formPrincipale.aggiungiGara(g);
			//prova
			salvaGara = new Salvataggio(g);
			salvaGara.salvaNomiGiocatori("partite");
			salvaGara.salvaMazzo("partite");
			salvaGara.salvaMani("partite");
			salvaGara.salvaPunteggio("partite");
			salvaTurno(codice);
			
			alert.mostraInformazione("Codice della partita: "+codice,"PARTITA CREATA CON SUCCESSO");
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
			giocatori.clear();
		}
	}
	
	private void salvaTurno(String codice) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/partite/"+codice+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter pw = new PrintWriter(absolutePath);
		
		pw.println("a");
		pw.println(0);
		pw.println(1);
		pw.println(1);
		pw.close();
	}
	
	private String getRandomString(int len, char minChar, char maxChar) {
	    String s = "";
	    for (int i = 0; i < len; ++i)
	      s += (char) ((Math.random() * (maxChar - minChar)) + minChar);
	    return s;
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