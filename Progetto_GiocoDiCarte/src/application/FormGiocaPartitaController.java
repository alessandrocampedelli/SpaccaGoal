package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.shape.Rectangle;
import classi.Robot;
import classi.Alert_cambiaForm;
import classi.Carta;
import classi.Salvataggio;
import classi.Gare;
import classi.Giocatore;
import classi.Partita;
import classi.Torneo;
import classi.Tipologia;
import classi.Leaderboard;
import javafx.scene.paint.Color;
import classi.Mazzo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//la classe che permette di giocare la partita
public class FormGiocaPartitaController implements Initializable
{
	//creo un oggetto della classe "Gare"
	Gare g = new Gare();
	//creo un oggetto della classe "Leaderboard" per il salvataggio
	Leaderboard leaderboard = new Leaderboard();
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	//le variabili di tipo salvataggio, partita, torneo, mazzo e giocatore che verranno inizializzate successivamente
	Salvataggio s;
	Partita partita;
	Torneo torneo = null;
	Mazzo mazzo;
	Giocatore[] players;
	//la mano del giocatore che sta giocando la partita
	String[] nomiCarte;
	//la carta che ha giocato il giocatore che sta giocando la partita
	Carta cartaGiocata;
	//la carta che ha giocato l'attaccante se siamo in un turno difensivo
	Carta cartaAtt;
	//il numero di carte che pescherà il giocatore per la stampa nelle label
	int cartePescate;
	//la variabile che indica se ci stiamo riferendo ad un turno di attacco o un turno di difesa ("d" per difesa e "a" per attacco)
	String turno;
	//la posizione del giocatore di attacco e del giocatore di difesa
	int posizioneGiocatoreAttaccante;
	int posizioneGiocatoreDifensore;
	//il nome della carta che è stato giocato dall'attaccante se ci troviamo in un turno di difesa
	String nomeCarta;
	
	//il codice della partita che si sta giocando
	static String codicePartita;
	//questo metodo permette di passare il codice della partita dal form precedente (FormIniziaPartitaController)
	public void copiaCodice(String codice) 
	{
		codicePartita = codice;
	}
	
	//il rettangolo che contiene la carta giocata dall'attaccante in un turno difensivo
	@FXML
	Rectangle rettangoloCarta = new Rectangle();
	//la label che dice se è un turno di attacco o di difesa e quale giocatore deve giocare il turno
	@FXML
	private Label lblTurnoAttacco;
	//le label con i vari giocatori che serviranno per stampare i punteggi attuali
	@FXML
	private Label lblGiocatore1;
	@FXML
	private Label lblGiocatore2;
	@FXML
	private Label lblGiocatore3;
	@FXML
	private Label lblGiocatore4;
	//la label per le stampe delle carte pescate e le informazioni all'utente necessarie per la partita
	@FXML
	private Label lblInfoUtente;
	//la lista dove stamperemo graficamente le carte della mano del giocatore
	@FXML
	private ListView<String> listCarte = new ListView<String>();
	//i bottoni "gioca carta" per giocare la carta selezionata, "passa turno" per saltare il turno e "sospendi gara" per sospendere la partita
	@FXML
	Button btnGiocaCarta = new Button();
	@FXML
	Button btnPassaTurno = new Button();
	@FXML
	Button btnSospendiGara = new Button();
	//l'immagine che sarà visibile solo in un turno di difesa con l'immagine giocata dal calciatore attaccante
	@FXML
	ImageView imgGiocata = new ImageView();

	//il bottone che permette di giocare la carta selezionata dalla listView
	@FXML
	public void btnGiocaCarta(ActionEvent event)
	{
		try 
		{
			//mi chiedo se è un turno di attacco
			if(turno.equals("a"))
			{
				//mi chiedo se è stata selezionata una carta dalla listView, altrimenti mando un alert all'utente
				if(listCarte.getSelectionModel().getSelectedItem() == null && !players[posizioneGiocatoreAttaccante].isRobot()) 
				{
					throw new IllegalArgumentException();
				}
				//mi chiedo se il giocatore non è un robot
				if(!players[posizioneGiocatoreAttaccante].isRobot()) 
				{
					//la carta che è stata selezionata all'interno della listView
					cartaGiocata = Carta.valueOf(listCarte.getSelectionModel().getSelectedItem());
				}
				//rimuovo la carta giocata dall'attaccante nella listView 
				players[posizioneGiocatoreAttaccante].getMano().remove(cartaGiocata);
			}
			else 
			{
				//mi chiedo se è stata selezionata una carta dalla listView, altrimenti mando un alert all'utente
				if(listCarte.getSelectionModel().getSelectedItem() == null && !players[posizioneGiocatoreDifensore].isRobot()) 
				{
					throw new IllegalArgumentException();
				}
				//mi chiedo se il giocatore non è un robot
				if(!players[posizioneGiocatoreDifensore].isRobot()) 
				{
					//la carta che è stata selezionata all'interno della listView
					cartaGiocata = Carta.valueOf(listCarte.getSelectionModel().getSelectedItem());
				}
				//controllo se si è difeso correttamente con il metodo "gioca" e rimuovo la carta giocata dal difensore nella listView 
				partita.gioca(players[posizioneGiocatoreAttaccante], players[posizioneGiocatoreDifensore], Carta.valueOf(nomeCarta), cartaGiocata);
				players[posizioneGiocatoreDifensore].getMano().remove(cartaGiocata);
			}
			//controllo tramite il metodo se la partita non è terminata
			if(!partita.finePartita(posizioneGiocatoreAttaccante)) 
			{
				//scarto dal mazzo la carta giocata (la rimetto in fondo al mazzo)
				mazzo.scarta(cartaGiocata);
				s = new Salvataggio(g.getGara(codicePartita));
				//salvo le mani, il mazzo, il punteggio e l'ultimo turno giocato della partita
				s.salvaMani();
				s.salvaMazzo();
				s.salvaPunteggio();
				partita.salvaTurnoGara(cartaGiocata, cartaAtt);
				//controllo se è stata giocata una di queste carte, se cosi fosse passiamo al form del rigore, altrimenti rimaniamo in questo form
				if(cartaGiocata.equals(Carta.RIGORE) || ((cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_SX)) || (cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_C)) || (cartaGiocata.equals(Carta.PORTIERE) && cartaAtt.equals(Carta.RIGORE_DX))))
				{
					//il form del rigore che ci passerà nel caso in cui sia stata giocata una carta "rigore" o "portiere"
					FXMLLoader loader = new FXMLLoader(getClass().getResource("FormRigore.fxml"));
					loader.load();
					FormRigoreController form = loader.getController();
					//passo il codice della partita e la carta giocata dall'attaccante nel form rigore
					form.copiaCodice(codicePartita);
					form.copiaCartaGiocata(cartaAtt);
					//passo al form del rigore
					alert.passaAlForm("/application/FormRigore.fxml", event);
				}
				else
				{
					//passo al form gioca partita con il giocatore successivo
					alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
				}
			}
			else 
			{
				//la partita è terminata, mi chiedo se è terminata una partita o un torneo
				if(codicePartita.charAt(0) == 'p')
				{
					//eseguo il metodo della classe partita "showFinePartita"
					partita.showFinePartita(event,players[posizioneGiocatoreAttaccante].getAlias(),leaderboard,alert);
				}
				else 
				{
					//converto la partita in oggetto "Torneo"
					torneo = g.getTorneo(codicePartita);
					//eseguo il metodo della classe torneo "finePartita"
					torneo.finePartita(event, players[posizioneGiocatoreAttaccante].getAlias(), players[posizioneGiocatoreDifensore].getAlias(), alert, leaderboard);
				}
			}
		}
		catch(IllegalArgumentException e) 
		{
			//mando un alert all'utente mostrando l'errore che non ha selezionato una carta 
			alert.mostraErrore("Prima di premere il bottone seleziona una carta", "ERRORE");
		}
		catch(IOException e) 
		{
			//consumo l'eccezione che si è verificata
			event.consume();
		}
	}

	//il bottone che permette di gestire il "passa turno"
	@FXML
	public void btnPassaTurno(ActionEvent event) throws IOException 
	{
		//in ogni turno di difesa si pesca una carta se si è subito goal, si pescano due carte se non si è subito goal
		players[posizioneGiocatoreDifensore].getMano().add(this.mazzo.pesca());
		//controllo se il giocatore può giocare il turno di attacco
		if(partita.checkGiocaTurno(players[posizioneGiocatoreAttaccante])) 
		{
			//caso particolare di carta speciale mister, pesca altre due carte e passa il turno
			if(cartaAtt.equals(Carta.MISTER)) 
			{
				players[posizioneGiocatoreAttaccante].getMano().add(this.mazzo.pesca());
				players[posizioneGiocatoreAttaccante].getMano().add(this.mazzo.pesca());
			}
			else
			{
				//non è stata pescata la carta mister, quindi il difensore non si è potuto difendere e per questo motivo esso ha subito goal
				players[posizioneGiocatoreAttaccante].aggiungiGoal();
				//il difensore pescherà solo una carta
				partita.setCartePescate(1);
			}
		}
		else 
		{
			//mi chiedo se è un turno difensivo
			if(turno.equals("d")) 
			{
				//se l'attaccante non ha potuto attaccare (non aveva carte di attacco in mano) o ha giocato la carta mister, il difensore ha subito goal
				if(!(cartaAtt.equals(Carta.INDICATORE_GOAL) || cartaAtt.equals(Carta.MISTER)))
				{
					//l'attaccante ha fatto goal, lo aggiungo al suo punteggio
					players[posizioneGiocatoreAttaccante].aggiungiGoal();
					//il difensore pescherà solo una carta
					partita.setCartePescate(1);
				}
				else
				{
					//il difensore pescherà due carte
					partita.setCartePescate(2);
				}
			}
		}
		//controllo tramite il metodo se la partita non è terminata
		if(!partita.finePartita(posizioneGiocatoreAttaccante)) 
		{
			//salvo le mani, il mazzo, il punteggio e l'ultimo turno giocato della partita
			s = new Salvataggio(g.getGara(codicePartita));
			s.salvaMani();
			s.salvaMazzo();
			s.salvaPunteggio();
			partita.salvaTurnoGara(cartaGiocata, cartaAtt);
			//passo al form gioca partita con il giocatore successivo
			alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
		}
		else 
		{
			//la partita è terminata, mi chiedo se è terminata una partita o un torneo
			if(codicePartita.charAt(0) == 'p')
			{
				//eseguo il metodo della classe partita "showFinePartita"
				partita.showFinePartita(event,players[posizioneGiocatoreAttaccante].getAlias(),leaderboard,alert);
			}
			else 
			{
				//converto la partita in oggetto "Torneo"
				torneo = g.getTorneo(codicePartita);
				//eseguo il metodo della classe torneo "finePartita"
				torneo.finePartita(event, players[posizioneGiocatoreAttaccante].getAlias(), players[posizioneGiocatoreDifensore].getAlias(), alert, leaderboard);
			}
		}
	}

	//il bottone che permette di sospendere la gara in qualsiasi momento
	@FXML
	public void btnSospendiGara(ActionEvent event) throws IOException
	{
		//chiedo la conferma all'utente se ha veramente intenzione di sospendere la partita, se clicca il bottone "OK" sospendo la partita
		if(alert.chiediConferma("Sei sicuro di voler sospendere la partita?", "ATTENZIONE")) 
		{
			if(codicePartita.charAt(0) == 'p')
			{
				//mando un alert all'utente che la partita è stata sospesa
				alert.mostraInformazione("Operazione eseguita con successo. La partita avente il codice '" + codicePartita + "' è stata sospesa", "OPERAZIONE COMPLETATA");
			}
			else
			{
				//mando un alert all'utente che il torneo è stato sospeso
				alert.mostraInformazione("Operazione eseguita con successo. Il torneo avente il codice '" + codicePartita + "' è stata sospesa", "OPERAZIONE COMPLETATA");
			}
			//passo al form principale
			alert.passaAlForm("/application/FormPrincipale.fxml", event);
		}
	}

	//il bottone che permette di selezionare una carta dipendendo dal turno difensivo o offensivo
	@FXML
	public void selezionaCarta(MouseEvent event) 
	{
		try 
		{
			//mi chiedo se è stata selezionata una carta dalla listView, altrimenti mando un alert all'utente
			if(listCarte.getSelectionModel().getSelectedItem().equals(null)) 
			{
				throw new NullPointerException();
			}
			//la carta selezionata dalla listView
			Carta c = Carta.valueOf(listCarte.getSelectionModel().getSelectedItem());
			//controllo se siamo in un turno di attacco
			if(turno.equals("a")) 
			{
				//siamo in un turno di attacco, quindi non permettiamo di cliccare una carta difensiva
				if(c.getTipologia().equals(Tipologia.DIFESA) || c.equals(Carta.FUORIGIOCO) || c.equals(Carta.VAR))
				{
					//cancello il selezionamento della carta di difesa
					listCarte.getSelectionModel().clearSelection();
				}
			}
			else 
			{
				//siamo in un turno di difesa, quindi non permettiamo di cliccare una carta offensiva
				if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.GOAL) || c.equals(Carta.MISTER))
				{
					//cancello il selezionamento della carta di attacco
					listCarte.getSelectionModel().clearSelection();
				}
			}
		}
		catch(NullPointerException e) 
		{
			//consumo l'eccezione che si è verificata
			event.consume();
		}
	}

	//metodo che permette di stampare i giocatori nella label con il loro attuale punteggio
	public void stampaGiocatoriLabel()
	{
		//posso avere tra i 2 e 4 giocatori nella partita, se i giocatori sono almeno 2 stampo le prime due label
		if(players.length >= 2)
		{
			lblGiocatore1.setText(players[0].getAlias() + ": " + players[0].getPunteggio() + " GOAL");
			lblGiocatore2.setText(players[1].getAlias() + ": " + players[1].getPunteggio() + " GOAL");
		}
		//se i giocatori sono almeno 3 stampo anche la terza label
		if(players.length >= 3)
		{
			lblGiocatore3.setText(players[2].getAlias() + ": " + players[2].getPunteggio() + " GOAL");
		}
		//se i giocatori sono 4 stampo anche la quarta label
		if(players.length == 4)
		{
			lblGiocatore4.setText(players[3].getAlias() + ": " + players[3].getPunteggio() + " GOAL");
		}
	}

	//metodo che viene eseguito all'apertura del programma
	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		//controllo se viene passato da form a form il codice della partita
		if(!(codicePartita == null)) 
		{
			//eseguo con un blocco try/catch il controllo la lettura corretta dei file di testo
			try
			{
				//converto la gara in oggetto "Partita"
				partita = (Partita) g.getGara(codicePartita);
				//inizializzo il mazzo della gara, i giocatori della partita e il tunro
				mazzo = partita.getMazzo();
				players = partita.getGiocatori();
				partita.leggiTurno();
				turno = partita.getTurno();
				//inizializzo la posizione del giocatore attaccante e difendente, il nome della carta giocata e il numero di carte pescate
				posizioneGiocatoreAttaccante = partita.getPosAttaccante();
				posizioneGiocatoreDifensore = partita.getPosDifensore();
				nomeCarta = partita.getNomeCarta();
				cartePescate = partita.getCartePescate();
				//controllo se è un turno di attacco
				if(turno.equals("a")) 
				{
					//setto la label di colore blu e stampo il nome del giocatore attaccante
					lblTurnoAttacco.setTextFill(Color.BLUE);
					lblTurnoAttacco.setText("TURNO DI ATTACCO: " + players[posizioneGiocatoreAttaccante].getAlias());
					//mi chiedo se il giocatore è un robot
					if(players[posizioneGiocatoreAttaccante].isRobot()) 
					{
						//creo l'oggetto robot del giocatore attaccante 
						Robot robot = new Robot(players[posizioneGiocatoreAttaccante]);
						//mi restituisce la carta che giocherà il robot intelligente
						cartaGiocata = robot.cartaGiocata('a');
						//stampo nella label che il giocatore stampato è un robot
						lblTurnoAttacco.setText(lblTurnoAttacco.getText()+" (Robot)");
						listCarte.setMouseTransparent(true);
						//seleziono la carta che giocherà il robot
						listCarte.getSelectionModel().select(cartaGiocata.name());
						btnSospendiGara.setVisible(false);
					}
					//restituisce la mano del giocatore attaccante
					nomiCarte = players[posizioneGiocatoreAttaccante].getManoNomi();
					String c1;
					String c2;
					Carta cPescata1;
					Carta cPescata2;
					//il numero di carte pescate al giocatore può essere 1 o 2
					if(cartePescate == 1) 
					{
						//determino la carta pescata dal giocatore prendendo l'ultima posizione dell'array (ultima carta pescata)
						cPescata1 = Carta.valueOf(nomiCarte[nomiCarte.length-1]);
						c1 = cPescata1.getStampa() != null ? cPescata1.getStampa() : cPescata1.name();
						//stampo nella label la carta pescata
						lblInfoUtente.setText("Hai pescato la carta " + c1);
					}
					else if(cartePescate == 2)
					{
						//determino le carte pescate dal giocatore prendendo l'ultima e la penultima posizione dell'array (ultima e penultima carta pescata)
						cPescata1 = Carta.valueOf(nomiCarte[nomiCarte.length-1]);
						cPescata2 = Carta.valueOf(nomiCarte[nomiCarte.length-2]);
						c1 = cPescata1.getStampa() != null ? cPescata1.getStampa() : cPescata1.name();
						c2 = cPescata2.getStampa() != null ? cPescata2.getStampa() : cPescata2.name();
						//stampo nella label le carte pescate
						lblInfoUtente.setText("Hai pescato le carte " + c1 + " e " + c2);
					}
					//metodo che restituisce se è presente nella mano almeno una carta di attacco (possibile attaccare)
					if(partita.checkGiocaTurno(players[posizioneGiocatoreAttaccante])) 
					{
						//mi chiedo se il giocatore non è un robot
						if(!players[posizioneGiocatoreAttaccante].isRobot()) 
						{
							//è possibile attaccare, attivo la visualizzazione del bottone "gioca carta" come unico bottone centrale
							btnGiocaCarta.setVisible(true);
							btnGiocaCarta.setLayoutX(462);
							btnGiocaCarta.setLayoutY(648);
						}
					}
					else 
					{
						//stampo che l'utente non ha carte con la quale attaccare e quindi passerà il turno
						lblInfoUtente.setText("Non hai carte con le quali attaccare. Sei costretto a passare il turno");
						//mi chiedo se il giocatore non è un robot
						if(!players[posizioneGiocatoreAttaccante].isRobot()) 
						{
							//è possibile attaccare, attivo la visualizzazione del bottone "passa turno" come unico bottone centrale
							btnPassaTurno.setVisible(true);
							btnPassaTurno.setLayoutX(462);
							btnPassaTurno.setLayoutY(648);
						}
						//disabilito l'utilizzo della listView
						listCarte.setDisable(true);
						//non è possibile giocare alcuna carta di attacco, allora settiamo la carta all'indicatore goal
						cartaGiocata = Carta.INDICATORE_GOAL;
					}
				}
				else 
				{
					//setto la label di colore rosso e stampo il nome del giocatore difensore
					lblTurnoAttacco.setTextFill(Color.RED);
					lblTurnoAttacco.setText("TURNO DI DIFESA: " + players[posizioneGiocatoreDifensore].getAlias());
					//mi chiedo se il giocatore è un robot
					if(players[posizioneGiocatoreDifensore].isRobot()) 
					{
						//creo l'oggetto robot del giocatore difensore 
						Robot robot = new Robot(players[posizioneGiocatoreDifensore]);
						//setta la carta giocata dall'attaccante
						robot.setCartaGiocata(Carta.valueOf(nomeCarta));
						//mi restituisce la carta difensiva che giocherà il robot intelligente
						cartaGiocata = robot.cartaGiocata('d');
						//stampo nella label che il giocatore stampato è un robot
						lblTurnoAttacco.setText(lblTurnoAttacco.getText()+" (Robot)");
						listCarte.setMouseTransparent(true);
						btnSospendiGara.setVisible(false);
					}
					//restituisce la mano del giocatore difendente
					nomiCarte = players[posizioneGiocatoreDifensore].getManoNomi();
					//mi chiedo se si può giocare il turno di attacco
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
							//mi chiedo se il giocatore non è un robot
							if(!players[posizioneGiocatoreDifensore].isRobot()) 
							{
								//è possibile attaccare, attivo la visualizzazione del bottone "passa turno" come unico bottone centrale
								btnPassaTurno.setVisible(true);
								btnPassaTurno.setLayoutX(462);
								btnPassaTurno.setLayoutY(648);
							}
							//disabilito l'utilizzo della listView
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
				e.printStackTrace();
			}
			//metodo per stampare l'attuale punteggio dei giocatori nella label
			stampaGiocatoriLabel();
			//creo un observableList da un array di nomi di carte
			ObservableList<String> items =FXCollections.observableArrayList(nomiCarte);
			//assegno l'observableList alla listView
			listCarte.setItems(items);
			//imposto una cellFactory personalizzata per la listView
			listCarte.setCellFactory(param -> 
			{
			    //creo una nuova listCell personalizzata
				return new ListCell<String>() 
				{	
			        //imageView per mostrare le immagini associate alle carte
					ImageView imageView = new ImageView();
			        //metodo che viene chiamato quando un elemento della lista deve essere aggiornato
					@Override
					public void updateItem(String name, boolean empty) 
					{
			            //richiamo il metodo della superclasse
						super.updateItem(name, empty);
			            //se l'elemento fosse vuoto, resetto la visualizzazione
						if(empty) 
						{
							setText(null);
							setGraphic(null);
							setStyle("");
						} 
						else 
						{
			                //switch per assegnare l'immagine corrispondente alla carta
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
			                //imposto l'immagine nella cella
							setGraphic(imageView);
							//mi chiedo se è un turno di attacco
							if(turno.equals("a")) 
							{
								if((players[posizioneGiocatoreAttaccante].isRobot() && this.getIndex() == getIndexCartaGiocata(posizioneGiocatoreAttaccante,items))) 
								{
									//imposto lo sfondo della carta giocata dal robot di blu perchè un turno offensivo
									setStyle("-fx-control-inner-background: blue;");
									//scrollo la listView fino all'indice della carta giocata
									listCarte.scrollTo(this.getIndex());
								}
							}
							else 
							{
								if((players[posizioneGiocatoreDifensore].isRobot() && this.getIndex() == getIndexCartaGiocata(posizioneGiocatoreDifensore,items))) 
								{ 
									//imposto lo sfondo della carta giocata dal robot di rosso perchè un turno difensivo
									setStyle("-fx-control-inner-background: red;");
									//scrollo la listView fino all'indice della carta giocata
									listCarte.scrollTo(this.getIndex());
								}
							}
						}
					}
				};
			});
			//eseguo il codice solo se siamo in un turno di attacco e l'attaccante è un robot o se siamo in un turno di difesa e il difensore è un robot
			if((turno.equals("a") && players[posizioneGiocatoreAttaccante].isRobot()) ||  (turno.equals("d") && players[posizioneGiocatoreDifensore].isRobot()))
			{
				Thread taskThread = new Thread(() -> 
				{
					//con un blocco try/catch eseguo lo sleep del progetto
					try 
					{
						//blocco l'esecuzione del programma per 3 secondi mostrando all'utente la carta che giocherà il robot
						Thread.sleep(3000);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() 
					{
						@Override
						public void run() 
						{
							//se la carta giocata non è l'indicatore goal
							if(!cartaGiocata.equals(Carta.INDICATORE_GOAL)) 
							{
								//eseguo il bottone "gioca carta"
								btnGiocaCarta.fire();
							}
							else 
							{	
								//eseguo il bottone "passa turno"
								btnPassaTurno.fire();
							}
						}
					});
				});
				//mando in esecuzione il thread
				taskThread.start();
			}
		}
	}

	//metodo che restituisce la posizione della carta giocata dal robot
	private int getIndexCartaGiocata(int posGiocatore, ObservableList<String> items) 
	{
		int cellaDaColorare = -1;
		//cerco all'interno della listView la posizione della cella da colorare
		for(int i = 0; i < items.size(); i++) 
		{
			//il nome della carta della posizione della listView
			String name = items.get(i);
			//se il giocatore è un robot e la carta giocata è quella selezionata dalla listView, restituisco la posizione della listView
			if(players[posGiocatore].isRobot() && name.equals(cartaGiocata.name())) 
			{
				cellaDaColorare = i;
				break;
			}
		}
		//ritorna la posizione della cella da colorare, se non fosse presente ritorna -1
		return cellaDaColorare;
	}

	//metodo che permette di controllare se si può giocare il turno del difensore e stampare le label di informazioni dei pescaggi
	private void checkTurnoDifensore()
	{
		//la carta che è stata giocata dal giocatore attaccante
		cartaAtt = Carta.valueOf(nomeCarta);
		//controllo se il giocatore ha almeno una carta di difesa in mano (in modo che possa difendersi)
		if(partita.checkGiocaTurno(cartaAtt, players[posizioneGiocatoreDifensore].getMano()))
		{
			//mi chiedo se il giocatore non è un robot
			if(!players[posizioneGiocatoreDifensore].isRobot()) 
			{
				//ha una carta difensiva in mano, setto tutti i bottoni visualizzabili (passa turno e gioca carta)
				btnPassaTurno.setVisible(true);
				btnGiocaCarta.setVisible(true);
			}
			else
			{
				//se il giocatore è un robot e la carta restituita è l'indicatore goal significa che non ha carte con cui difendersi
				if(cartaGiocata.equals(Carta.INDICATORE_GOAL)) 
				{
					//stampo nella label che l'utente non può difendersi ed è costretto a passare il turno subendo goal
					lblInfoUtente.setText("Non hai carte di difesa corrette con le quali difenderti. Sei costretto a passare il turno e subire gol");
				}
				else
				{	
					//stampo nella label la carta che giocherà il robot
					lblInfoUtente.setText("La carta che giocherà il robot è " + cartaGiocata);
				}
			}
		}
		else 
		{
			String output;
			//il difensore non può difendersi perchè non ha carte di difesa in mano, se l'attaccante gioca una carta speciale cambia la stampa della label all'utente 
			if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA) || cartaAtt.equals(Carta.MISTER)) 
			{
				output = "La carta " + cartaAtt + " non è difendibile. Sei costretto a passare il turno";
				if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA))
				{
					output += " subendo un gol.";
				}
				//stampo nella label che le carte offensive giocate non sono difendibili
				lblInfoUtente.setText(output);
			}
			else 
			{
				//stampo nella label che l'utente non può difendersi ed è costretto a passare il turno subendo goal
				lblInfoUtente.setText("Non hai carte di difesa con le quali difenderti. Sei costretto a passare il turno e subire gol");
			}
			//mi chiedo se il giocatore non è un robot
			if(!players[posizioneGiocatoreDifensore].isRobot()) 
			{
				//non è possibile difendersi, attivo la visualizzazione del bottone "passa turno" come unico bottone centrale
				btnPassaTurno.setVisible(true);
				btnPassaTurno.setLayoutX(462);
				btnPassaTurno.setLayoutY(648);
				//disabilito l'utilizzo della listView
				listCarte.setDisable(true);
			}
		}
	}
}