package classi;
import classi.Gara;
import java.util.Scanner;
import java.io.*;
public class Salvataggio {
	Gara eventoDaSalvare;
	public Salvataggio(Gara g) 
	{
		eventoDaSalvare = g;
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