package classi;
import classi.Gara;
import classi.Giocatore;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Salvataggio {
	Gara eventoDaSalvare;
	ArrayList<Gara> gareLette;
	public Salvataggio(Gara g) 
	{
		this.eventoDaSalvare = g;
	}
	public Salvataggio() 
	{
		this.gareLette = new ArrayList<Gara>();
	}

	public ArrayList<Gara> leggiGare() throws FileNotFoundException
	{
		leggiPartite();
		leggiTornei();
		return gareLette;
	}

	public void leggiPartite() throws FileNotFoundException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src\\partite";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		//ottengo il vettore di file con tutti i file presenti nella cartella
		File[] cartelle = f.listFiles();
		for(int i = 0; i < cartelle.length; i++) 
		{
			gareLette.add(leggiNomiPartita(new File(cartelle[i].getPath()+"/nomiGiocatori.txt"), cartelle[i].getName()));
			gareLette.get(i).setMazzo(leggiMazzo(new File(cartelle[i].getPath()+"/mazzo.txt"), cartelle[i].getName()));
			leggiMani(new File(cartelle[i].getPath()+"/mani.txt"),cartelle[i].getName(), gareLette.get(i));
			leggiPunteggi(new File(cartelle[i].getPath()+"/punteggi.txt"),cartelle[i].getName(), gareLette.get(i));
		}
	}

	public void leggiTornei() throws FileNotFoundException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src\\tornei";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		//ottengo il vettore di file con tutti i file presenti nella cartella
		File[] cartelle = f.listFiles();
		for(int i = 0; i < cartelle.length; i++) 
		{
			gareLette.add(leggiNomiTorneo(new File(cartelle[i].getPath()+"/nomi.txt"), cartelle[i].getName()));
			gareLette.get(i).setMazzo(leggiMazzo(new File(cartelle[i].getPath()+"/mazzo.txt"), cartelle[i].getName()));
			leggiMani(new File(cartelle[i].getPath()+"/mani.txt"),cartelle[i].getName(), gareLette.get(i));
			leggiPunteggi(new File(cartelle[i].getPath()+"/punteggi.txt"),cartelle[i].getName(), gareLette.get(i));
		}
	}
	
	private Gara leggiNomiPartita(File f, String codice) throws FileNotFoundException
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
				giocatori.add(new Giocatore(idGiocatore,true));
			}
			else
				giocatori.add(new Giocatore(idGiocatore,false));
		}
		scan.close();
		return new Partita(giocatori,codice);
	}
	
	private Gara leggiNomiTorneo(File f, String codice) throws FileNotFoundException
	{
		ArrayList<Giocatore> giocatori = new ArrayList<>();
		Scanner scan = new Scanner(f);
		while(scan.hasNextLine()) {
			String idGiocatore = scan.nextLine();
			char robot = idGiocatore.charAt(idGiocatore.length() - 1);
			//carattere robot
			if(robot == '*') {
				idGiocatore = idGiocatore.substring(0, idGiocatore.length() - 1);
				giocatori.add(new Giocatore(idGiocatore,true));
			}else
				giocatori.add(new Giocatore(idGiocatore,false));
		}
		scan.close();
		return new Torneo(giocatori,codice);
	}
	
	private Mazzo leggiMazzo(File f, String codice) throws FileNotFoundException 
	{
		Scanner scan = new Scanner(f);
		LinkedList<Carta> carte = new LinkedList<Carta>();
		while(scan.hasNextLine()) 
		{
			carte.add(Carta.valueOf(scan.nextLine()));
		}
		scan.close();
		return new Mazzo(carte);
	}

	private void leggiMani(File f, String codice, Gara g) throws FileNotFoundException
	{
		Scanner scan = new Scanner(f);
		for(int i = 0; scan.hasNextLine(); i++) 
		{
			String mano = scan.nextLine();
			g.giocatori[i].setMano(leggiManoGiocatore(mano));
		}
		scan.close();
	}
	
	private ArrayList<Carta> leggiManoGiocatore(String mano)
	{
		ArrayList<Carta> _mano = new ArrayList<>();
		String[] nomiCarte = mano.split(",");
		for(int i = 0; i < nomiCarte.length; i++)
			_mano.add(Carta.valueOf(nomiCarte[i]));
		return _mano;
	}
	
	private void leggiPunteggi(File f, String codice, Gara g) throws FileNotFoundException
	{
		Scanner scan = new Scanner(f);
		for(int i = 0; scan.hasNextLine(); i++) 
		{
			String punteggio = scan.nextLine();
			g.giocatori[i].setPunteggio(Integer.parseInt(punteggio));
		}
		scan.close();
	}
	
	public void salvaNomiGiocatori(String partitaTorneo) throws IOException
	{
		String path = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara().getCodice();
		Files.createDirectory(Paths.get(path));
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara().getCodice()+"/nomiGiocatori.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		
		PrintWriter fw = new PrintWriter(absolutePath);
		for(Giocatore player : eventoDaSalvare.giocatori) 
		{
			//controllo se il giocatore da salvare è un robot oppure no
			if(player.getRobot())
				fw.println(player.getAlias()+"*");
			else
				fw.println(player.getAlias());
		}
		fw.close();
	}
	
	public void salvaMazzo(String partitaTorneo) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara().getCodice()+"/mazzo.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);

		for(int i = 0; i < eventoDaSalvare.getMazzo().getCarte().size(); i++)
		{
			fw.println(eventoDaSalvare.getMazzo().getCarte().get(i));
		}
		fw.close();
	}
	
	public void salvaMani(String partitaTorneo) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara().getCodice()+"/mani.txt";
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
			//cancello l'ultima virgola
			riga = riga.substring(0, riga.length() - 1);
			fw.println(riga);
		}
		fw.close();
	}
	
	public void salvaPunteggio(String partitaTorneo) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara().getCodice()+"/punteggi.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		for(int i = 0; i < eventoDaSalvare.getGiocatori().length; i++)
		{
			fw.println(eventoDaSalvare.getGiocatori()[i].getPunteggio());
		}
		fw.close();
	}
}