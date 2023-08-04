package classi;

import java.util.ArrayList;

public class Partita extends Gara{
	
	public Partita(ArrayList<Giocatore> giocatori, String codice) {
		super(giocatori, codice);
	}
	public String toString() {
		String output = "Codice: "+codice.getCodice()+"\nGiocatori:\n";
		for(int i = 0; i < giocatori.length; i++) {
			output+= (i+1)+") "+giocatori[i].getAlias()+"\n";
		}
		return output;
	}
}
