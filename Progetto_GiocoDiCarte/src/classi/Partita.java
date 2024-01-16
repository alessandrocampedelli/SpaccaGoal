package classi;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.event.ActionEvent;

//la classe "Partita" derivata dalla classe "Gara"
public class Partita extends Gara
{
	//il numero di carte iniziali di un giocatore è costante
	private final int N_CARTE_INIZIO = 5;
	//il turno della partita, la posizione del giocatore attaccante e difensore, il nome della carta e il numero di carte pescate
	private String turno;
	private int posizioneGiocatoreAttaccante;
	private int posizioneGiocatoreDifensore;
	private String nomeCarta;
	private int cartePescate;

	//metodo costruttore della classe "Partita"
	public Partita(ArrayList<Giocatore> giocatori, String codice)
	{
		//eredità il metodo costruttore della classe padre "Gara"
		super(giocatori,codice);
	}

	public String getTurno()
	{
		return this.turno;
	}

	public int getPosAttaccante()
	{
		return this.posizioneGiocatoreAttaccante;
	}

	public int getPosDifensore()
	{
		return this.posizioneGiocatoreDifensore;
	}

	public String getNomeCarta()
	{
		return this.nomeCarta;
	}

	public int getCartePescate() 
	{
		return this.cartePescate;
	}

	public void setCartePescate(int cartePescate) 
	{
		this.cartePescate = cartePescate;
	}

	//metodo che permette di pulire le mani dei giocatori
	private void pulisciMani() 
	{
		//pulisco l'arrayList di Carta di ogni giocatore della partita
		for(Giocatore g : giocatori)
		{
			g.getMano().clear();
		}
	}

	//metodo che permette di distribuire le carte ai giocatori
	public void distribuzioneCarte()
	{
		//pulisco le mani dei giocatori da eventuali partite precedenti
		pulisciMani();
		//mischio il mazzo della partita
		carte.mischia();
		for(int j = 0; j < this.giocatori.length; j++) 
		{
			//assegno ad ogni giocatore 5 carte in mano
			for(int i = 0; i < N_CARTE_INIZIO; i++) 
			{
				giocatori[j].getMano().add(carte.pesca());
				//il giocatore che inizia la partita pesca una carta in più
				if(i == 4 && j == 0)
				{
					giocatori[j].getMano().add(carte.pesca());
				}
			}
		}
		//dopo aver consegnato le mani ai giocatori, rimischio le carte
		carte.mischia();
	}

	//metodo che permette di giocare una carta
	public void gioca(Giocatore att, Giocatore dif, Carta cartaAtt, Carta cartaDif) 
	{
		//il difensore pesca una carta (una carta viene pescata indipendemente se subisce goal oppure no)
		dif.getMano().add(this.carte.pesca());
		cartePescate = 1;
		//se la carta giocata è l'attaccante
		if(cartaAtt.equals(Carta.ATTACCANTE)) 
		{
			
			if(!(cartaDif.equals(Carta.DIFENSORE) || cartaDif.equals(Carta.DIFENSORE_ROCCIA)))
			{
				att.aggiungiGoal();
			}
			else 
			{
				dif.getMano().add(this.carte.pesca());
				cartePescate++;
			}
		}
		else 
		{
			//bomber vero
			if(cartaAtt.equals(Carta.BOMBER_VERO)) 
			{
				if(!cartaDif.equals(Carta.DIFENSORE_ROCCIA)) 
				{
					att.aggiungiGoal();
				}
				else 
				{
					dif.getMano().add(this.carte.pesca());
					cartePescate++;
				}
			}
			else 
			{
				//rovesciata dell'anno e tiro della domenica
				if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA))
				{
					att.aggiungiGoal();
				}
				else 
				{
					//goal
					if(cartaAtt.equals(Carta.GOAL)) 
					{
						if(!(cartaDif.equals(Carta.VAR) || cartaDif.equals(Carta.FUORIGIOCO))) 
						{
							att.aggiungiGoal();
						}
						else 
						{
							dif.getMano().add(this.carte.pesca());
							cartePescate++;
						}
					}
					else 
					{
						if(cartaAtt.equals(Carta.MISTER)) 
						{
							att.getMano().add(this.carte.pesca());
							att.getMano().add(this.carte.pesca());
							cartePescate = 2;
						}
						else 
						{
							//caso del rigore
							if(!cartaDif.equals(Carta.PORTIERE)) 
							{
								att.aggiungiGoal();
							}
						}
					}
				}
			}
		}
	}
	
	//metodo che controlla se si può giocare un turno di attacco
	public boolean checkGiocaTurno(Giocatore att) 
	{
		for(Carta c : att.getMano()) 
		{
			//controllo se c'è almeno una carta di attacco nella mano dell'attaccante
			if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.GOAL) || c.equals(Carta.MISTER))
				//c'è almeno una carta, ritorna "true" perchè il turno offensivo si può giocare
				return true;
		}
		//non c'è nessuna carta di attacco, ritorna "false" perchè l'attaccante non può attaccare
		return false;
	}

	//metodo che controlla se si può giocare il turno difensivo
	public boolean checkGiocaTurno(Carta cartaGiocata, ArrayList<Carta> manoAvversario) 
	{
		//controllo se l'attaccante ha giocato una carta non difendibile
		if(cartaGiocata.equals(Carta.ROVESCIATA_DELLANNO) || cartaGiocata.equals(Carta.TIRO_DOMENICA) || cartaGiocata.equals(Carta.MISTER)) 
		{
			//ritorna "false" perchè il difensore non si puà difendere anche se ha una carta difensiva
			return false;
		}
		else 
		{
			for(Carta c : manoAvversario) 
			{
				//controllo se c'è almeno una carta di difesa nella mano del difensore
				if(c.getTipologia().equals(Tipologia.DIFESA) || c.equals(Carta.VAR) || c.equals(Carta.FUORIGIOCO))
					//c'è almeno una carta, ritorna "true" perchè il turno difensivo si può giocare
					return true;
			}
			//non c'è nessuna carta di difesa, ritorna "false" perchè il difensore non si puà difendere
			return false;
		}
	}
	
	//metodo che permette di salvare il turno di default appena viene creata una partita
	public void salvaTurno(String partitaTorneo) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+this.codice+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter pw = new PrintWriter(absolutePath);
		pw.println("a");
		pw.println(0);
		pw.println(1);
		pw.println(1);
		pw.close();
	}

	//metodo che permette di leggere il turno dal file di testo
	public void leggiTurno() throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "";
		if(codice.charAt(0) == 'p')
		{
			relativePath = "src/partite/"+codice+"/turno.txt";
		}
		else
		{
			relativePath = "src/tornei/"+codice.substring(0,codice.length() - 1)+"/"+codice+"/turno.txt";
		}
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		Scanner scan = new Scanner(f);
		turno = scan.nextLine();
		posizioneGiocatoreAttaccante = scan.nextInt();
		posizioneGiocatoreDifensore = scan.nextInt();
		cartePescate = scan.nextInt();
		//devo dire che se va nel form rigore deve leggere anche la carta giocata
		if(turno.equals("d"))
		{
			nomeCarta = scan.next();
		}
		scan.close();
	}

	//metodo che permette di salvare il turno della partita in corso
	public void salvaTurnoGara(Carta cartaGiocata, Carta cartaAtt) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath; 
		if(this.getCodiceGara().charAt(0) == 'p')
		{
			relativePath = "src/partite/"+this.getCodiceGara()+"/turno.txt";
		}
		else
		{
			relativePath = "src/tornei/"+this.getCodiceGara().substring(0, this.getCodiceGara().length() - 1)+"/"+this.getCodiceGara()+"/turno.txt";
		}
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
			fw.println(cartePescate);
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
					fw.println(cartePescate);
					fw.println(cartaAtt.name());
				}
			}
			fw.println("a");
			if((giocatori.length-1) != posizioneGiocatoreAttaccante)
				fw.println(posizioneGiocatoreAttaccante+1);
			else
				fw.println(0);

			if((giocatori.length-1) != posizioneGiocatoreDifensore)
				fw.println(posizioneGiocatoreDifensore+1);
			else
				fw.println(0);
			fw.println(cartePescate);
		}
		fw.close();
	}

	//metodo che permette di salvare il turno del rigore
	public void salvaTurnoRigore(Carta cartaGiocata) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath;
		if(this.getCodiceGara().charAt(0) == 'p')
		{
			relativePath = "src/partite/"+this.getCodiceGara()+"/turno.txt";
		}
		else
		{
			relativePath = "src/tornei/"+this.getCodiceGara().substring(0, this.getCodiceGara().length() - 1)+"/"+this.getCodiceGara()+"/turno.txt";
		}
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		if(turno.equals("a")) 
		{
			fw.println("d");
			fw.println(posizioneGiocatoreAttaccante);
			fw.println(posizioneGiocatoreDifensore);
			fw.println(cartePescate);
			fw.println(cartaGiocata.name());
		}
		else 
		{
			fw.println("a");
			if((giocatori.length-1) != posizioneGiocatoreAttaccante)
				fw.println(posizioneGiocatoreAttaccante+1);
			else
				fw.println(0);

			if((giocatori.length-1) != posizioneGiocatoreDifensore)
				fw.println(posizioneGiocatoreDifensore+1);
			else
				fw.println(0);
			fw.println(cartePescate);
		}
		fw.close();
	}
	
	//metodo che permette di verificare se la partita è terminata (l'attaccante ha segnato 5 goal)
	public boolean finePartita(int iPosAtt) 
	{
		boolean fine = false;
		//controllo se il giocatore ha segnato 5 goal
		if(giocatori[iPosAtt].getPunteggio() == 5)
		{
			//il giocatore ha segnato 5 goal, ritorna la variabile booleana "true" e la partita sarà terminata
			fine = true;
		}
		//il giocatore non ha segnato 5 goal, ritorna la variabile booleana "false" e la partita non sarà terminata
		return fine;
	}
	
	//metodo che permette di mostrare la classifica finale con i punteggi dei giocatori
	public String mostraRisultati() 
	{
		//richiamo il metodo dalla classe padre "Gara"
		return super.mostraRisultati();
	}
	
	//metodo che viene eseguito quando la partita termina
	public void showFinePartita(ActionEvent event, String aliasVincente, Leaderboard leaderboard, Alert_cambiaForm alert) throws IOException
	{
		giocatori[posizioneGiocatoreAttaccante].aggiungiVittoriaPartita();
		leaderboard.getPlayers(aliasVincente).aggiungiVittoriaPartita();
		//aggiornata una vittoria nella leaderboard, risalvo il file di testo con i valori aggiornati
		leaderboard.salvaPlayers();
		alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+ " HA VINTO LA PARTITA");
		//la partita è terminata, mostro all'utente la leaderboard e elimino la cartella della partita
		alert.passaAlForm("/application/FormLeaderboard.fxml", event);
		inviaMail(this.giocatori);
		//s.deleteDirectory("partite");
	}

	//metodo che permette di creare la stampa con le informazioni dei giocatori (alias, robot, punteggio e mano)
	public String toString() 
	{
		String info = "";
		//creo la stringa con le informazioni dei giocatori
		for(Giocatore p: this.getGiocatori()) 
		{
			//aggiungo alla stringa l'alias del giocatore, se è un robot oppure no, il punteggio attuale e la mano che possiede
			info += "Giocatore " + p.getAlias() + (p.isRobot() ? " (Robot)" : "") + "\n Punteggio: " + p.getPunteggio() + "\n Mano: " + p.getMano().toString() + "\n\n";
		}
		//ritorna la stringa appena creata
		return info;
	}
}