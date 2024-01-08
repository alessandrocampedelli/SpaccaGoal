package classi;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

//la classe "Leaderboard" che contiene tutti i giocatori che hanno preso parte al nostro gioco
public class Leaderboard 
{
	//l'ArrayList con tutti i giocatori
	private ArrayList<Giocatore> players;
	private String path;

	//metodo costruttore della classe "Leaderboard" che riempie l'ArrayList con le informazioni del file di salvataggio dei giocatori
	public Leaderboard() 
	{	
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/giocatori.txt";
		//costruisco il percorso per arrivare al file salvato "giocatori.txt"
		this.path = currentDirectory + File.separator + relativePath;

		//inizializzo l'ArrayList dei giocatori
		this.players = new ArrayList<>();
		//con un blocco try/catch carico tutti i giocatori del file nell'ArrayList dei giocatori tramite il metodo della classe
		try 
		{
			caricaPlayers(this.path);
		}
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	//metodo che restituisce l'ArrayList di giocatori
	public ArrayList<Giocatore> getPlayers()
	{
		return this.players;
	}
	
	//metodo che permette l'aggiunta di un giocatore all'ArrayList di giocatori
	public void addPlayers(Giocatore g) 
	{
		players.add(g);
	}

	//metodo che restituisce l'oggetto "Giocatore" dal suo alias passato come parametro
	public Giocatore getPlayers(String alias) 
	{
		for(Giocatore g : players) 
		{
			if(g.getAlias().equals(alias)) 
			{
				//restituisco l'oggetto "Giocatore" dell'alias passato come parametro
				return g;
			}
		}
		//non restituisco nulla in quanto non esiste l'alias ricercato
		return null;
	}
	
	//metodo che restituisce l'indice dell'ArrayList dal suo alias passato come parametro
	public int indexPlayer(String alias) 
	{
		for(int i = 0; i < players.size(); i++) 
		{
			if(players.get(i).getAlias().equals(alias))
			{
				//restituisco l'indice del giocatore
				return i;
			}
		}
		//non è stato trovato il giocatore quindi restituisco il valore negativo -1
		return -1;
	}
	
	//metodo che carica i giocatori già presenti salvati sul file di testo nell'ArrayList
	private void caricaPlayers(String path) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(path));
		//ciclo finchè non ho letto tutto il file di testo
		while(scan.hasNextLine()) 
		{
			//leggo la riga dal file di testo e la splitto in un vettore per ogni ',' trovata
			String riga = scan.nextLine();
			String[] infoPlayer = riga.split(",");
			Giocatore g;
			//se il giocatore restituisce la 'p' significa che non è un robot, se restituisce una 'r' vuole dire che è un robot
			if(infoPlayer[3].equals("p"))
			{
				g = new Giocatore(infoPlayer[0],false,infoPlayer[4]);
			}
			else
			{
				g = new Giocatore(infoPlayer[0],true,infoPlayer[4]);
			}
			//tramite le proprietà della classe "Giocatore" setto il numero di partite e tornei vinti dal giocatore
			g.setVittoriePartite(Integer.parseInt(infoPlayer[1]));
			g.setVittorieTornei(Integer.parseInt(infoPlayer[2]));
			//aggiungo il giocatore all'ArrayList di giocatori della leaderboard
			addPlayers(g);
		}
		//chiudo il file di testo aperto in lettura
		scan.close();
	}

	//metodo che salva i giocatori (alias, numero vittorie dei tornei, numero vittorie delle partite, robot) sul file
	public void salvaPlayers() throws FileNotFoundException
	{
		PrintWriter fw = new PrintWriter(path);
		//con un ciclo for scrivo nel file di testo tutte le informazioni dei giocatori 
		for(Giocatore g: players) 
		{
			String riga = g.getAlias() + "," + g.getNPartiteVinte() + "," + g.getNTorneiVinti();
			//controllo se il giocatore è un robot oppure no
			if(g.isRobot())
			{
				//il giocatore è un robot e per riconoscerlo nel file scrivo una 'r'
				riga += ",r";
			}
			else
			{
				//il giocatore non è un robot e per riconoscerlo nel file scrivo una 'p'
				riga += ",p";
			}
			riga += "," + g.getEmail();
			//stampo la riga appena creata andando a capo per il giocatore successivo
			fw.println(riga);
		}
		//chiudo il file di testo aperto in scrittura
		fw.close();
	}

	//metodo che permette di costruire una matrice con le informazioni necessarie nella leaderboard dei giocatori
	public String[][] toMatrix()
	{
		//costruisco la matrice con righe pari al numero di giocatori (+1 perchè la prima riga è la legenda) e con colonne pari a 4 informazioni
		String[][] matrice = new String[players.size()+1][4];

		//la prima riga contiene l'indice della leaderboard
		matrice[0][0] = "ALIAS";
		matrice[0][1] = "PARTITE VINTE";
		matrice[0][2] = "TORNEI VINTI";
		matrice[0][3] = "GIOCATORE ROBOT";

		int r = 1;
		//con un ciclo aggiungo alla matrice le informazioni dei giocatori (nome, numero partite vinte, numero tornei vinti e se è un robot oppure no)
		for(Giocatore g : this.players) 
		{
			matrice[r][0] = g.getAlias();
			matrice[r][1] = Integer.toString(g.getNPartiteVinte());
			matrice[r][2] = Integer.toString(g.getNTorneiVinti());
			matrice[r][3] = (g.isRobot() ? "SI" : "NO");
			r+=1;
		}
		//restituisce la matrice di String con tutti i valori incolonnati
		return matrice;
	}
}