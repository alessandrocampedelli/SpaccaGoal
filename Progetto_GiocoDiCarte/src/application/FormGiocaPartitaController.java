package application;

import javafx.fxml.FXML;




import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.scene.layout.HBox;
import classi.Robot;
import classi.Alert_cambiaForm;
import classi.Carta;
import classi.Gara;
import classi.Salvataggio;
import classi.Gare;
import classi.Giocatore;
import classi.Partita;
import classi.Torneo;
import classi.Tipologia;
import classi.Leaderboard;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import classi.Mazzo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FormGiocaPartitaController implements Initializable
{
	//creo un oggetto delle classi "gara", "leaderboard" e "alert"
	Gare g = new Gare();
	Leaderboard leaderboard = new Leaderboard();
	Alert_cambiaForm alert = new Alert_cambiaForm();

	Salvataggio s;
	Partita partita;
	Torneo torneo = null;
	Mazzo mazzo;
	Giocatore[] players;
	String[] nomiCarte;
	Carta cartaGiocata;
	Carta cartaAtt;
	int cartePescate;
	static String codicePartita;
	//questo metodo permette di passare il codice della partita dal form precedente (FormIniziaPartitaController)
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
	private Label lblInfoUtente;

	//la lista dove stamperemo tutte le carte delle mani dei vari giocatori
	@FXML
	private ListView<String> listCarte = new ListView<String>();
	//bottoni nel programma sovrapposti in quanto potrà esserne visibile uno solo contemporaneamente
	@FXML
	Button btnGiocaCarta = new Button();
	@FXML
	Button btnPassaTurno = new Button();
	@FXML
	Button btnSospendiGara = new Button();
	//l'immagine che sarà visibile solo in un turno di difesa con l'immagine giocata dal calciatore attaccante
	@FXML
	ImageView imgGiocata = new ImageView();

	//variabile che indica se ci stiamo riferendo ad un turno di attacco o un turno di difesa (prenderà il valore "d" per difesa e "a" per attacco)
	String turno;
	int posizioneGiocatoreAttaccante;
	int posizioneGiocatoreDifensore;
	//il nome della carta che è stato giocato dall'attaccante se ci troviamo in un turno di difesa
	String nomeCarta;

	@FXML
	public void btnGiocaCarta(ActionEvent event)
	{
		try {
			if(turno.equals("a"))
			{
				if(listCarte.getSelectionModel().getSelectedItem() == null && !players[posizioneGiocatoreAttaccante].isRobot()) {
					throw new IllegalArgumentException();
				}
				if(!players[posizioneGiocatoreAttaccante].isRobot()) 
				{
					//la carta che è stata selezionata all'interno della listView
					cartaGiocata = Carta.valueOf(listCarte.getSelectionModel().getSelectedItem());
				}
				//se ci stiamo riferendo ad un turno di attacco rimuoverò la carta giocata dall'attaccante nella listView 
				players[posizioneGiocatoreAttaccante].getMano().remove(cartaGiocata);
			}
			else 
			{
				if(listCarte.getSelectionModel().getSelectedItem() == null && !players[posizioneGiocatoreDifensore].isRobot()) {
					throw new IllegalArgumentException();
				}
				if(!players[posizioneGiocatoreDifensore].isRobot()) 
				{
					//la carta che è stata selezionata all'interno della listView
					cartaGiocata = Carta.valueOf(listCarte.getSelectionModel().getSelectedItem());
				}
				//se ci stiamo riferendo ad un turno di difesa rimuoverò la carta giocata dal difensore nella listView e controllo se si è difeso correttamente 
				partita.gioca(players[posizioneGiocatoreAttaccante], players[posizioneGiocatoreDifensore], Carta.valueOf(nomeCarta), cartaGiocata);
				players[posizioneGiocatoreDifensore].getMano().remove(cartaGiocata);
			}
			//controllo tramite il metodo se la partita è terminata (i giocatori hanno raggiunto i 5 goal)
			if(!partita.finePartita(posizioneGiocatoreAttaccante)) 
			{
				//scarto dal mazzo la carta giocata (la rimetto in fondo al mazzo) e salvo le mani, il mazzo, il punteggio e l'ultimo turno giocato della partita
				mazzo.scarta(cartaGiocata);
				//salvo tutte le informazioni utili della partita tramite il costruttore della classe "Salvataggio"
				s = new Salvataggio(g.getGara(codicePartita));
				s.salvaMani();
				s.salvaMazzo();
				s.salvaPunteggio();
				partita.salvaTurnoGara(cartaGiocata, cartaAtt);
				//controllo se è stata giocata una di queste carte, se cosi fosse passeremo al form del rigore, altrimenti rimaremmo in questo form
				if(cartaGiocata.equals(Carta.RIGORE) || ((cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_SX)) || (cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_C)) || (cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_DX))))
				{
					//il form del rigore che ci passerà nel caso in cui sia stata giocata una carta "rigore" o "portiere"
					FXMLLoader loader = new FXMLLoader(getClass().getResource("FormRigore.fxml"));
					loader.load();
					FormRigoreController form = loader.getController();
					form.copiaCodice(codicePartita);
					form.copiaCartaGiocata(cartaAtt);
					alert.passaAlForm("/application/FormRigore.fxml", event);
				}
				else
				{
					alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
				}
			}
			else 
			{
				if(codicePartita.charAt(0) == 'p')
				{
					partita.showFinePartita(event,players[posizioneGiocatoreAttaccante].getAlias(),leaderboard,alert);
				}
				else 
				{
					torneo = g.getTorneo(codicePartita);
					torneo.finePartita(event, players[posizioneGiocatoreAttaccante].getAlias(), players[posizioneGiocatoreDifensore].getAlias(), alert, leaderboard);
				}
			}
		}catch(IllegalArgumentException e) {
			alert.mostraErrore("Prima di premere il bottone seleziona una carta", "ERRORE");
		}catch(IOException e) {
			event.consume();
		}
	}

	@FXML
	public void btnPassaTurno(ActionEvent event) throws IOException 
	{
		//in ogni turno di difesa si pesca una carta se si è subito goal, si pescano due carte se non si è subito goal
		players[posizioneGiocatoreDifensore].getMano().add(this.mazzo.pesca());
		if(partita.checkGiocaTurno(players[posizioneGiocatoreAttaccante])) 
		{
			//caso particolare di carta speciale mister, l'attaccante scarta la carta e pesca altre due carte e passa il turno
			if(cartaAtt.equals(Carta.MISTER)) 
			{
				players[posizioneGiocatoreAttaccante].getMano().add(this.mazzo.pesca());
				players[posizioneGiocatoreAttaccante].getMano().add(this.mazzo.pesca());
			}
			else
			{
				//non è stata pescata la carta mister, quindi il difensore non si è potuto difendere e per questo motivo esso ha subito goal
				players[posizioneGiocatoreAttaccante].aggiungiGoal();
				partita.setCartePescate(1);
			}
		}
		else 
		{
			if(turno.equals("d")) 
			{
				//controllo l'unico caso in cui l'attaccante non sia riuscito ad attaccare, altrimenti il difensore ha subito goal
				if(!cartaAtt.equals(Carta.INDICATORE_GOAL)) 
				{
					players[posizioneGiocatoreAttaccante].aggiungiGoal();
					partita.setCartePescate(1);
				}
				else
				{
					partita.setCartePescate(2);
				}
			}
		}
		//controllo tramite il metodo se la partita è terminata (i giocatori hanno raggiunto i 5 goal)
		if(!partita.finePartita(posizioneGiocatoreAttaccante)) 
		{
			//salvo le mani, il mazzo, il punteggio e l'ultimo turno giocato della partita
			s = new Salvataggio(g.getGara(codicePartita));
			s.salvaMani();
			s.salvaMazzo();
			s.salvaPunteggio();
			partita.salvaTurnoGara(cartaGiocata, cartaAtt);
			alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
		}
		else 
		{
			//se la partita è terminata eseguo il metodo che mi permetterà di chiudere la partita
			if(codicePartita.charAt(0) == 'p')
			{
				partita.showFinePartita(event,players[posizioneGiocatoreAttaccante].getAlias(),leaderboard,alert);
			}
			else 
			{
				torneo = g.getTorneo(codicePartita);
				torneo.finePartita(event, players[posizioneGiocatoreAttaccante].getAlias(), players[posizioneGiocatoreDifensore].getAlias(), alert, leaderboard);
			}
		}
	}

	@FXML
	public void btnSospendiGara(ActionEvent event) throws IOException
	{
		//l'utente ha intenzione di sospendere la partita,lo richiedo per conferma con un alert, sospendo la partita e torno al form principale
		if(alert.chiediConferma("Sei sicuro di voler sospendere la partita?", "ATTENZIONE")) 
		{
			if(codicePartita.charAt(0) == 'p')
				alert.mostraInformazione("Operazione eseguita con successo. La partita avente il codice '"+codicePartita+"' è stata sospesa", "OPERAZIONE COMPLETATA");
			else
				alert.mostraInformazione("Operazione eseguita con successo. Il torneo avente il codice '"+codicePartita+"' è stata sospesa", "OPERAZIONE COMPLETATA");
			alert.passaAlForm("/application/FormPrincipale.fxml", event);
		}
	}

	@FXML
	public void selezionaCarta(MouseEvent event) 
	{
		try {
			if(listCarte.getSelectionModel().getSelectedItem().equals(null)) {
				throw new NullPointerException();
			}
			//ottengo la carta selezionata dalla listView
			Carta c = Carta.valueOf(listCarte.getSelectionModel().getSelectedItem());
			if(turno.equals("a")) 
			{
				//siamo in un turno di attacco, quindi non permettiamo di cliccare una carta difensiva
				if(c.getTipologia().equals(Tipologia.DIFESA) || c.equals(Carta.FUORIGIOCO) || c.equals(Carta.VAR))
				{
					listCarte.getSelectionModel().clearSelection();
				}
			}
			else 
			{
				//siamo in un turno di difesa, quindi non permettiamo di cliccare una carta offensiva
				if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.GOAL) || c.equals(Carta.MISTER))
				{
					listCarte.getSelectionModel().clearSelection();
				}
			}
		}catch(NullPointerException e) {
			event.consume();
		}
	}

	//metodo che permette di stampare i giocatori nella label con il loro attuale punteggio
	public void stampaGiocatoriLabel()
	{
		//posso avere tra i 2 e 4 giocatori, i controlli servono per il numero di giocatori che stanno giocando la partita
		if(players.length >= 2)
		{
			lblGiocatore1.setText(players[0].getAlias() + ": " + players[0].getPunteggio() + " GOAL");
			lblGiocatore2.setText(players[1].getAlias() + ": " + players[1].getPunteggio() + " GOAL");
		}
		if(players.length >= 3)
		{
			lblGiocatore3.setText(players[2].getAlias() + ": " + players[2].getPunteggio() + " GOAL");
		}
		if(players.length == 4)
		{
			lblGiocatore4.setText(players[3].getAlias() + ": " + players[3].getPunteggio() + " GOAL");
		}
	}

	//metodo "initialize". viene eseguito ogni volta che si apre questo form
	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		//controllo se viene passato da form a form il codice della partita
		if(!(codicePartita == null)) 
		{
			//eseguo con un blocco try/catch il controllo la lettura corretta dei file di testo
			try
			{
				partita = (Partita) g.getGara(codicePartita);
				mazzo = partita.getMazzo();
				players = partita.getGiocatori();
				partita.leggiTurno();
				turno = partita.getTurno();
				posizioneGiocatoreAttaccante = partita.getPosAttaccante();
				posizioneGiocatoreDifensore = partita.getPosDifensore();
				nomeCarta = partita.getNomeCarta();
				cartePescate = partita.getCartePescate();
				//controllo se è un turno di attacco o di difesa
				if(turno.equals("a")) 
				{
					if(players[posizioneGiocatoreAttaccante].isRobot()) 
					{
						Robot robot = new Robot(players[posizioneGiocatoreAttaccante]);
						cartaGiocata = robot.cartaGiocata('a');
						listCarte.setMouseTransparent(true);
					}
					//restituisce la mano del giocatore attaccante e setto la label (colore e contenuto) al giocatore attaccante
					nomiCarte = players[posizioneGiocatoreAttaccante].getManoNomi();
					lblTurnoAttacco.setTextFill(Color.BLUE);
					lblTurnoAttacco.setText("TURNO DI ATTACCO: " + players[posizioneGiocatoreAttaccante].getAlias());
					if(players[posizioneGiocatoreAttaccante].isRobot()) {
						lblTurnoAttacco.setText(lblTurnoAttacco.getText()+" (Robot)");
					}
					String c1;
					String c2;
					Carta cPescata1;
					Carta cPescata2;
					if(cartePescate == 1) 
					{
						cPescata1 = Carta.valueOf(nomiCarte[nomiCarte.length-1]);
						c1 = cPescata1.getStampa() != null ? cPescata1.getStampa() : cPescata1.name();
						lblInfoUtente.setText("Hai pescato la carta "+c1);
					}
					else if(cartePescate == 2)
					{
						cPescata1 = Carta.valueOf(nomiCarte[nomiCarte.length-1]);
						cPescata2 = Carta.valueOf(nomiCarte[nomiCarte.length-2]);
						c1 = cPescata1.getStampa() != null ? cPescata1.getStampa() : cPescata1.name();
						c2 = cPescata2.getStampa() != null ? cPescata2.getStampa() : cPescata2.name();
						lblInfoUtente.setText("Hai pescato le carte "+c1+" e "+c2);
					}
					
					//metodo che restituisce se è presente nella mano almeno una carta di attacco (possibile attaccare)
					if(partita.checkGiocaTurno(players[posizioneGiocatoreAttaccante])) 
					{
						if(!players[posizioneGiocatoreAttaccante].isRobot()) 
						{
							//è possibile attaccare, attivo il bottone "gioca carta" e rimane disabilitato il bottone "passa turno"
							btnGiocaCarta.setVisible(true);
							btnGiocaCarta.setLayoutX(374);
							btnGiocaCarta.setLayoutY(554);
							btnSospendiGara.setVisible(true);
						}
					}
					else 
					{
						lblInfoUtente.setText("Non hai carte con le quali attaccare. Sei costretto a passare il turno");
						//non è possibile attaccare, attivo il bottone "passa turno" e rimane disabilitato il bottone "gioca carta"
						if(!players[posizioneGiocatoreAttaccante].isRobot()) 
						{
							btnPassaTurno.setVisible(true);
							btnPassaTurno.setLayoutX(374);
							btnPassaTurno.setLayoutY(554);
							btnSospendiGara.setVisible(true);
						}
						listCarte.setDisable(true);
						//non è possibile giocare alcuna carta di attacco, allora la settiamo noi di default
						cartaGiocata = Carta.INDICATORE_GOAL;
					}
				}
				else 
				{
					if(players[posizioneGiocatoreDifensore].isRobot()) 
					{
						Robot robot = new Robot(players[posizioneGiocatoreDifensore]);
						cartaGiocata = robot.cartaGiocata('d');
						listCarte.setMouseTransparent(true);
					}
					//restituisce la mano del giocatore difendente e setto la label (colore e contenuto) al giocatore difendente
					nomiCarte = players[posizioneGiocatoreDifensore].getManoNomi();
					lblTurnoAttacco.setTextFill(Color.RED);
					lblTurnoAttacco.setText("TURNO DI DIFESA: " + players[posizioneGiocatoreDifensore].getAlias());
					if(players[posizioneGiocatoreDifensore].isRobot()) {
						lblTurnoAttacco.setText(lblTurnoAttacco.getText()+" (Robot)");
					}
					if(partita.checkGiocaTurno(players[posizioneGiocatoreAttaccante])) 
					{
						//metodo per salvare la carta giocata e controllare se il difensore può difendersi oppure no
						checkTurnoDifensore();
					}
					else
					{
						//il giocatore non ha carte di attacco al momento in mano, controllo se era l'ultima oppure non ha attaccato
						if(Carta.valueOf(nomeCarta).equals(Carta.INDICATORE_GOAL)) 
						{
							lblInfoUtente.setText("L'attaccante non ha attaccato. Passa il turno");
							if(!players[posizioneGiocatoreDifensore].isRobot()) 
							{
								//il giocatore attaccante non ha attaccato, il difensore non si difende e attivo la visualizzazione del bottone "passa turno"
								btnPassaTurno.setVisible(true);
								btnPassaTurno.setLayoutX(374);
								btnPassaTurno.setLayoutY(554);
								btnSospendiGara.setVisible(true);
							}
							listCarte.setDisable(true);
							cartaAtt = Carta.INDICATORE_GOAL;
						}
						//caso in cui l'attaccante non abbia più carte offensive in mano, ma comunque ha giocato una carta di attacco nel turno precedente
						else 
						{
							//metodo per salvare la carta giocata e controllare se il difensore può difendersi oppure no
							checkTurnoDifensore();
						}
					}
					//setto il campo "imageView" alla carta giocata dall'attaccante (se non ha giocato nulla visualizzo la carta di default)
					imgGiocata.setImage(cartaAtt.getImmagine());
				}
			}
			catch(IOException e)
			{
				System.out.println(e.getMessage());
			}
			
			//metodo per stampare l'attuale punteggio della partita nella label
			stampaGiocatoriLabel();
			ObservableList<String> items =FXCollections.observableArrayList(nomiCarte);
			listCarte.setItems(items);
			ImageView[] _imageView = new ImageView[1];
			listCarte.setCellFactory(param -> 
			{
				return new ListCell<String>() 
				{	
					ImageView imageView = new ImageView();
					@Override
					public void updateItem(String name, boolean empty) 
					{
						super.updateItem(name, empty);
						if (empty) 
						{
							setText(null);
							setGraphic(null);
							setStyle("");
						} 
						else 
						{
							switch(name) 
							{
							case "ATTACCANTE": imageView.setImage(Carta.ATTACCANTE.getImmagine()); break;
							case "BOMBER_VERO": imageView.setImage(Carta.BOMBER_VERO.getImmagine()); break;
							case "RIGORE": imageView.setImage(Carta.RIGORE.getImmagine()); break;
							case "ROVESCIATA_DELLANNO": imageView.setImage(Carta.ROVESCIATA_DELLANNO.getImmagine()); break;
							case "TIRO_DOMENICA": imageView.setImage(Carta.TIRO_DOMENICA.getImmagine()); break;
							case "DIFENSORE": imageView.setImage(Carta.DIFENSORE.getImmagine()); break;
							case "DIFENSORE_ROCCIA": imageView.setImage(Carta.DIFENSORE_ROCCIA.getImmagine()); break;
							case "PORTIERE": imageView.setImage(Carta.PORTIERE.getImmagine()); break;
							case "INDICATORE_GOAL": imageView.setImage(Carta.INDICATORE_GOAL.getImmagine()); break;
							case "FUORIGIOCO": imageView.setImage(Carta.FUORIGIOCO.getImmagine()); break;
							case "GOAL": imageView.setImage(Carta.GOAL.getImmagine()); break;
							case "MISTER": imageView.setImage(Carta.MISTER.getImmagine()); break;
							case "VAR": imageView.setImage(Carta.VAR.getImmagine()); break;
							}
							setGraphic(imageView);
							_imageView[0] = imageView;
							if(turno.equals("a")) 
							{
								if((players[posizioneGiocatoreAttaccante].isRobot() && this.getIndex() == getIndexCartaGiocata(posizioneGiocatoreAttaccante,items))) 
								{
									setStyle("-fx-control-inner-background: blue;");
								}
							}
							else 
							{
								if((players[posizioneGiocatoreDifensore].isRobot() && this.getIndex() == getIndexCartaGiocata(posizioneGiocatoreDifensore,items))) 
								{ 
									setStyle("-fx-control-inner-background: red;");
								}
							}
						}
					}
				};
			});
			//eseguo il codice solo se siamo in un turno di attacco e l'attaccante è un robot o il caso opposto
			if((turno.equals("a") && players[posizioneGiocatoreAttaccante].isRobot()) ||  (turno.equals("d") && players[posizioneGiocatoreDifensore].isRobot()))
			{
				Thread taskThread = new Thread(() -> 
				{
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() 
					{
						@Override
						public void run() 
						{
							btnGiocaCarta.fire();
							/*
							if(!_imageView[0].getImage().getUrl().equals(Carta.INDICATORE_GOAL.getImmagine().getUrl()) && !_imageView[0].getImage().getUrl().equals(Carta.ROVESCIATA_DELLANNO.getImmagine().getUrl())
									&& !_imageView[0].getImage().getUrl().equals(Carta.TIRO_DOMENICA.getImmagine().getUrl())) {
								System.out.println("fire gioca carta");
								btnGiocaCarta.fire();
							}else {
								System.out.println("fire passa turno");
								btnPassaTurno.fire();
							}*/
						}
					});
				});
				taskThread.start();
			}
		}
	}
	private int getIndexCartaGiocata(int posGiocatore, ObservableList<String> items) {
		int cellaDaColorare = -1;
		for (int i = 0; i < items.size(); i++) {
	        String name = items.get(i);
	        if (players[posizioneGiocatoreAttaccante].isRobot() && name.equals(cartaGiocata.name())) {
	        	cellaDaColorare = i;
	            break;
	        }
	    }
		return cellaDaColorare;
	}
	private void checkTurnoDifensore()
	{
		//la carta che è stata giocata dal giocatore attaccante
		cartaAtt = Carta.valueOf(nomeCarta);
		//controllo se il giocatore ha almeno una carta di difesa in mano (in modo che possa difendersi)
		if(partita.checkGiocaTurno(cartaAtt, players[posizioneGiocatoreDifensore].getMano()))
		{
			if(!players[posizioneGiocatoreDifensore].isRobot()) 
			{
				//ha una carta difensiva in mano, setto entrambi i bottoni visualizzabili
				btnPassaTurno.setVisible(true);
				btnGiocaCarta.setVisible(true);
				btnSospendiGara.setVisible(true);
			}
		}
		else 
		{
			String output;
			if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA) || cartaAtt.equals(Carta.MISTER)) 
			{
				output = "La carta "+cartaAtt+" non è difendibile. Sei costretto a passare il turno";
				if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA))
				{
					output += " subendo un gol.";
				}
				lblInfoUtente.setText(output);
			}
			else 
			{
				lblInfoUtente.setText("Non hai carte di difesa con le quali difenderti. Sei costretto a passare il turno e subire gol");
			}
			if(!players[posizioneGiocatoreDifensore].isRobot()) 
			{
				//non è possibile difendersi, non ha carte difensive in mano e attivo la visualizzazione del bottone "passa turno"
				btnPassaTurno.setVisible(true);
				btnPassaTurno.setLayoutX(374);
				btnPassaTurno.setLayoutY(554);
				btnSospendiGara.setVisible(true);
				listCarte.setDisable(true);
			}
		}
	}
}