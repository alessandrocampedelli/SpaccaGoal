package classi;
import java.util.ArrayList;

public class Tornei 
{
	private ArrayList<Torneo> tornei;
	
	public Tornei()
	{
		tornei = new ArrayList<Torneo>();
	}
	
	public void aggiungiTorneo(Torneo t)
	{
		tornei.add(t);
	}
	
	//ricerco se è presente il codice inserito dall'utente 
	public boolean cercaCodice(String codiceUtente)
	{
		boolean codicePresente = false;
		for(Torneo t: tornei)
		{
			//ricerco all'interno delle partite se è presente il codice inserito dall'utente
			if(t.getCodiceGara().getCodice().equals(codiceUtente))
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
		for(Torneo t: tornei)
		{
			if(t.getCodiceGara().getCodice().equals(codiceUtente))
			{
				partitaCodice = t.getCodiceGara().getNuovoCarica();
			}
		}
		return partitaCodice;
	}
}