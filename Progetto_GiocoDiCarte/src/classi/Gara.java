package classi;

import java.util.ArrayList;
public abstract class Gara {
	protected Giocatore[] giocatori;
	protected Codice codice;
	protected Mazzo carte;
	protected void aggiungiGiocatori(ArrayList<Giocatore> giocatori, String codice) {
		this.giocatori = giocatori.toArray(new Giocatore[giocatori.size()]);
		this.codice = new Codice(codice);
	}
	
	protected Codice getCodiceGara()
	{
		return this.codice;
	}
}
