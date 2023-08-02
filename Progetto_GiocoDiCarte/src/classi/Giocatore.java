package classi;

public class Giocatore {
	private String alias;
	private int nVittorie;
	private boolean robot;
	
	public Giocatore(String alias, boolean robot) {
		this.alias = alias;
		this.nVittorie = 0;
		this.robot = robot;
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
