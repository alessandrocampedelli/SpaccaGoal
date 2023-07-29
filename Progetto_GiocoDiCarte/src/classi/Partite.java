package classi;
import java.util.ArrayList;

public class Partite 
{
	private ArrayList<Partita> partite;
	
	public Partite()
	{
		partite = new ArrayList<Partita>();
	}
	
	public void aggiungiPartita(Partita p)
	{
		partite.add(p);
	}
	
	//ricerco se è presente il codice inserito dall'utente 
	public boolean cercaCodice(String codiceUtente)
	{
		boolean codicePresente = false;
		for(Partita p: partite)
		{
			//ricerco all'interno delle partite se è presente il codice inserito dall'utente
			if(p.getCodiceGara().getCodice() == codiceUtente)
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
		for(Partita p: partite)
		{
			if(p.getCodiceGara().getCodice() == codiceUtente)
			{
				partitaCodice = p.getCodiceGara().getNuovoCarica();
			}
		}
		return partitaCodice;
	}
}