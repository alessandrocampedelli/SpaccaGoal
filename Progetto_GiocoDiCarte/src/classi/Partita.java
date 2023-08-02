package classi;

import java.util.ArrayList;

public class Partita extends Gara{
	
	public Partita(ArrayList<Giocatore> giocatori, String codice) {
		aggiungiGiocatori(giocatori, codice);
	}
}
