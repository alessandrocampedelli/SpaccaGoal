package classi;

import java.util.ArrayList;

public class Giocatore 
{
	private String alias;
	private int nVittorie;
	private boolean robot;
	private ArrayList<Carta> mano;
	private int punteggio;
	
	public Giocatore(String alias, boolean robot) 
	{
		this.alias = alias;
		this.robot = robot;
		this.nVittorie = 0;
		this.punteggio = 0;
		this.mano = new ArrayList<>(); 
	}
	public String getAlias() 
	{
		return alias;
	}
	public int getNVittorie() 
	{
		return nVittorie;
	}
	public boolean getRobot()
	{
		return robot;
	}
	public int getPunteggio() {
		return this.punteggio;
	}
	public ArrayList<Carta> getMano(){
		return mano;
	}
	public void setMano(ArrayList<Carta> mano) {
		this.mano = mano;
	}
	public void setPunteggio(int p) {
		this.punteggio = p;
	}
	//metodo che restituisce un vettore di stringhe contenenti i nomi delle carte della mano
	public String[] getManoNomi(){
		String[] nomiCarte = new String[mano.size()];
		for(int i = 0; i < nomiCarte.length; i++) {
			nomiCarte[i] = mano.get(i).name();
		}
		return nomiCarte;
	}
	public void aggiungiVittoria() 
	{
		nVittorie = nVittorie + 1;
	}
	public void aggiungiGoal() {
		this.punteggio+=1;
	}
	public void rimuoviGoal() {
		this.punteggio-=1;
	}
}