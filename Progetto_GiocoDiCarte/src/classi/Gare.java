package classi;

import java.util.ArrayList;
import classi.Salvataggio;
import java.io.*;
public class Gare {
	
	private ArrayList<Gara> gare;

	public Gare()
	{
		/*gare = new ArrayList<Gara>();
		ArrayList<Giocatore> giocatori = new ArrayList<Giocatore>();
		giocatori.add(new Giocatore("Matteo",true));
		giocatori.add(new Giocatore("Alessandro",true));
		giocatori.add(new Giocatore("Massimo",true));
		gare.add(new Partita(giocatori, "abcd"));*/
		try {
			Salvataggio caricaPartite = new Salvataggio();
			gare = caricaPartite.leggiPartite();
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void aggiungiGara(Gara g)
	{
		gare.add(g);
	}
	
	public boolean[] cercaCodice(String codiceUtente)
	{
		//posizione 0 = true se il codice esiste, false se non esiste
		//posizione 1 = indica se la partita è già iniziata oppure no
		boolean[] infoCodice = new boolean[2];
		for(Gara g : gare)
		{
			//ricerco all'interno delle partite se è presente il codice inserito dall'utente
			if(g.getCodiceGara().getCodice().equals(codiceUtente))
			{
				infoCodice[0] = true;
				infoCodice[1] = g.getCodiceGara().getNuovoCarica();
				break;
			}
		}
		return infoCodice;
	}
	
	public ArrayList<String> restituisciGiocatori(String codiceUtente)
	{
		ArrayList<String> giocatori = new ArrayList<String>();
		for(Gara g : gare)
		{
			if(g.getCodiceGara().getCodice().equals(codiceUtente))
			{
				for(int i = 0; i < g.getGiocatori().length; i++)
				{
					giocatori.add(g.getGiocatori()[i].getAlias());
				}
			}
		}
		return giocatori;
	}
	
 	public String toString() 
 	{
		String nomi = "";
		for(int i = 0; i < gare.size();i++) {
			nomi += gare.get(i).codice.getCodice()+"\n";
		}
		return nomi;
	}
}