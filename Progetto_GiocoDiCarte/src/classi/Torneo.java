package classi;

import java.util.ArrayList;

public class Torneo extends Gara
{	
	ArrayList<Partita> partite;
	ArrayList<Giocatore> players;
	public Torneo(ArrayList<Giocatore> giocatori, String codice) 
	{
		super(giocatori, codice);
		this.players = new ArrayList<>();
		this.partite = new ArrayList<>();
	}
}