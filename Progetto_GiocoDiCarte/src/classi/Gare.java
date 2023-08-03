package classi;

import java.util.ArrayList;
public class Gare {

	private ArrayList<Gara> gare;

	public Gare()
	{
		gare = new ArrayList<Gara>();
	}

	public void aggiungiGara(Gara g)
	{
		gare.add(g);
	}

	//ricerco se è presente il codice inserito dall'utente 
	public boolean cercaCodice(String codiceUtente)
	{
		boolean codicePresente = false;
		for(Gara g: gare)
		{
			//ricerco all'interno delle partite se è presente il codice inserito dall'utente
			if(g.getCodiceGara().getCodice().equals(codiceUtente))
			{
				codicePresente = true;
				break;
			}
		}
		return codicePresente;
	}

	public boolean partitaNuovaRicominciata(String codiceUtente)
	{
		boolean partitaCodice = false;
		for(Gara g: gare)
		{
			if(g.getCodiceGara().getCodice().equals(codiceUtente))
			{
				partitaCodice = g.getCodiceGara().getNuovoCarica();
			}
		}
		return partitaCodice;
	}
}