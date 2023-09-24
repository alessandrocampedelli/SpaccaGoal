package classi;

import java.util.ArrayList;

public class Leaderboard {
	private ArrayList<Giocatore> players;
	
	public Leaderboard() {
		players = new ArrayList<>();
		caricaPlayers();
	}
	//metodo che carica i giocatori gia presenti salvati sul file di testo nell'arrayList
	private void caricaPlayers() {
		
	}
	//metodo che salva i giocatori (alias, numero vittorie torneo e numero vittorie partite) su file
	public void salvaPlayers() {
		
	}
	public void addPlayers(Giocatore g) {
		players.add(g);
	}
	public ArrayList<Giocatore> getPlayers(){
		return this.players;
	}
	//controllo se è gia stato creato un giocatore con quell'alias
	public Giocatore giocatoreGiaCreato(String alias) {
		for(Giocatore g : players) {
			if(g.getAlias().equals(alias))
				return g;
		}
		return null;
	}
}
