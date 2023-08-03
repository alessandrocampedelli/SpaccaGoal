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
public class FormCreaPartitaController implements Initializable{
	@FXML
	private TextField txtCodice;
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
	private Alert_cambiaForm alert = new Alert_cambiaForm();
	// Event Listener on Button.onAction
	@FXML
	public void btnAggiungiGiocatore(ActionEvent event) {
		try {
			boolean robot = false;
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
		}catch (Exception e) {
			alert.mostraErrore();
		}
	}
	@FXML
	public void btnCreaPartita(MouseEvent event) throws IOException
	{
		
	}
	@FXML
	public void btnVaiIndietro1(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
	}
	public int getNumGiocatori() {
		return giocatori.size();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}