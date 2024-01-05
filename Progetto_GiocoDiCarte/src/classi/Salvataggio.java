package classi;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

//classe Salvataggio per tutte le letture e scritture sui file di testo utili per il salvataggio
public class Salvataggio 
{
	//l'oggetto "Gara" che quando servirà conterrà la gara in oggetto da salvare
	Gara eventoDaSalvare;
	//l'ArrayList che conterrà tutte le gare lette in fase di lettura
	ArrayList<Gara> gareLette;
	Leaderboard leaderboard = new Leaderboard();

	//metodo costruttore della classe "Salvataggio" passando come parametro una gara
	public Salvataggio(Gara g) 
	{
		this.eventoDaSalvare = g;
	}

	//metodo costruttore ovveridato della classe "Salvataggio" non passando nulla come parametro
	public Salvataggio() 
	{
		this.gareLette = new ArrayList<Gara>();
	}

	//metodo che permette di restituire tutte le gare lette, sia partite singole che tornei
	public ArrayList<Gara> leggiGare() throws FileNotFoundException
	{
		//metodo che restituisce tutte le partite lette
		leggiPartite();
		//metodo che restituisce tutte i tornei letti
		leggiTornei();
		//ritornano tutte le gare lette
		return gareLette;
	}

	//metodo che permette di leggere le partite presenti nel file di testo
	public void leggiPartite() throws FileNotFoundException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src\\partite";
		String absolutePath = currentDirectory + File.separator + relativePath;
		//creo il file con il percorso assoluto
		File f = new File(absolutePath);
		//ottengo il vettore di file con tutti i file di partite presenti nella cartella
		File[] partite = f.listFiles();
		for(int i = 0; i < partite.length; i++) 
		{
			//aggiungo alle gare lette la partita
			gareLette.add(leggiNomiPartita(new File(partite[i].getPath()+"/nomi.txt"), partite[i].getName()));
			//richiamo l'utilizzo dei metodi della lettura del mazzo, delle mani dei giocatori e dei punteggi della partita appena letta
			leggiMazzo(new File(partite[i].getPath()+"/mazzo.txt"), partite[i].getName(), gareLette.get(gareLette.size() - 1));
			leggiMani(new File(partite[i].getPath()+"/mani.txt"),partite[i].getName(), gareLette.get(gareLette.size() - 1));
			leggiPunteggi(new File(partite[i].getPath()+"/punteggi.txt"),partite[i].getName(), gareLette.get(gareLette.size() - 1));
		}
	}

	//metodo che permette di leggere i tornei presenti nel file di testo
	public void leggiTornei() throws FileNotFoundException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src\\tornei";
		String absolutePath = currentDirectory + File.separator + relativePath;
		//creo il file con il percorso assoluto
		File f = new File(absolutePath);
		//ottengo il vettore di file con tutti i file di tornei presenti nella cartella
		File[] tornei = f.listFiles();
		for(int i = 0; i < tornei.length; i++) 
		{
			Torneo torneo = leggiGiocatoriTorneo(new File(tornei[i].getPath()+"/giocatoriTorneo.txt"), tornei[i].getName());
			//aggiungo alle gare lette il torneo appena lette
			gareLette.add(torneo);
			//ottengo il vettore di file con tutti i file di partite presenti nella cartella
			File[] partite = tornei[i].listFiles();
			for(int j = 0; j < partite.length; j++) 
			{
				//controllo se è una cartella, in questo modo entro in questo ciclo (nella cartella ci sono anche file di testo che in questo momento non ci interessano)
				if(partite[j].isDirectory())
				{
					//leggo la partita del torneo richiamando l'utilizzo dei metodi della lettura dei giocatori, del mazzo, delle mani e dei punteggi
					Partita p = (Partita) leggiNomiPartita(new File(partite[j].getPath()+"/nomi.txt"), partite[j].getName());
					leggiMazzo(new File(partite[j].getPath()+"/mazzo.txt"), partite[j].getName(), p);
					leggiMani(new File(partite[j].getPath()+"/mani.txt"), partite[j].getName(), p);
					leggiPunteggi(new File(partite[j].getPath()+"/punteggi.txt"),partite[j].getName(), p);
					//aggiungo la partita all'oggetto torneo
					torneo.aggiungiPartita(p);
				}
			}
		}
	}

	//metodo che restituisce un oggetto "Partita" con i giocatori che ne fanno parte e il codice della partita
	public Partita leggiNomiPartita(File f, String codice) throws FileNotFoundException
	{
		//richiamo l'utilizzo del metodo che restituisce un ArrayList con i giocatori
		ArrayList<Giocatore> giocatori = leggiGiocatori(f);
		return new Partita(giocatori , codice);
	}

	//metodo che restituisce un oggetto "Torneo" con i giocatori che ne fanno parte e il codice del torneo
	private Torneo leggiGiocatoriTorneo(File f, String codice) throws FileNotFoundException
	{
		//richiamo l'utilizzo del metodo che restituisce un ArrayList con i giocatori
		ArrayList<Giocatore> giocatori = leggiGiocatori(f);
		return new Torneo(giocatori , codice);
	}

	//metodo che permette la lettura del tabellone per creare la griglia del torneo
	public ArrayList<Giocatore> leggiTabelloneTorneo() throws FileNotFoundException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara()+"/tabelloneTorneo.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		//creo il file con il percorso assoluto
		File f = new File(absolutePath);
		//controllo se il file esiste, se si ritorna l'ArrayList di giocatori del file, altrimenti ritorna l'oggetto "null"
		if(f.exists()) 
		{
			ArrayList<Giocatore> giocatori = leggiGiocatori(f);
			return giocatori;
		}
		return null;
	}

	//metodo che permette la lettura del mazzo passati come parametro il file, il codice e la gara
	public void leggiMazzo(File f, String codice, Gara g) throws FileNotFoundException 
	{
		//l'oggetto che permette la lettura del file passato come parametro
		Scanner scan = new Scanner(f);
		//la LinkedList che conterrà tutte le carte del mazzo (già mischiate) lette dal file
		LinkedList<Carta> carte = new LinkedList<Carta>();
		while(scan.hasNextLine()) 
		{
			//aggiungo alla LinkedList le carte
			carte.add(Carta.valueOf(scan.nextLine()));
		}
		//tramite la proprietà della classe setto il mazzo della partita appena creato
		g.setMazzo(new Mazzo(carte));
		scan.close();
	}

	//metodo che permette la lettura delle mani dei giocatori passati come parametro il file, il codice e la gara
	public void leggiMani(File f, String codice, Gara g) throws FileNotFoundException
	{
		//l'oggetto che permette la lettura del file passato come parametro
		Scanner scan = new Scanner(f);
		for(int i = 0; scan.hasNextLine(); i++)
		{
			//la riga contenente la mano del giocatore
			String mano = scan.nextLine();
			//aggiungo le carte alla mano del giocatore richiamando il metodo "leggiManoGiocatore"
			g.giocatori[i].setMano(leggiManoGiocatore(mano));
		}
		scan.close();
	}

	//metodo che permette la lettura della stringa letta dal file con le mani dei giocatori e ritorna un ArrayList con le sue carte
	private ArrayList<Carta> leggiManoGiocatore(String mano)
	{
		ArrayList<Carta> _mano = new ArrayList<>();
		//splitto la stringa in un vettore ogni volta che viene trovata una virgola
		String[] nomiCarte = mano.split(",");
		for(int i = 0; i < nomiCarte.length; i++)
		{
			_mano.add(Carta.valueOf(nomiCarte[i]));
		}
		//ritorna l'ArrayList con la mano del giocatore
		return _mano;
	}

	//metodo che permette la lettura dei punteggi dei giocatori passati come parametro il file, il codice e la gara
	public void leggiPunteggi(File f, String codice, Gara g) throws FileNotFoundException
	{
		//l'oggetto che permette la lettura del file passato come parametro
		Scanner scan = new Scanner(f);
		for(int i = 0; scan.hasNextLine(); i++) 
		{
			//la riga contenente il punteggio del giocatore
			String punteggio = scan.nextLine();
			//aggiungo il punteggio del giocatore convertendo la stringa ad intero
			g.giocatori[i].setPunteggio(Integer.parseInt(punteggio));
		}
		scan.close();
	}

	//metodo che permette il salvataggio di una partita passati come parametri l'oggetto "Partita" e il percorso
	public void salvaPartita(Partita p,String percorso) throws IOException
	{
		//distribuisco le carte ai giocatori
		p.distribuzioneCarte();
		//richiamo l'utilizzo dei metodi della classe per salvare i nomi dei giocatori, il mazzo, le mani e i punteggi
		this.salvaNomiGiocatori(percorso);
		this.salvaMazzo();
		this.salvaMani();
		this.salvaPunteggio();
		//richiamo l'utilizzo della classe partita per salvare anche l'ultimo turno giocato
		p.salvaTurno(percorso);
	}

	//DA QUI!
	//metodo che permette il salvataggio dei nomi dei giocatori
	public void salvaNomiGiocatori(String partitaTorneo) throws IOException
	{
		String path = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara();
		Files.createDirectory(Paths.get(path));
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara()+"/nomi.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		for(Giocatore player : eventoDaSalvare.giocatori) 
		{
			//controllo se il giocatore da salvare è un robot oppure no
			if(player.isRobot())
				fw.println(player.getAlias()+"*");
			else
				fw.println(player.getAlias());
		}
		fw.close();
	}

	//metodo che permette il salvataggio dei giocatori dei torneo vincenti
	public void salvaGiocatoriTorneo() throws IOException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara()+"/giocatoriTorneo.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		Torneo t = (Torneo) eventoDaSalvare;
		salvaGiocatore(absolutePath, t.getGiocatoreVincenti());
	}

	//metodo che permette il salvataggio dei giocatori dei torneo del tabellone
	public void salvaTabelloneTorneo() throws IOException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara()+"/tabelloneTorneo.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		Torneo t = (Torneo) eventoDaSalvare;
		salvaGiocatore(absolutePath, t.getTabelloneGiocatori());
	}

	//metodo che permette il salvataggio dei giocatori del torneo
	public void salvaGiocatore(String absolutePath, ArrayList<Giocatore> players) throws FileNotFoundException 
	{
		PrintWriter fw = new PrintWriter(absolutePath);
		for(Giocatore player : players) 
		{
			//controllo se il giocatore da salvare è un robot oppure no
			if(player.isRobot())
				fw.println(player.getAlias()+"*");
			else
				fw.println(player.getAlias());
		}
		fw.close();
	}

	//metodo che permette il salvataggio del mazzo di una partita
	public void salvaMazzo() throws IOException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath; 
		if(eventoDaSalvare.getCodiceGara().charAt(0) == 'p')
			relativePath = "src/partite/"+eventoDaSalvare.getCodiceGara()+"/mazzo.txt";
		else
			relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara().substring(0, eventoDaSalvare.getCodiceGara().length() - 1)+"/"+eventoDaSalvare.getCodiceGara()+"/mazzo.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter fw = new PrintWriter(absolutePath);
		for(int i = 0; i < eventoDaSalvare.getMazzo().getCarte().size(); i++)
		{
			fw.println(eventoDaSalvare.getMazzo().getCarte().get(i));
		}
		fw.close();
	}

	//metodo che permette il salvataggio delle mani dei giocatori 
	public void salvaMani() throws IOException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath; 
		if(eventoDaSalvare.getCodiceGara().charAt(0) == 'p')
			relativePath = "src/partite/"+eventoDaSalvare.getCodiceGara()+"/mani.txt";
		else
			relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara().substring(0, eventoDaSalvare.getCodiceGara().length() - 1)+"/"+eventoDaSalvare.getCodiceGara()+"/mani.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter fw = new PrintWriter(absolutePath);
		for(int i = 0; i < eventoDaSalvare.getGiocatori().length; i++)
		{
			ArrayList<Carta> mano = eventoDaSalvare.getGiocatori()[i].getMano();
			String riga = "";
			for(Carta c: mano) 
			{
				riga += c.name()+",";
			}
			//nel caso estremo il giocatore finisca le carte il gioco prevede il pescaggio di una carta perchè nessun giocatore puo rimanere senza
			if(riga.length() == 0)
			{
				Carta c = eventoDaSalvare.getMazzo().pesca(); 
				mano.add(c);
				riga += c.name()+",";
			}
			//cancello l'ultima virgola
			riga = riga.substring(0, riga.length() - 1);
			fw.println(riga);
		}
		fw.close();
	}

	//metodo che permette il salvataggio dei punteggi del giocatore
	public void salvaPunteggio() throws IOException
	{
		//queste righe permettono di determinare il percorso assoluto del file che ci interessa
		String currentDirectory = System.getProperty("user.dir");
		String relativePath; 
		if(eventoDaSalvare.getCodiceGara().charAt(0) == 'p')
			relativePath = "src/partite/"+eventoDaSalvare.getCodiceGara()+"/punteggi.txt";
		else
			relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara().substring(0, eventoDaSalvare.getCodiceGara().length() - 1)+"/"+eventoDaSalvare.getCodiceGara()+"/punteggi.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		for(int i = 0; i < eventoDaSalvare.getGiocatori().length; i++)
		{
			fw.println(eventoDaSalvare.getGiocatori()[i].getPunteggio());
		}
		fw.close();
	}

	//metodo che permette la lettura dei giocatori
	private ArrayList<Giocatore> leggiGiocatori(File f) throws FileNotFoundException
	{
		ArrayList<Giocatore> giocatori = new ArrayList<>();
		Scanner scan = new Scanner(f);
		while(scan.hasNextLine()) 
		{
			String idGiocatore = scan.nextLine();
			char robot = idGiocatore.charAt(idGiocatore.length() - 1);
			//carattere robot
			if(robot == '*') 
			{
				idGiocatore = idGiocatore.substring(0, idGiocatore.length() - 1);
				String email = leaderboard.getPlayers(idGiocatore).getEmail();
				giocatori.add(new Giocatore(idGiocatore,true,email));
			}
			else {
				String email = leaderboard.getPlayers(idGiocatore).getEmail();
				giocatori.add(new Giocatore(idGiocatore,false,email));
			}
		}
		scan.close();
		return giocatori;
	}

	//metodo che elimina la cartella della partita terminata
	public void deleteDirectory(String percorso)
	{
		String path = "src/"+percorso;
		File file = new File(path);
		for (File subfile : file.listFiles()) 
		{
			if (subfile.isDirectory()) 
			{	
				deleteDirectory(percorso+"/"+subfile.getName());
			}
			subfile.delete();
		}
		file.delete();
	}

	//metodo che permette di creare delle cartelle per le partite dei tornei identificati da un numero
	public void createDirectory(String percorso) throws IOException
	{
		String path = "src/"+percorso+"/"+eventoDaSalvare.getCodiceGara();
		//creo la cartella con il percorso relativo
		Files.createDirectory(Paths.get(path));
	}
}