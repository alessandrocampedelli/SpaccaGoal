package classi;
import classi.Gara;
import classi.Giocatore;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
public class Salvataggio {
	Gara eventoDaSalvare;
	public Salvataggio(Gara g) 
	{
		eventoDaSalvare = g;
	}
	public Salvataggio() 
	{
	}
	public ArrayList<Gara> leggiPartite() throws FileNotFoundException{
		ArrayList<Gara> partite = new ArrayList<>();
		//ottengo il percorso della cartella contenente tutti i file
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/partite";
		String absolutePath = currentDirectory + File.separator + relativePath;
		
		File f = new File(absolutePath);
		//ottengo il vettore di file
		File[] file = f.listFiles();
		for(int i = 0; i < file.length; i++) {
			partite.add(leggiPartita(file[i].getName()));
		}
		return partite;
	}
	private Gara leggiPartita(String codice) throws FileNotFoundException{
		ArrayList<Giocatore> giocatori = new ArrayList<>();
		Scanner scan = new Scanner(new File(codice));
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
		return new Partita(giocatori,codice);
	}
	public void salvaPartita() throws IOException
	{
		// Ottieni il percorso assoluto della directory di lavoro corrente
        String currentDirectory = System.getProperty("user.dir");

        // Costruisci il percorso relativo al file
        String relativePath = "src/partite/"+eventoDaSalvare.getCodiceGara().getCodice()+".txt";
     
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
		// Ottieni il percorso assoluto della directory di lavoro corrente
        String currentDirectory = System.getProperty("user.dir");

        // Costruisci il percorso relativo al file
        String relativePath = "src/tornei/"+eventoDaSalvare.getCodiceGara().getCodice()+".txt";
     
        // Costruisci il percorso assoluto al file
        String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter fw = new PrintWriter(absolutePath);
		for(Giocatore player : eventoDaSalvare.giocatori) {
			fw.println(player.getAlias());
		}
		fw.close();
	}
}