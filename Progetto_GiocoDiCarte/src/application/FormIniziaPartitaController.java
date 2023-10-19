package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.lang.InterruptedException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import classi.Alert_cambiaForm;
import classi.Carta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import classi.Gare;
import classi.Partita;
import classi.Torneo;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

public class FormIniziaPartitaController implements Initializable
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
	static String codicePartitaTorneo;

	public void copiaInfo(String codice, String codiceTorneo) 
	{
		codiceUtente = codice;
		codicePartitaTorneo = codiceTorneo;
	}

	@FXML
	public void btnAvviaPartita(ActionEvent event) throws IOException
	{	
		//classe da cui partono i dati
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormGiocaPartita.fxml"));
		loader.load();
		FormGiocaPartitaController form = loader.getController();
		form.copiaCodice(codiceUtente);
		alert.mostraInformazione(getGiocatoriString(), "TURNO DI GIOCO");
		alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
	}
	//IDEA, CONTROLLARE SE è T E ANDARE ALLA RICERCA DELLA PARTITA DA INIZIARE
	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		if(!(codiceUtente == null)) 
		{
			Torneo t = (Torneo) gare.getGara(codiceUtente);
			//sarà sempre la prima partita perchè mano a mano vengono eliminate
			Partita p = t.getPartitaTorneo(codicePartitaTorneo);
			giocatori = gare.restituisciGiocatori(codiceUtente);

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

	public String getGiocatoriString() 
	{
		String output = "";
		for(int i = 0; i < this.giocatori.size(); i++) 
		{
			output += (i+1) +") "+giocatori.get(i)+"\n";
		}
		return output;
	}
}
