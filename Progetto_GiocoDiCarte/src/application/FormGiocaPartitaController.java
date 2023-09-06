package application;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;
import classi.Alert_cambiaForm;
import classi.Carta;
import classi.Gara;
import classi.Gare;
import classi.Giocatore;
import classi.Partita;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import classi.Mazzo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FormGiocaPartitaController implements Initializable
{	
	Gare g = new Gare();
	Alert_cambiaForm alert = new Alert_cambiaForm();

	static String codicePartita;

	public void copiaCodice(String codice) 
	{
		codicePartita = codice;
	}

	Partita partita;
	Mazzo mazzo;
	Giocatore[] players;
	String[] nomiCarte;
	@FXML
	private ListView<String> listCarte = new ListView<String>();

	
	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		if(!(codicePartita == null)) {
			partita = (Partita) g.getGara(codicePartita);
			partita.distribuzioneCarte();
			mazzo = partita.getMazzo();
			players = partita.getGiocatori();
			nomiCarte = players[0].getManoNomi();
			ObservableList<String> items =FXCollections.observableArrayList (nomiCarte);
			// TODO Auto-generated method stub
			listCarte.setItems(items);
			listCarte.setCellFactory(param -> {
				return new ListCell<String>() {
					private ImageView imageView = new ImageView();
	
					@Override
					public void updateItem(String name, boolean empty) {
						super.updateItem(name, empty);
						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							switch(name) {
							case "ATTACCANTE": imageView.setImage(Carta.ATTACCANTE.getImmagine()); break;
							case "BOMBER_VERO": imageView.setImage(Carta.BOMBER_VERO.getImmagine()); break;
							case "RIGORE": imageView.setImage(Carta.RIGORE.getImmagine()); break;
							case "ROVESCIATA_DELLANNO": imageView.setImage(Carta.ROVESCIATA_DELLANNO.getImmagine()); break;
							case "TIRO_DOMENICA": imageView.setImage(Carta.TIRO_DOMENICA.getImmagine()); break;
							case "CAMBIO_SCHEMA": imageView.setImage(Carta.CAMBIO_SCHEMA.getImmagine()); break;
							case "DIFENSORE": imageView.setImage(Carta.DIFENSORE.getImmagine()); break;
							case "DIFENSORE_ROCCIA": imageView.setImage(Carta.DIFENSORE_ROCCIA.getImmagine()); break;
							case "PORTIERE": imageView.setImage(Carta.PORTIERE.getImmagine()); break;
							case "INDICATORE_GOAL": imageView.setImage(Carta.INDICATORE_GOAL.getImmagine()); break;
							case "AUTOGOAL": imageView.setImage(Carta.AUTOGOAL.getImmagine()); break;
							case "FUORIGIOCO": imageView.setImage(Carta.FUORIGIOCO.getImmagine()); break;
							case "GOAL": imageView.setImage(Carta.GOAL.getImmagine()); break;
							case "MISTER": imageView.setImage(Carta.MISTER.getImmagine()); break;
							case "VAR": imageView.setImage(Carta.VAR.getImmagine()); break;
							}
							setGraphic(imageView);
						}
					}
				};
			});
		}
	}}
