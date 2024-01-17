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
		//il numero di carte pescate sarà sempre almeno una
		cartePescate = 1;
		
		//se il giocatore offensivo ha giocato l'attaccante, il difensore potrà difendersi con il difensore e con il difensore roccia
		if(cartaAtt.equals(Carta.ATTACCANTE)) 
		{
			//se il giocatore difendente non ha giocato il difensore o il difensore roccia
			if(!(cartaDif.equals(Carta.DIFENSORE) || cartaDif.equals(Carta.DIFENSORE_ROCCIA)))
			{
				//aggiungo un goal all'attaccante
				att.aggiungiGoal();
			}
			else 
			{
				//il difensore pesca un'altra carta in quanto non ha subito goal
				dif.getMano().add(this.carte.pesca());
				//il numero di carte pescate viene incrementato a 2
				cartePescate++;
			}
		}
		else 
		{
			//se il giocatore offensivo ha giocato il bomber vero, il difensore potrà difendersi solamente con il difensore roccia
			if(cartaAtt.equals(Carta.BOMBER_VERO)) 
			{
				//se il giocatore difendente non ha giocato il difensore roccia
				if(!cartaDif.equals(Carta.DIFENSORE_ROCCIA)) 
				{
					//aggiungo un goal all'attaccante
					att.aggiungiGoal();
				}
				else 
				{
					//il difensore pesca un'altra carta in quanto non ha subito goal
					dif.getMano().add(this.carte.pesca());
					//il numero di carte pescate viene incrementato a 2
					cartePescate++;
				}
			}
			else 
			{
				//se il giocatore offensivo ha giocato la rovesciata dell'anno o il tiro della domenica farà goal sicuramente perchè sono carte non difendibili
				if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA))
				{
					//aggiungo un goal all'attaccante
					att.aggiungiGoal();
				}
				else 
				{
					//se il giocatore offensivo ha giocato il goal, il difensore potrà difendersi con il var e con il fuorigioco
					if(cartaAtt.equals(Carta.GOAL)) 
					{
						//se il giocatore difendente non ha giocato il var o il fuorigioco
						if(!(cartaDif.equals(Carta.VAR) || cartaDif.equals(Carta.FUORIGIOCO))) 
						{
							//aggiungo un goal all'attaccante
							att.aggiungiGoal();
						}
						else 
						{
							//il difensore pesca un'altra carta in quanto non ha subito goal
							dif.getMano().add(this.carte.pesca());
							//il numero di carte pescate viene incrementato a 2
							cartePescate++;
						}
					}
					else 
					{
						//se il giocatore offensivo ha giocato la carta mister, il difensore non si difenderà e l'attaccante pescherà due carte
						if(cartaAtt.equals(Carta.MISTER)) 
						{
							//aggiungo due carte alla mano dell'attaccante e il numero di carte pescate passa a 2
							att.getMano().add(this.carte.pesca());
							att.getMano().add(this.carte.pesca());
							cartePescate = 2;
						}
						else 
						{
							//se il giocatore difensivo non ha giocato il portiere (l'attaccante ha giocato la carta rigore per esclusione)
							if(!cartaDif.equals(Carta.PORTIERE)) 
							{
								//aggiungo un goal all'attaccante
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
			{				
				//c'è almeno una carta, ritorna "true" perchè il turno offensivo si può giocare
				return true;
			}
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
				{
					//c'è almeno una carta, ritorna "true" perchè il turno difensivo si può giocare
					return true;
				}
			}
			//non c'è nessuna carta di difesa, ritorna "false" perchè il difensore non si puà difendere
			return false;
		}
	}
	
	//metodo che permette di salvare il turno di default appena viene creata una partita
	public void salvaTurno(String partitaTorneo) throws IOException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+this.codice+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		//creo un oggetto "PrintWriter" con il percorso assoluto come parametro
		PrintWriter pw = new PrintWriter(absolutePath);
		//salvo di default che il primo turno sarà un turno di attacco con le posizioni del primo giocatore (attaccante) e del secondo giocatore (difensivo)
		pw.println("a");
		pw.println(0);
		pw.println(1);
		//numero di carte pescate per il salvataggio nella label
		pw.println(1);
		pw.close();
	}

	//metodo che permette di leggere il turno dal file di testo
	public void leggiTurno() throws IOException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "";
		//cambia il percorso relativo se stiamo leggendo un turno di una partita o di un torneo
		if(codice.charAt(0) == 'p')
		{
			relativePath = "src/partite/"+codice+"/turno.txt";
		}
		else
		{
			relativePath = "src/tornei/"+codice.substring(0,codice.length() - 1) + "/"+codice+"/turno.txt";
		}
		String absolutePath = currentDirectory + File.separator + relativePath;
		//creo il file con il percorso assoluto
		File f = new File(absolutePath);
		//creo l'oggetto per leggere le righe del file di testo
		Scanner scan = new Scanner(f);
		//la prima riga indica se è un turno di attacco ("a") o di difesa ("d")
		turno = scan.nextLine();
		//la seconda riga indica la posizione del giocatore che sta attaccando
		posizioneGiocatoreAttaccante = scan.nextInt();
		//la terza riga indica la posizione del giocatore che sta difendendo (il successivo)
		posizioneGiocatoreDifensore = scan.nextInt();
		//la quarta riga indica il numero di carte per la stampa delle carte pescate nella label
		cartePescate = scan.nextInt();
		//se è un turno difensivo leggiamo anche la carta giocata dall'attaccante
		if(turno.equals("d"))
		{
			nomeCarta = scan.next();
		}
		scan.close();
	}

	//metodo che permette di salvare il turno della partita in corso
	public void salvaTurnoGara(Carta cartaGiocata, Carta cartaAtt) throws IOException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath; 
		//cambia il percorso relativo se stiamo salvando un turno di una partita o di un torneo
		if(this.getCodiceGara().charAt(0) == 'p')
		{
			relativePath = "src/partite/"+this.getCodiceGara()+"/turno.txt";
		}
		else
		{
			relativePath = "src/tornei/"+this.getCodiceGara().substring(0, this.getCodiceGara().length() - 1)+"/"+this.getCodiceGara()+"/turno.txt";
		}
		String absolutePath = currentDirectory + File.separator + relativePath;
		//creo un oggetto "PrintWriter" con il percorso assoluto come parametro
		PrintWriter fw = new PrintWriter(absolutePath);
		//mi chiedo se è un turno di attacco o di difesa
		if(turno.equals("a")) 
		{
			//se l'attaccante ha giocato la carta rigore sarà ancora un turno di attacco in quanto cambiamo form, altrimenti passeremo ad un turno difensivo
			if(cartaGiocata.equals(Carta.RIGORE)) 
			{
				fw.println("a");
			}
			else 
			{
				fw.println("d");
			}
			//salvo nel file di testo la posizione del giocatore attaccante, la posizione dei giocatore difensivo e il numero di carte pescate
			fw.println(posizioneGiocatoreAttaccante);
			fw.println(posizioneGiocatoreDifensore);
			fw.println(cartePescate);
			//salvo la carta giocata dall'attaccante
			fw.println(cartaGiocata.name());
		}
		else 
		{
			//TERMINARE DA QUI!
			//siamo in un turno difensivo
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
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath;
		//cambia il percorso relativo se stiamo salvando un turno di una partita o di un torneo
		if(this.getCodiceGara().charAt(0) == 'p')
		{
			relativePath = "src/partite/"+this.getCodiceGara()+"/turno.txt";
		}
		else
		{
			relativePath = "src/tornei/"+this.getCodiceGara().substring(0, this.getCodiceGara().length() - 1)+"/"+this.getCodiceGara()+"/turno.txt";
		}
		String absolutePath = currentDirectory + File.separator + relativePath;
		//creo un oggetto "PrintWriter" con il percorso assoluto come parametro
		PrintWriter fw = new PrintWriter(absolutePath);
		//mi chiedo se è un turno di attacco o di difesa
		if(turno.equals("a")) 
		{
			//passiamo ad un turno difensivo in quanto è stata scelta una direzione in cui calciare il rigore
			fw.println("d");
			//salvo nel file di testo la posizione del giocatore attaccante, la posizione dei giocatore difensivo e il numero di carte pescate
			fw.println(posizioneGiocatoreAttaccante);
			fw.println(posizioneGiocatoreDifensore);
			fw.println(cartePescate);
			//salvo la carta giocata dall'attaccante
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
		//aggiungo la vittoria della partita al giocatore
		giocatori[posizioneGiocatoreAttaccante].aggiungiVittoriaPartita();
		leaderboard.getPlayers(aliasVincente).aggiungiVittoriaPartita();
		//aggiornata una vittoria nella leaderboard, risalvo il file di testo con i valori aggiornati
		leaderboard.salvaPlayers();
		//mostro con un alert il vincitore della partita con i risultati della partita
		alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+ " HA VINTO LA PARTITA");
		//la partita è terminata, mostro all'utente la leaderboard e elimino la cartella della partita
		alert.passaAlForm("/application/FormLeaderboard.fxml", event);
		inviaMail(this.giocatori);
		//elimino la partita appena giocata visto che è terminata
		s.deleteDirectory("partite");
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