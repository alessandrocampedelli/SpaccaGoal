package classi;

import java.util.ArrayList;
public class Gare {
	/*
	private ArrayList<Partita> partite;
	private ArrayList<Torneo> tornei;
	public Gare() {
		partite = new ArrayList<>();
		tornei = new ArrayList<>();
	}
	public void aggiungiGara(Gara g)
	{
		if(g instanceof Partita) 
			partite.add((Partita) g);
		else
			tornei.add((Torneo) g);
	}
	public Boolean[] cercaCodice(String codiceUtente)
	{
		Gare
		//posizione 0 = true se il codice esiste, false se non esiste
		//posizione 1 = indica se la partita è già iniziata oppure no
		Boolean[] infoCodice = new Boolean[2];
		for(Gara g: gare)
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
	}*/
	
	private ArrayList<Gara> gare;

	public Gare()
	{
		gare = new ArrayList<Gara>();
	}

	public void aggiungiGara(Gara g)
	{
		gare.add(g);
	}
	public Boolean[] cercaCodice(String codiceUtente)
	{
		//posizione 0 = true se il codice esiste, false se non esiste
		//posizione 1 = indica se la partita è già iniziata oppure no
		Boolean[] infoCodice = new Boolean[2];
		for(Gara g: gare)
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
	
}