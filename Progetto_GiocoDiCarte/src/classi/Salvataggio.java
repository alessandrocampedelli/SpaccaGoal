package classi;
import classi.Gara;
import classi.Giocatore;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Salvataggio {
	Gara eventoDaSalvare;
	Mazzo mazzoGara;
	ArrayList<Gara> gareLette;
	public Salvataggio(Gara g) 
	{
		this.eventoDaSalvare = g;
	}
	public Salvataggio(Mazzo c) 
	{
		this.mazzoGara = c;
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
		//ottengo il percorso della cartella contenente tutti i file
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src\\partite";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		//ottengo il vettore di file
		File[] cartelle = f.listFiles();
		for(int i = 0; i < cartelle.length; i++) 
		{
			gareLette.add(leggiNomiPartita(new File(cartelle[i].getPath()+"/nomiGiocatori.txt"), cartelle[i].getName()));
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
		return new Partita(giocatori,codice);
	}

	public void leggiTornei() throws FileNotFoundException
	{
		//ottengo il percorso della cartella contenente tutti i file
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src\\tornei";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		//ottengo il vettore di file
		File[] cartelle = f.listFiles();
		for(int i = 0; i < cartelle.length; i++) 
		{
			gareLette.add(leggiNomiTorneo(new File(cartelle[i].getPath()+"/nomiGiocatori.txt"), cartelle[i].getName()));
		}
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
		return new Torneo(giocatori,codice);
	} 

	//diventa salvaGiocatori()
	public void salvaNomiPartita() throws IOException
	{
		String path = "src/partite/"+eventoDaSalvare.getCodiceGara().getCodice();
		Files.createDirectory(Paths.get(path));

		// Ottieni il percorso assoluto della directory di lavoro corrente
		String currentDirectory = System.getProperty("user.dir");

		// Costruisci il percorso relativo al file
		String relativePath = "src/partite/"+eventoDaSalvare.getCodiceGara().getCodice()+"/nomiGiocatori.txt";

		// Costruisci il percorso assoluto al file
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter fw = new PrintWriter(absolutePath);
		for(Giocatore player : eventoDaSalvare.giocatori) {
			//controllo se il giocatore da salvare è un robot oppure no
			if(player.getRobot())
				fw.println(player.getAlias()+"*");
			else
				fw.println(player.getAlias());
		}
		fw.close();
	}

	public void salvaTorneo() throws IOException
	{
		String path = "src/tornei/"+eventoDaSalvare.getCodiceGara().getCodice();
		Files.createDirectory(Paths.get(path));
		// Ottieni il percorso assoluto della directory di lavoro corrente
		String currentDirectory = System.getProperty("user.dir");

		// Costruisci il percorso relativo al file
		String relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara().getCodice()+"/nomiGiocatori.txt";

		// Costruisci il percorso assoluto al file
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter fw = new PrintWriter(absolutePath);
		for(Giocatore player : eventoDaSalvare.giocatori) {
			//controllo se il giocatore da salvare è un robot oppure no
			if(player.getRobot())
				fw.println(player.getAlias()+"*");
			else
				fw.println(player.getAlias());
		}
		fw.close();
	}

	public void salvaMazzo() throws IOException
	{
		// Ottieni il percorso assoluto della directory di lavoro corrente
		String currentDirectory = System.getProperty("user.dir");

		// Costruisci il percorso relativo al file
		String relativePath = "src/partite/"+eventoDaSalvare.getCodiceGara().getCodice()+"/mazzoPartita.txt";

		// Costruisci il percorso assoluto al file
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter fw = new PrintWriter(absolutePath);

		fw.close();
	}
}