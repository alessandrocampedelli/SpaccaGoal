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
		File[] partite = f.listFiles();
		for(int i = 0; i < partite.length; i++) 
		{
			gareLette.add(leggiNomiPartita(new File(partite[i].getPath()+"/nomi.txt"), partite[i].getName()));
			leggiMazzo(new File(partite[i].getPath()+"/mazzo.txt"), partite[i].getName(), gareLette.get(gareLette.size() - 1));
			leggiMani(new File(partite[i].getPath()+"/mani.txt"),partite[i].getName(), gareLette.get(gareLette.size() - 1));
			leggiPunteggi(new File(partite[i].getPath()+"/punteggi.txt"),partite[i].getName(), gareLette.get(gareLette.size() - 1));
		}
	}

	public void leggiTornei() throws FileNotFoundException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src\\tornei";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		//ottengo il vettore di file con tutti i file presenti nella cartella
		File[] tornei = f.listFiles();
		for(int i = 0; i < tornei.length; i++) 
		{
			Torneo torneo = leggiGiocatoriTorneo(new File(tornei[i].getPath()+"/giocatoriTorneo.txt"), tornei[i].getName());
			gareLette.add(torneo);
			File[] partite = tornei[i].listFiles();
			for(int j = 0; j < partite.length; j++) 
			{
				//controllo se è una cartella, in questo modo entro in questo ciclo
				if(partite[j].isDirectory())
				{
					Partita p = (Partita) leggiNomiPartita(new File(partite[j].getPath()+"/nomi.txt"), partite[j].getName());
					leggiMazzo(new File(partite[j].getPath()+"/mazzo.txt"), partite[j].getName(), p);
					leggiMani(new File(partite[j].getPath()+"/mani.txt"), partite[j].getName(), p);
					leggiPunteggi(new File(partite[j].getPath()+"/punteggi.txt"),partite[j].getName(), p);
					torneo.aggiungiPartita(p);
				}
			}
		}
	}
	
	public Gara leggiNomiPartita(File f, String codice) throws FileNotFoundException
	{
		ArrayList<Giocatore> giocatori = leggiGiocatori(f);
		return new Partita(giocatori,codice);
	}
	
	private Torneo leggiGiocatoriTorneo(File f, String codice) throws FileNotFoundException
	{
		ArrayList<Giocatore> giocatori = leggiGiocatori(f);
		return new Torneo(giocatori,codice);
	}
	
	public ArrayList<Giocatore> leggiTabelloneTorneo() throws FileNotFoundException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara()+"/tabelloneTorneo.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		if(f.exists()) {
			ArrayList<Giocatore> giocatori = leggiGiocatori(f);
			return giocatori;
		}
		return null;
	}
	
	public void leggiMazzo(File f, String codice, Gara g) throws FileNotFoundException 
	{
		Scanner scan = new Scanner(f);
		LinkedList<Carta> carte = new LinkedList<Carta>();
		while(scan.hasNextLine()) 
		{
			carte.add(Carta.valueOf(scan.nextLine()));
		}
		g.setMazzo(new Mazzo(carte));
		scan.close();
	}

	public void leggiMani(File f, String codice, Gara g) throws FileNotFoundException
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
	
	public void leggiPunteggi(File f, String codice, Gara g) throws FileNotFoundException
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
		String path = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara();
		Files.createDirectory(Paths.get(path));
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+eventoDaSalvare.getCodiceGara()+"/nomi.txt";
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
	
	public void salvaGiocatoriTorneo() throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara()+"/giocatoriTorneo.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		Torneo t = (Torneo) eventoDaSalvare;
		salvaFile(absolutePath, t.getGiocatoreVincenti());
	}
	public void salvaFile(String absolutePath, ArrayList<Giocatore> players) throws FileNotFoundException {
		PrintWriter fw = new PrintWriter(absolutePath);
		for(Giocatore player : players) 
		{
			//controllo se il giocatore da salvare è un robot oppure no
			if(player.getRobot())
				fw.println(player.getAlias()+"*");
			else
				fw.println(player.getAlias());
		}
		fw.close();
	}
	public void salvaTabelloneTorneo() throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara()+"/tabelloneTorneo.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		Torneo t = (Torneo) eventoDaSalvare;
		salvaFile(absolutePath, t.getTabelloneGiocatori());
	}
	
	public void salvaMazzo(String partitaTorneo) throws IOException
	{
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
	
	public void salvaMani(String partitaTorneo) throws IOException
	{
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
	
	public void salvaPunteggio(String partitaTorneo) throws IOException
	{
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
				giocatori.add(new Giocatore(idGiocatore,true));
			}
			else
				giocatori.add(new Giocatore(idGiocatore,false));
		}
		scan.close();
		return giocatori;
	}
	
	//metodo che elimina la cartella della partita terminata
    public void deleteDirectory(String percorso)
    {
		String path = "src/"+percorso+"/"+eventoDaSalvare.getCodiceGara();
		File file = new File(path);
        for (File subfile : file.listFiles()) {
            if (subfile.isDirectory()) {
                deleteDirectory(percorso);
            }
            subfile.delete();
        }
		file.delete();
    }
    
    public void createDirectory(String percorso) throws IOException{
    	String path = "src/"+percorso+"/"+eventoDaSalvare.getCodiceGara();
    	Files.createDirectory(Paths.get(path));
    }
}