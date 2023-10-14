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
import classi.Torneo;
import classi.Gara;
import classi.Gare;

public class FormCreaTorneoController implements Initializable{
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
	static Gara g;

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
			if(chbRobot.isSelected()) {
				robot = true;
				txtGiocatoriInseriti.getItems().add(txtGiocatoriInseriti.getItems().size(), nome+" (Robot)");
			}else
				txtGiocatoriInseriti.getItems().add(txtGiocatoriInseriti.getItems().size(), nome);

			giocatori.add(new Giocatore(nome,robot));

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
		}catch (IOException e) {
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}
	}
	@FXML
	public void btnCreaTorneo(ActionEvent event) throws IOException
	{	
		if(alert.chiediConferma("Sei sicuro di creare questo torneo con i seguenti giocatori:\n"+getGiocatori(), "MESSAGGIO DI CONFERMA")) {
			String codice = getRandomString(6,'a', 'z');
			codice = "t"+codice;
			g = new Torneo(giocatori,codice);
			g.distribuzioneCarte();

			FXMLLoader loader =new FXMLLoader(getClass().getResource("FormPrincipale.fxml"));
			loader.load();
			FormPrincipaleController formPrincipale = loader.getController();
			formPrincipale.aggiungiGara(g);
			//IDEA SALVATAGGIO TORNEO. FARE DENTRO LA CARTELLA DEL TORNEO UNA CARTELLA "PARTITE" CON DENTRO TUTTE LE PARTITE.
			salvaGara = new Salvataggio(g);
			salvaGara.salvaNomiGiocatori("tornei");
			salvaGara.salvaMazzo("tornei");
			salvaGara.salvaMani("tornei");
			salvaGara.salvaPunteggio("tornei");
			//PROVARE SALVA TURNO DENTRO CLASSE GARA SE FUNZIONANTE
			salvaTurno(codice);

			alert.mostraInformazione("Codice del torneo: "+codice,"TORNEO CREATO CON SUCCESSO");
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
			giocatori.clear();
		}
	}
	
	private void salvaTurno(String codice) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+codice+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter pw = new PrintWriter(absolutePath);
		
		pw.println("a");
		pw.println(0);
		pw.println(1);
		//PER NON FARE VEDERE LA PRIMA CARTA PESCATA PROVARE AD INSERIRE 1
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
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtGiocatoriInseriti.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				int indiceEliminato = txtGiocatoriInseriti.getSelectionModel().getSelectedIndex();
				String alias  =txtGiocatoriInseriti.getSelectionModel().getSelectedItem();
				if(alert.chiediConferma("Sei sicuro di voler eliminare il giocatore di nome: "+alias, "MESSAGGIO DI CONFERMA")){
					giocatori.remove(indiceEliminato);
					txtGiocatoriInseriti.getItems().clear();
					txtGiocatoriInseriti.getItems().addAll(nomiGiocatori());

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

					if(giocatori.size() != 8) 
					{
						btnAggiungiGiocatore.setVisible(true);
					}
					if(giocatori.size() == 4 || giocatori.size() == 8) 
					{
						btnCreaTorneo.setVisible(true);
					}else 
					{
						btnCreaTorneo.setVisible(false);
					}
				}
			}

		});
	}
}