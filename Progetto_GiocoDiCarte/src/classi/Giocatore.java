package classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

//classe con tutte le informazioni di un giocatore che prende parte al gioco
public class Giocatore 
{
	//campi privati con l'alias, il numero di partite e tornei vinti, se il giocatore è un robot oppure no e la sua mail
	private String alias;
	private int nPartiteVinte;
	private int nTorneiVinti;
	private BooleanProperty robot;
	private String email;
	//durante la partita mi salvo la mano del giocatore e il suo punteggio attuale
	private ArrayList<Carta> mano;
	private int punteggio;
	
	//metodo costruttore della classe "Giocatore" che permette la creazione di un giocatore della partita
	public Giocatore(String alias, boolean robot, String email) 
	{
		this.alias = alias;
		this.robot = new SimpleBooleanProperty(robot);
		this.punteggio = 0;
		//se il giocatore è nuovo il numero di partite e tornei vinti sarà zero
		this.nPartiteVinte = 0;
		this.nTorneiVinti = 0;
		this.email = email;
		//istanzio l'ArrayList della mano del giocatore
		this.mano = new ArrayList<>();
		//metodo della classe che permette di ricaricare il numero di partite e tornei vinti del giocatore se non è nuovo
		caricaVittoriePartiteTorneo();
	}
	
	public String getEmail() 
	{
		return email;
	}

	public String getAlias() 
	{
		return alias;
	}
	
	public int getNPartiteVinte() 
	{
		return nPartiteVinte;
	}
	
	public int getNTorneiVinti()
	{
		return nTorneiVinti;
	}
	
	public boolean isRobot()
	{
		return robot.get();
	}
	
	public BooleanProperty getRobot() 
	{
		return robot;
	}
	
	public int getPunteggio() 
	{
		return this.punteggio;
	}
	
	public ArrayList<Carta> getMano()
	{
		return mano;
	}
	
	public void setMano(ArrayList<Carta> mano) 
	{
		this.mano = mano;
	}
	
	public void setPunteggio(int p) 
	{
		this.punteggio = p;
	}
	
	public void setVittoriePartite(int v) 
	{
		this.nPartiteVinte = v;
	}

	public void setVittorieTornei(int v) 
	{
		this.nTorneiVinti = v;
	}
	
	//metodo che restituisce un vettore di stringhe contenenti i nomi delle carte della mano del giocatore
	public String[] getManoNomi()
	{
		//la dimensione del vettore sarà il numero di carte che il giocatore ha in mano
		String[] nomiCarte = new String[mano.size()];
		for(int i = 0; i < nomiCarte.length; i++) 
		{
			nomiCarte[i] = mano.get(i).name();
		}
		return nomiCarte;
	}
	
	//metodo che permette di aggiungere una vittoria di una partita al giocatore
	public void aggiungiVittoriaPartita() 
	{
		this.nPartiteVinte = this.nPartiteVinte + 1;
	}
	
	//metodo che permette di aggiungere una vittoria di un torneo al giocatore
	public void aggiungiVittoriaTorneo() 
	{
		this.nTorneiVinti = this.nTorneiVinti + 1;
	}
	
	//metodo che permette di aggiungere un goal alla partita al giocatore
	public void aggiungiGoal() 
	{
		this.punteggio+=1;
	}

	//metodo che permette di caricare nel programma il numero di vittore delle partite e dei tornei dei giocatori che hanno già vinto
	private void caricaVittoriePartiteTorneo()
	{
		String currentDirectory = System.getProperty("user.dir");
		//vado a ricercare l'informazione nel file dove sono salvate le informazioni della Leaderboard del gioco
		String relativePath = "src/giocatori.txt";
		String path = currentDirectory + File.separator + relativePath;
		try 
		{
			Scanner scan = new Scanner(new File(path));
			while(scan.hasNextLine()) 
			{
				//la riga è formata dal nome del giocatore, dal numero di partite e tornei vinti, se è un robot oppure non e la sua mail separata da ","
				String riga = scan.nextLine();
				String[] infoPlayer = riga.split(",");
				//se l'alias letto nel file è lo stesso del giocatore
				if(infoPlayer[0].equals(alias)) 
				{
					//setto con il metodo della classe il numero presente nel file di partite e tornei vinti al giocatore
					setVittoriePartite(Integer.parseInt(infoPlayer[1]));
					setVittorieTornei(Integer.parseInt(infoPlayer[2]));
					break;
				}
			}
			scan.close();
		}
		catch(FileNotFoundException e) 
		{
			System.out.println(e.getMessage());
		}
	}
}