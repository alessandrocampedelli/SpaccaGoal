package classi;

import java.util.ArrayList;
public abstract class Gara 
{
	protected Giocatore[] giocatori;
	protected Codice codice;
	protected Mazzo carte;
	protected final int N_CARTE_INIZIO = 5;
	public Gara(ArrayList<Giocatore> giocatori, String codice) 
	{
		this.giocatori = giocatori.toArray(new Giocatore[giocatori.size()]);
		this.codice = new Codice(codice);
		this.carte = new Mazzo();
	}
	public void distribuzioneCarte() {
		//pulisco le mani dei giocatori da eventuali partite precedenti
		pulisciMani();
		carte.mischia();
		//distribuzione delle carte
		for(Giocatore g : giocatori) {
			for(int i = 0; i < N_CARTE_INIZIO; i++)
				g.getMano().add(carte.pesca());
		}
	}
	
	private void pulisciMani() {
		for(Giocatore g : giocatori)
			g.getMano().clear();
	}
	
	public Codice getCodiceGara()
	{
		return this.codice;
	}
	
	public Giocatore[] getGiocatori()
	{
		return this.giocatori;
	}
	
	public Mazzo getMazzo() 
	{
		return carte;
	}
}