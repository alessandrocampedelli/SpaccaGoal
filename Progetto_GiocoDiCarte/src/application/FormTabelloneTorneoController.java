package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import classi.Gare;
import classi.Torneo;
import classi.Salvataggio;
import classi.Giocatore;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;

//classe che mostra il tabellone del torneo, possono partire dalle semifinali oppure dai quarti
public class FormTabelloneTorneoController implements Initializable
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	//creo un oggetto di classe "Gare"
	Gare gare = new Gare();

	//tutte le 8 label relative ai quarti, se sono 8 giocatori che prendono parte al torneo saranno utilizzate
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
	//tutte le 4 label relative alle semifinali
	@FXML
	private Label lblSemifinale1;
	@FXML
	private Label lblSemifinale2;
	@FXML
	private Label lblSemifinale3;
	@FXML
	private Label lblSemifinale4;
	//le 2 label relative alle finali
	@FXML
	private Label lblFinale1;
	@FXML
	private Label lblFinale2;
	//la label del vincitore della partita
	@FXML
	private Label lblVincitore;
	//il bottone utile per mostrare la leaderboard
	@FXML
	private Button btnMostraLeaderboard = new Button();
	//il bottone per avviare la prima partita del torneo
	@FXML
	private Button btnAvviaPartita = new Button();
	//l'ArrayList dei giocatori che prendono parte al torneo e quindi verranno mostrati nel tabellone
	private ArrayList<Giocatore> giocatori;
	//il codice dell'utente inserito nel form precedente
	static String codiceUtente;
	Torneo t;

	//metodo che permette il passaggio del codice da un form ad un altro
	public void copiaInfo(String codice) 
	{
		codiceUtente = codice;
	}
	
	//il bottone che permette di avviare una partita del torneo
	@FXML
	public void btnAvviaPartita(ActionEvent event) throws IOException
	{	
		//classe che permette di passare il codice della partita inserito dall'utente al form "iniziaPartita" 
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormIniziaPartita.fxml"));
		loader.load();
		FormIniziaPartitaController form = loader.getController();
		//copiaInfo è un metodo del form "iniziaPartita"
		form.copiaInfo(codiceUtente);
		//passo al form inzia partita
		alert.passaAlForm("/application/FormIniziaPartita.fxml", event);
	}

	//il bottone che permette di mostrare la leaderboard al termine del torneo
	@FXML
	public void btnMostraLeaderboard(ActionEvent event) throws IOException
	{	
		//il torneo è terminato, eliminazione della cartella del torneo dalla cartella "tornei"
		Salvataggio s = new Salvataggio(t);
		s.deleteDirectory("tornei");
		
		//passo al form di visualizzazione della leaderboard
		alert.passaAlForm("/application/FormLeaderboard.fxml", event);
	}

	//metodo che viene eseguito all'apertura del form
	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		try
		{
			//controllo se il codice passato non sia null
			if(!(codiceUtente == null))
			{				
				//restituisce l'oggetto "Torneo" con il codice del torneo
				t = gare.getTorneo(codiceUtente);
				Salvataggio s = new Salvataggio(t);
				//mi restituisce i giocatori che andranno inseriti nel tabellone
				giocatori = s.leggiTabelloneTorneo();
				Label[] labels;
				//controllo se mi servono per stampare il tabellone anche le label dei quarti oppure no, dipende dal numero di giocatori
				if(t.getTabelloneGiocatori().size() >= 8) 
				{
					//i giocatori sono maggiori di 8 quindi il torneo è composto da 8 giocatori e si partirà a giocare dai quattro quarti
					labels = new Label[]{lblQuarto1,lblQuarto2,lblQuarto3,lblQuarto4,lblQuarto5,lblQuarto6,lblQuarto7,lblQuarto8,lblSemifinale1,lblSemifinale2,lblSemifinale3,lblSemifinale4,lblFinale1,lblFinale2,lblVincitore};
				}
				else 
				{
					//i giocatori sono minori di 8 quindi il torneo è composto da 4 giocatori e si partirà a giocare dalle due semifinali
					labels = new Label[]{lblSemifinale1,lblSemifinale2,lblSemifinale3,lblSemifinale4,lblFinale1,lblFinale2,lblVincitore};	
				}
				//riempio le label con i valori dell'ArrayList "giocatori"
				for(int i = 0; i < giocatori.size(); i++)
				{
					//controllo se il giocatore in questione è un robot oppure no, se fosse aggiungo l'informazione nella stampa
					if(giocatori.get(i).isRobot())
					{
						labels[i].setText(giocatori.get(i).getAlias() + "*");
					}
					else
					{
						labels[i].setText(giocatori.get(i).getAlias());
					}
				}
				//controllo se fosse rimasto solo un giocatore, nel caso sarebbe il vincitore
				if(t.getGiocatoreVincenti().size() == 1) 
				{
					//mostro il bottone leaderboard all'utente e non mostro il bottone "avvia partita"
					btnMostraLeaderboard.setVisible(true);
					btnAvviaPartita.setVisible(false);
					//elimino la cartella della partita del torneo appena giocata con il metodo della classe "Salvataggio"
					Salvataggio elimina = new Salvataggio(t);
					elimina.deleteDirectory("tornei/"+t.getCodiceGara());
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}