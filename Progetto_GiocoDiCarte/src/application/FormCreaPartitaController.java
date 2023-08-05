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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import classi.Alert_cambiaForm;
import classi.Codice;
import classi.Giocatore;
import classi.Partita;
import classi.Gare;
public class FormCreaPartitaController implements Initializable{
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
	public String getGiocatori() {
		String output = "";
		for(Giocatore g: giocatori) {
			output += g.getAlias() + "\n";
		}
		return output;
	}
	private Alert_cambiaForm alert = new Alert_cambiaForm();
	// Event Listener on Button.onAction
	@FXML
	public void btnAggiungiGiocatore(ActionEvent event) {
		try {
			boolean robot = false;
			if(txtAlias.getText().equals("")) {
				throw new IOException();
			}
			String nome = txtAlias.getText();
			if(chbRobot.isSelected()) {
				robot = true;
				txtGiocatoriInseriti.getItems().add(txtGiocatoriInseriti.getItems().size(), nome+" (Robot)");
			}else
				txtGiocatoriInseriti.getItems().add(txtGiocatoriInseriti.getItems().size(), nome);
			
			giocatori.add(new Giocatore(nome,robot));
			
			lblGiocatoriInseriti.setText("Giocatori inseriti: "+giocatori.size());
			lblGiocatoriDaInserire.setText("Giocatori che puoi ancora inserire: "+(5 - giocatori.size()));
			
			txtAlias.clear();
			chbRobot.setSelected(false);
			
			if(giocatori.size() == 5) {
				btnAggiungiGiocatore.setVisible(false);
			}
			if(giocatori.size() >= 2) {
				btnCreaPartita.setVisible(true);
			}else {
				btnCreaPartita.setVisible(false);
			}
		}catch (IOException e) {
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}
	}
	@FXML
	public void btnCreaPartita(ActionEvent event) throws IOException
	{	
		if(alert.chiediConferma("Sei sicuro di creare questa partita con i seguenti giocatori: "+getGiocatori(), "MESSAGGIO DI CONFERMA")) {
			String codice = getRandomString(5,'!', '~');
			codice = "p_"+codice;
			Partita p = new Partita(giocatori,codice);
			Gare g = new Gare();
			g.aggiungiGara(p);
			alert.mostraInformazione("Codice della partita: "+codice,"PARTITA CREATA CON SUCCESSO");
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
	@FXML
	public void btnVaiIndietro1(MouseEvent event) throws IOException
	{
		giocatori.clear();
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}