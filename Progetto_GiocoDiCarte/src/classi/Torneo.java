package classi;

import java.util.ArrayList;

public class Torneo extends Gara
{	
	ArrayList<Partita> partite;
	public Torneo(ArrayList<Giocatore> giocatori, String codice) 
	{
		super(giocatori, codice);
		this.partite = new ArrayList<>();
		creazionePartite();
	}
	private void creazionePartite() {
		for(int i = 0, k = 1; i < this.giocatori.length; i+=2,k++) {
			ArrayList<Giocatore> g = new ArrayList<>();
			g.add(giocatori[i]);
			g.add(giocatori[i+1]);
			Partita p = new Partita(g,this.codice+k);
			partite.add(p);
		}
	}
	public ArrayList<Partita> getPartite(){
		return this.partite;
	}
}