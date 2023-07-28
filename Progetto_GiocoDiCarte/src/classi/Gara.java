package classi;

public abstract class Gara {
	protected Giocatore[] giocatori;
	protected Codice codice;
	protected Mazzo carte;
	protected void aggiungiGiocatori(String[] alias, String codice) {
		this.giocatori = new Giocatore[alias.length];
		for(int i = 0; i < giocatori.length; i++) {
			giocatori[i] = new Giocatore(alias[i]);
		}
		this.codice = new Codice(codice);
	}
}
