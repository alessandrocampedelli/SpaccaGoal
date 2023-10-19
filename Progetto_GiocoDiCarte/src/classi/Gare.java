package classi;

import java.util.ArrayList;
import classi.Salvataggio;
import java.io.*;
public class Gare 
{	
	private ArrayList<Gara> gare;

	public Gare()
	{
		gare = new ArrayList<Gara>();
		try 
		{
			Salvataggio caricaGare = new Salvataggio();
			gare = caricaGare.leggiGare();
		}
		catch(FileNotFoundException e) 
		{
			System.out.println(e.getMessage());
		}
	}

	public void aggiungiGara(Gara g)
	{
		gare.add(g);
	}

	public boolean cercaCodice(String codiceUtente, char partitaTorneo)
	{
		//true se il codice esiste, false se non esiste
		boolean infoCodice = false;
		for(Gara g : gare)
		{
			if(codiceUtente.charAt(0) == partitaTorneo)
			{
				//ricerco all'interno delle partite se è presente il codice inserito dall'utente
				if(g.getCodiceGara().equals(codiceUtente))
				{
					infoCodice = true;
					break;
				}
			}
		}
		return infoCodice;
	}

	public boolean cercaCodice(String codiceUtente)
	{
		//indica se la partita è già iniziata oppure no
		boolean infoCodice = false;
		for(Gara g : gare)
		{
			//ricerco all'interno delle partite se è presente il codice inserito dall'utente
			if(g.getCodiceGara().equals(codiceUtente))
			{
				infoCodice = true;
				break;
			}
		}		
		return infoCodice;
	}

	//metodo che mi restituisce la gara dato il suo codice
	public Gara getGara(String codice)  throws FileNotFoundException
	{
		Gara gara = null;
		if(codice.charAt(0) == 't') {
			for(Gara g: this.gare)
			{
				if(g.getCodiceGara().equals(codice))
				{
					gara = getPartitaDiTorneo(codice);
				}

			}
		}else {
			for(Gara g: this.gare)
			{
				if(g.getCodiceGara().equals(codice))
				{
					gara = g;
					break;
				}

			}
		}
		return gara;
	}
	private Partita getPartitaDiTorneo(String codice) throws FileNotFoundException{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src\\tornei\\"+codice;
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		//ottengo il vettore di file con tutti i file presenti nella cartella
		File[] partite = f.listFiles();
		Partita p = creaPartita(partite[1]);
		return p;
	}
	private Partita creaPartita(File p) throws FileNotFoundException {
		Partita partita = new Partita();
		Salvataggio s = new Salvataggio(partita);
		partita = (Partita) s.leggiNomiPartita(new File(p.getPath()+"/nomi.txt"), p.getName());
		s.leggiMazzo(new File(p.getPath()+"/mazzo.txt"), p.getName(), partita);
		s.leggiMani(new File(p.getPath()+"/mani.txt"),p.getName(), partita);
		s.leggiPunteggi(new File(p.getPath()+"/punteggi.txt"),p.getName(), partita);
		return partita;
	}
	public ArrayList<String> restituisciGiocatori(String codiceUtente)
	{
		ArrayList<String> giocatori = new ArrayList<String>();
		for(Gara g : gare)
		{
			if(g.getCodiceGara().equals(codiceUtente))
			{
				for(int i = 0; i < g.getGiocatori().length; i++)
				{
					giocatori.add(g.getGiocatori()[i].getAlias());
				}
			}
		}
		return giocatori;
	}

	public ArrayList<Gara> getGare()
	{
		return this.gare;
	}

	public String toString() 
	{
		String nomi = "";
		for(int i = 0; i < gare.size();i++) {
			nomi += gare.get(i).codice+"\n";
		}
		return nomi;
	}
}