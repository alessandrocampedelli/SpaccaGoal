package classi;

public class Giocatore {
	private String alias;
	private int nVittorie;
	
	public Giocatore(String alias) {
		this.alias = alias;
		this.nVittorie = 0;
	}
	public String getAlias() {
		return alias;
	}
	public int getNVittorie() {
		return nVittorie;
	}
	public void aggiungiVittoria() {
		nVittorie = nVittorie + 1;
	}
}
