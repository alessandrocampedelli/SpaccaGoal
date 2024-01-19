package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Window;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import classi.Alert_cambiaForm;
import classi.Robot;
import classi.Carta;
import classi.Gare;
import classi.Giocatore;
import classi.Mazzo;
import classi.Partita;
import classi.Salvataggio;
import classi.Torneo;
import classi.Leaderboard;

//classe che permette di gestire il calcio di rigore, sia offensivamente (tirandolo), sia difensivamente (parandolo)
public class FormRigoreController implements Initializable
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	//creo l'oggetto "Leaderboard" per aggiornare la leaderboard nel caso di chiusura della partita nel form rigore
	Leaderboard leaderboard = new Leaderboard();
	//creo l'oggetto "Gare" per ritrovare la gara dal codice
	Gare g = new Gare();
	//le variabili di tipo "Salvataggio", "Partita", "Mazzo" e il vettore di "Giocatore"
	Salvataggio s;
	Partita partita;
	Torneo torneo;
	Mazzo mazzo;
	Giocatore[] players;
	//il turno che gestisce se è un turno difensivo o offensivo
	String turno;
	//la posizione del giocatore dell'attaccante e del difensore
	int posizioneGiocatoreAttaccante;
	int posizioneGiocatoreDifensore;
	//l'eventuale nome della carta giocata dall'attaccante nel turno precedente salvata nel file di testo
	String nomeCarta;
	//permette il settaggio del numero di carte da pescare
	int cartePescate;
	//l'immagine del pallone e del portiere mostrate all'utente
	@FXML
	private ImageView imgPortiere;
	@FXML
	private ImageView imgPallone;
	//la label che mostra le informazioni del rigore
	@FXML
	private Label lblRigore;
	//il bottone che permette di tirare/parare il rigore a destra, al centro e a sinistra
	@FXML
	private Button btnSinistra = new Button();
	@FXML
	private Button btnDestra = new Button();
	@FXML
	private Button btnCentro = new Button();
	//il codice della partita che viene passato dal form "giocaPartita"
	static String codicePartita;
	//la carta giocata dall'attaccante, se è un turno offensivo il valore sarà "null", altrimenti se è un turno difensivo sarà una carta
	static Carta cartaGiocata;

	//metodo che permette il passaggio del codice da un form ad un altro
	public void copiaCodice(String codice) 
	{
		codicePartita = codice;
	}
	//metodo che permette di passare da un form all'altro la carta giocata dall'attaccante in un turno di difesa
	public void copiaCartaGiocata(Carta c) 
	{
		cartaGiocata = c;
	}

	//il bottone che viene eseguito se viene calciato/parato un rigore a sinistra
	@FXML
	public void btnSinistra(ActionEvent event) throws IOException
	{
		//controllo se è un turno di attacco o difesa per la carta che dovrà restituire
		if(turno.equals("a")) 
		{
			//restituisce la carta offensiva del rigore con direzione sinistra
			cartaGiocata = Carta.RIGORE_SX;
		}
		else 
		{
			//restituisce la carta difensiva del portiere con direzione sinistra
			cartaGiocata = Carta.PORTIERE_SX;
			//controllo con il metodo se il rigore è stato segnato oppure no
			rigoreSegnato(Carta.valueOf(nomeCarta), cartaGiocata, event);
		}
		//richiamo l'utilizzo del metodo per salvare il turno
		salvaTurnoCambiaForm(event,btnSinistra);
	}

	//il bottone che viene eseguito se viene calciato/parato un rigore al centro
	@FXML
	public void btnCentro(ActionEvent event) throws IOException
	{
		//controllo se è un turno di attacco o difesa per la carta che dovrà restituire
		if(turno.equals("a")) 
		{
			//restituisce la carta offensiva del rigore con direzione centrale
			cartaGiocata = Carta.RIGORE_C;
		}
		else 
		{
			//restituisce la carta difensiva del portiere con direzione centrale
			cartaGiocata = Carta.PORTIERE_C;
			//controllo con il metodo se il rigore è stato segnato oppure no
			rigoreSegnato(Carta.valueOf(nomeCarta), cartaGiocata, event);
		}
		//richiamo l'utilizzo del metodo per salvare il turno
		salvaTurnoCambiaForm(event,btnCentro);
	}

	//il bottone che viene eseguito se viene calciato/parato un rigore a destra
	@FXML
	public void btnDestra(ActionEvent event) throws IOException
	{
		//controllo se è un turno di attacco o difesa per la carta che dovrà restituire
		if(turno.equals("a")) 
		{
			//restituisce la carta offensiva del rigore con direzione destra
			cartaGiocata = Carta.RIGORE_DX;
		}
		else 
		{
			//restituisce la carta difensiva del portiere con direzione destra
			cartaGiocata = Carta.PORTIERE_DX;
			//controllo con il metodo se il rigore è stato segnato oppure no
			rigoreSegnato(Carta.valueOf(nomeCarta), cartaGiocata,event);
		}
		//richiamo l'utilizzo del metodo per salvare il turno
		salvaTurnoCambiaForm(event,btnDestra);
	}
	
	//metodo che viene eseguito all'apertura del form
	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		//controllo se il codice della partita passato da form a form non sia nullo
		if(!(codicePartita == null)) 
		{
			try
			{
				//converto in tipo "Partita" l'oggetto "Gara" trovato dal metodo attraverso il codice della partita
				partita = (Partita) g.getGara(codicePartita);
				//restituisce il mazzo della partita, i giocatori, il turno, la posizione del giocatore attaccante e difensivo, il nome della carta giocate e il numero delle carte pescate
				mazzo = partita.getMazzo();
				players = partita.getGiocatori();
				partita.leggiTurno();
				turno = partita.getTurno();
				posizioneGiocatoreAttaccante = partita.getPosAttaccante();
				posizioneGiocatoreDifensore = partita.getPosDifensore();
				nomeCarta = partita.getNomeCarta();
				cartePescate = partita.getCartePescate();
				//controllo se è un turno di attacco (la stringa contiene "a") o di difesa (la stringa contiene "d")
				if(turno.equals("a"))
				{
					//visualizzazione del pallone ma non del portiere e scrittura label di attacco del rigore
					imgPallone.setVisible(true);
					imgPortiere.setVisible(false);
					lblRigore.setText(players[posizioneGiocatoreAttaccante].getAlias() + " dove vuoi tirare il rigore?");
				}
				else
				{
					//visualizzazione del portiere ma non del pallone e scrittura label di difesa del rigore
					imgPallone.setVisible(false);
					imgPortiere.setVisible(true);
					lblRigore.setText(players[posizioneGiocatoreDifensore].getAlias() +" dove ti vuoi buttare per parare il rigore?");
				}
				//eseguo il codice se siamo in un turno di attacco e l'attaccante è un robot o se siamo in un turno di difesa e il difensore è un robot
				if((turno.equals("a") && players[posizioneGiocatoreAttaccante].isRobot()) ||  (turno.equals("d") && players[posizioneGiocatoreDifensore].isRobot()))
				{
					Robot robot;
					int direzione;
					//controllo se è un turno di attacco o difesa
					if(turno.equals("a")) 
					{
						//è un turno di attacco, istanzio l'oggetto del robot con la posizione del giocatore attaccante
						robot = new Robot(players[posizioneGiocatoreAttaccante]);
						//la direzione che viene scelta randomicamente con un numero da 0 a 2 con il metodo all'interno della classe robot
						direzione = robot.scegliDirezione();
						//metodo che permette di colorare la direzione in cui il robot calcia di blu (turno di attacco)
						coloraDirezioneRobot(direzione, "blue");
					}
					else 
					{
						//è un turno di difensa, istanzio l'oggetto del robot con la posizione del giocatore difensore
						robot = new Robot(players[posizioneGiocatoreDifensore]);
						//la direzione che viene scelta randomicamente con un numero da 0 a 2 con il metodo all'interno della classe robot
						direzione = robot.scegliDirezione();
						//metodo che permette di colorare la direzione in cui il robot calcia di rosso (turno di difesa)
						coloraDirezioneRobot(direzione, "red");
					}
					//il thread per bloccare il form per 3 secondi per rendere chiara all'utente la direzione scelta di tirare/parare
					Thread taskThread = new Thread(() -> 
					{
						try 
						{
							//il programma si blocca per 3 secondi mostrando all'utente la direzione scelta del rigore
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
								//se la direzione è 0 eseguo il bottone di sinistra, se è 1 eseguo il bottone centrale e se è 3 eseguo il bottone a destra
								if(direzione == 0) 
								{
									btnSinistra.fire();
								}
								else if(direzione == 1) 
								{
									btnCentro.fire();
								}
								else 
								{
									btnDestra.fire();
								}
							}
						});
					});
					//avvio il thread
					taskThread.start();
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	//metodo che controlla se il rigore è stato segnato oppure no
	private void rigoreSegnato(Carta rigore, Carta portiere, ActionEvent event) throws IOException
	{
		//controllo se la direzione in cui si è buttato il portiere non coincide con quella tirata dall'attaccante
		if(!(rigore.getDirezione().equals(portiere.getDirezione()))) 
		{
			//l'attaccante ha fatto goal, assegno il goal al giocatore attaccante
			players[posizioneGiocatoreAttaccante].aggiungiGoal();
			//questo metodo serve per aggiornare la label, il giocatore difensore avrà pescato solo una carta perchè ha subito goal
			partita.setCartePescate(1);
			//controllo se fosse finita la partita (attaccante ha fatto 5 goal), nel caso richiamo l'utilizzo del metodo "showFinePartita" o "finePartita"
			if(partita.finePartita(posizioneGiocatoreAttaccante)) {
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
		else
		{
			//il difensore pesca una carta
			players[posizioneGiocatoreDifensore].getMano().add(this.mazzo.pesca());
			//questo metodo serve per aggiornare la label, il giocatore difensore avrà pescato due carte perchè non ha subito goal
			partita.setCartePescate(2);
		}
	}
	
	//metodo che permette di salvare il turno uguale nei tre eventi bottone
	public void salvaTurnoCambiaForm(ActionEvent event, Button bottone) throws IOException 
	{
		//eseguo un salvataggio della partita delle mani dei giocatori, del mazzo, del loro punteggio e del turno
		s = new Salvataggio(g.getGara(codicePartita));
		s.salvaMani();
		s.salvaMazzo();
		s.salvaPunteggio();
		partita.salvaTurnoRigore(cartaGiocata);
		//controllo se la partita non fosse finita (l'attaccante non è arrivato a 5 goal)
		if(!partita.finePartita(posizioneGiocatoreAttaccante)) 
		{
			Scene scene = bottone.getScene();
			//controllo che la scena del bottone di destra non sia nulla
			if (scene != null) 
			{
				Window window = scene.getWindow();
				//controllo che la window del bottone di destra non sia nulla
				if (window != null) 
				{
					//passo al form gioca partita
					alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
				}
			}
		}
	}
	
	//metodo che permette di colorare il bottone con la direzione scelta randomicamente dal robot con il colore passato come parametro del metodo
	private void coloraDirezioneRobot(int direzione, String colore)
	{
		//se la direzione è 0 il rigore viene tirato/parato a sinistra, se è 1 viene tirato/parato al centro e se è 2 viene tirato/parato a destra
		if(direzione == 0) 
		{
			//coloro il bottone di sinistra di blu se il robot sta calciando il calcio di rigore, di rosso se lo sta parando
			btnSinistra.setStyle("-fx-background-color: " + colore + "; -fx-text-fill: white;");
		}
		else if(direzione == 1) 
		{
			//coloro il bottone centrale di blu se il robot sta calciando il calcio di rigore, di rosso se lo sta parando
			btnCentro.setStyle("-fx-background-color: " + colore + "; -fx-text-fill: white;");
		}
		else 
		{
			//coloro il bottone di destra di blu se il robot sta calciando il calcio di rigore, di rosso se lo sta parando
			btnDestra.setStyle("-fx-background-color: " + colore + "; -fx-text-fill: white;");
		}
	}
}