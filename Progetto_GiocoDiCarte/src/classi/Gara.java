package classi;

import java.util.ArrayList;

//la classe astratta "Gara" che accomuna i campi e metodi uguali della classe "Partita" e "Torneo"
public abstract class Gara 
{
	//campi protected con le informazioni dei giocatori che prendono parte alla gara, il codice e il mazzo di carte della gara
	protected Giocatore[] giocatori;
	protected String codice;
	protected Mazzo carte;
	//campo protected per effettuare il salvataggio della partita corrente
	protected Salvataggio s;
	
	//metodo costruttore della classe gara dove vengono passati come parametri l'arrayList dei giocatori e il codice della gara
	public Gara(ArrayList<Giocatore> giocatori, String codice)
	{
		//converto l'arrayList in un vettore perchè il numero di giocatori è fisso
		this.giocatori = giocatori.toArray(new Giocatore[giocatori.size()]);
		this.codice = codice;
		//instanzio un oggetto della classe mazzo
		this.carte = new Mazzo();
		this.s = new Salvataggio(this);
	}

	public String getCodiceGara()
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
	
	public void setMazzo(Mazzo m) 
	{
		this.carte = m;
	}
	
	//metodo che fa ritornare la stringa con la classifica finale con i punteggi di tutti i giocatori della gara
	public String mostraRisultati() 
	{
		String output = "CLASSIFICA FINALE:\n";
		for(Giocatore g : giocatori) 
		{
			output += g.getAlias()+": "+g.getPunteggio()+"\n";
		}
		return output;
	}
}