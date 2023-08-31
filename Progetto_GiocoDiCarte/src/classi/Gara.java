package classi;

import java.util.ArrayList;
public abstract class Gara 
{
	protected Giocatore[] giocatori;
	protected Codice codice;
	protected Mazzo carte;
	protected ArrayList<Carta> mano;
	
	public Gara(ArrayList<Giocatore> giocatori, String codice) 
	{
		this.giocatori = giocatori.toArray(new Giocatore[giocatori.size()]);
		this.codice = new Codice(codice);
		this.mano = new ArrayList<>(); 
	}
	//public void dareCarteInizio()
	
	public Codice getCodiceGara()
	{
		return this.codice;
	}
	
	public Giocatore[] getGiocatori()
	{
		return this.giocatori;
	}
	
	public Mazzo getMazzo() 
	{
		return carte;
	}
	public ArrayList<Carta> getMano(){
		return mano;
	}
}