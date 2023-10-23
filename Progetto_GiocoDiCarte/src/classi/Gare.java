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
					Torneo t = (Torneo) g;
					gara = t.partite.get(0);
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