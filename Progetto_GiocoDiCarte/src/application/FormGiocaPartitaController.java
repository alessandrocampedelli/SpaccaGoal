package application;

import javafx.fxml.FXML;


import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.scene.layout.HBox;
import classi.Alert_cambiaForm;
import classi.Carta;
import classi.Gara;
import classi.Salvataggio;
import classi.Gare;
import classi.Giocatore;
import classi.Partita;
import classi.Tipologia;
import classi.Leaderboard;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import classi.Mazzo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FormGiocaPartitaController implements Initializable
{
	Gare g = new Gare();
	Leaderboard leaderboard = new Leaderboard();
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Salvataggio s;
	static String codicePartita;

	public void copiaCodice(String codice) 
	{
		codicePartita = codice;
	}
	
	@FXML
	private Label lblTurnoAttacco;
	@FXML
	private Label lblGiocatore1;
	@FXML
	private Label lblGiocatore2;
	@FXML
	private Label lblGiocatore3;
	@FXML
	private Label lblGiocatore4;
	@FXML
	private ListView<String> listCarte = new ListView<String>();
	@FXML
	ImageView imgGiocata = new ImageView();

	String turno;
	int posizioneGiocatoreAttaccante;
	int posizioneGiocatoreDifensore;
	String nomeCarta;

	@FXML
	Gara partita;
	Mazzo mazzo;
	Giocatore[] players;
	String[] nomiCarte;
	Carta cartaGiocata;
	Carta cartaAtt;
	@FXML
	Button btnGiocaCarta = new Button();
	@FXML
	Button btnPassaTurno = new Button();

	// Event Listener on Button.onAction
	@FXML
	public void btnGiocaCarta(ActionEvent event) throws IOException
	{
		//salvo tutte le informazioni della partita
		s = new Salvataggio(g.getGara(codicePartita));
		cartaGiocata = Carta.valueOf(listCarte.getSelectionModel().getSelectedItem());
		if(turno.equals("a"))
			players[posizioneGiocatoreAttaccante].getMano().remove(cartaGiocata);
		else {
			partita.gioca(players[posizioneGiocatoreAttaccante], players[posizioneGiocatoreDifensore], Carta.valueOf(nomeCarta), cartaGiocata);
			players[posizioneGiocatoreDifensore].getMano().remove(cartaGiocata);
		}
		//classe da cui partono i dati
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormRigore.fxml"));
		loader.load();
		FormRigoreController form = loader.getController();
		String aliasVincente = players[posizioneGiocatoreAttaccante].getAlias();
		if(!finePartita()) 
		{
			mazzo.scarta(cartaGiocata);
			s.salvaMazzo("partite");
			s.salvaMani("partite");
			s.salvaPunteggio("partite");
			salvaTurno();
			if(cartaGiocata.equals(Carta.RIGORE) || ((cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_SX)) || (cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_C)) || (cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_DX))))
			{
				form.copiaCodice(codicePartita);
				form.copiaCartaGiocata(cartaAtt);
				alert.passaAlForm("/application/FormRigore.fxml", event);
			}
			else
			{
				alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
			}
		}
		else {
			players[posizioneGiocatoreAttaccante].aggiungiVittoriaPartita();
			leaderboard.getPlayers().get(leaderboard.indexPlayer(aliasVincente)).aggiungiVittoriaPartita();
			leaderboard.salvaPlayers();
			alert.mostraInformazione(aliasVincente+" vinto la partita", "PARTITA TERMINATA");
			form.copiaCodice(codicePartita);
			alert.passaAlForm("/application/FormLeaderboard.fxml", event);
			//s.deleteDirectory("partite");
		}
	}
	@FXML
	public void btnPassaTurno(ActionEvent event) throws IOException 
	{
		players[posizioneGiocatoreDifensore].getMano().add(this.mazzo.pesca());
		if(partita.checkGiocaTurno(players[posizioneGiocatoreAttaccante])) 
		{
			if(cartaAtt.equals(Carta.MISTER)) 
			{
				players[posizioneGiocatoreAttaccante].getMano().add(this.mazzo.pesca());
				players[posizioneGiocatoreAttaccante].getMano().add(this.mazzo.pesca());
			}
			else
			{
				players[posizioneGiocatoreAttaccante].aggiungiGoal();
			}
		}else 
		{
			if(turno.equals("d")) 
			{
				if(!cartaAtt.equals(Carta.INDICATORE_GOAL)) 
				{
					players[posizioneGiocatoreAttaccante].aggiungiGoal();
				}
			}
		}
		if(!finePartita()) 
		{
			//salvo tutte le informazioni della partita
			s = new Salvataggio(g.getGara(codicePartita));
			s.salvaMazzo("partite");
			s.salvaMani("partite");
			s.salvaPunteggio("partite");
			salvaTurno();

			alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
		}
		else 
		{
			showFinePartita(event,players[posizioneGiocatoreAttaccante].getAlias());
		}
	}
	// Event Listener on Button.onAction
	@FXML
	public void btnSospendiGara(ActionEvent event) throws IOException
	{
		if(alert.chiediConferma("Sei sicuro di voler sospendere la partita?", "ATTENZIONE")) {
			alert.mostraInformazione("Operazione eseguita con successo. La partita avente il codice '"+codicePartita+"' è stata sospesa", "OPERAZIONE COMPLETATA");
			alert.passaAlForm("/application/FormPrincipale.fxml", event);
		}
	}
	@FXML
	public void selezionaCarta(MouseEvent event) {
		//ottengo la carta selezionata
		Carta c = Carta.valueOf(listCarte.getSelectionModel().getSelectedItem());
		if(turno.equals("a")) {
			//se è una carta di attacco la rendo cliccabile e viceversa
			if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.GOAL) || c.equals(Carta.MISTER)) {
				if(!btnGiocaCarta.isVisible())
					btnGiocaCarta.setVisible(true);
			}else {
				listCarte.getSelectionModel().clearSelection();
				btnGiocaCarta.setVisible(false);
			}
		}else {
			//se è una carta di difesa la rendo cliccabile e viceversa
			if(c.getTipologia().equals(Tipologia.DIFESA) || c.equals(Carta.FUORIGIOCO) || c.equals(Carta.VAR)) {
				if(!btnGiocaCarta.isVisible())
					btnGiocaCarta.setVisible(true);
			}else {
				listCarte.getSelectionModel().clearSelection();
				btnGiocaCarta.setVisible(false);
			}
		}

	}
	
	public void stampaGiocatoriLabel()
	{
		if(!lblGiocatore1.getText().equals(players[0].getAlias())) 
		{
			if(players.length >= 2)
			{
				lblGiocatore1.setText(players[0].getAlias() + ": "+players[0].getPunteggio()+" GOAL");
				lblGiocatore2.setText(players[1].getAlias() + ": "+players[1].getPunteggio()+" GOAL");
			}
			if(players.length >= 3)
			{
				lblGiocatore3.setText(players[2].getAlias() + ": "+players[2].getPunteggio()+" GOAL");
			}
			if(players.length == 4)
			{
				lblGiocatore4.setText(players[3].getAlias() + ": "+players[3].getPunteggio()+" GOAL");
			}
		}
	}

	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		if(!(codicePartita == null)) 
		{
			try
			{
				leggiTurno();
				partita = (Partita) g.getGara(codicePartita);
				mazzo = partita.getMazzo();
				players = partita.getGiocatori();
				boolean giocaTurnoAtt = partita.checkGiocaTurno(players[posizioneGiocatoreAttaccante]);
				if(turno.equals("a")) {
					nomiCarte = players[posizioneGiocatoreAttaccante].getManoNomi();
					lblTurnoAttacco.setTextFill(Color.BLUE);
					lblTurnoAttacco.setText("TURNO DI ATTACCO: " + players[posizioneGiocatoreAttaccante].getAlias());
					if(giocaTurnoAtt) {
						btnPassaTurno.setVisible(false);
					}
					else 
					{
						btnPassaTurno.setVisible(true);
						listCarte.setDisable(true);
						cartaGiocata = Carta.INDICATORE_GOAL;
					}
				}
				else 
				{
					nomiCarte = players[posizioneGiocatoreDifensore].getManoNomi();
					lblTurnoAttacco.setTextFill(Color.RED);
					lblTurnoAttacco.setText("TURNO DI DIFESA: " + players[posizioneGiocatoreDifensore].getAlias());
					if(giocaTurnoAtt) {
						cartaAtt = Carta.valueOf(nomeCarta);
						//controllo se il giocatore ha carte con le quali può difendersi
						if(partita.checkGiocaTurno(cartaAtt, players[posizioneGiocatoreDifensore].getMano()))
						{
							btnPassaTurno.setVisible(false);
						}
						else 
						{
							btnPassaTurno.setVisible(true);
							listCarte.setDisable(true);
						}
					}
					else
					{
						if(Carta.valueOf(nomeCarta).equals(Carta.INDICATORE_GOAL)) {
							cartaAtt = Carta.INDICATORE_GOAL;
							btnPassaTurno.setVisible(true);
							listCarte.setDisable(true);
						}else {
							cartaAtt = Carta.valueOf(nomeCarta);
							//controllo se il giocatore ha carte con le quali può difendersi
							if(partita.checkGiocaTurno(cartaAtt, players[posizioneGiocatoreDifensore].getMano()))
							{
								btnPassaTurno.setVisible(false);
							}
							else 
							{
								btnPassaTurno.setVisible(true);
								listCarte.setDisable(true);
							}
						}
					}
					imgGiocata.setImage(cartaAtt.getImmagine());
				}
			}
			catch(IOException e)
			{
				System.out.println(e.getMessage());
			}
			stampaGiocatoriLabel();
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
			//alert.mostraCartaPescata();
		}
	}
	
	private void leggiTurno() throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/partite/"+codicePartita+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		Scanner scan = new Scanner(f);
		turno = scan.nextLine();
		posizioneGiocatoreAttaccante = scan.nextInt();
		posizioneGiocatoreDifensore = scan.nextInt();
		if(turno.equals("d"))
		{
			nomeCarta = scan.next();
		}
		scan.close();
	}
	
	private void salvaTurno() throws IOException{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/partite/"+codicePartita+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		if(turno.equals("a")) 
		{
			if(cartaGiocata.equals(Carta.RIGORE)) 
			{
				fw.println("a");
			}
			else 
			{
				fw.println("d");
			}
			fw.println(posizioneGiocatoreAttaccante);
			fw.println(posizioneGiocatoreDifensore);
			fw.println(cartaGiocata.name());
		}
		else 
		{
			if(!(cartaGiocata == null)) 
			{
				if((cartaAtt.equals(Carta.RIGORE_SX)||cartaAtt.equals(Carta.RIGORE_C)||cartaAtt.equals(Carta.RIGORE_DX)) && (cartaGiocata.equals(Carta.PORTIERE ))) 
				{
					fw.println("d");
					fw.println(posizioneGiocatoreAttaccante);
					fw.println(posizioneGiocatoreDifensore);
					fw.println(cartaAtt.name());
				}
			}
			fw.println("a");
			if((players.length-1) != posizioneGiocatoreAttaccante)
				fw.println(posizioneGiocatoreAttaccante+1);
			else
				fw.println(0);

			if((players.length-1) != posizioneGiocatoreDifensore)
				fw.println(posizioneGiocatoreDifensore+1);
			else
				fw.println(0);
		}
		fw.close();
	}
	private boolean finePartita() 
	{
		boolean fine = false;
		if(players[posizioneGiocatoreAttaccante].getPunteggio() == 5)
			fine = true;
		return fine;
	}

	private void showFinePartita(ActionEvent event, String aliasVincente) throws IOException
	{
		leaderboard.getPlayers().get(leaderboard.indexPlayer(aliasVincente)).aggiungiVittoriaPartita();
		leaderboard.salvaPlayers();
		//classe da cui partono i dati
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormLeaderboard.fxml"));
		loader.load();
		alert.mostraInformazione(partita.mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO LA PARTITA");
		alert.passaAlForm("/application/FormLeaderboard.fxml", event);
		s = new Salvataggio(g.getGara(codicePartita));
		//s.deleteDirectory("partite");
	}
}