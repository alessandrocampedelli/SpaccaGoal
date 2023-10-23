package classi;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;

public class Torneo extends Gara
{	
	ArrayList<Partita> partite;
	private ArrayList<Giocatore> giocatoriVincenti;
	public Torneo(ArrayList<Giocatore> giocatori, String codice) 
	{
		super(giocatori, codice);
		this.partite = new ArrayList<>();
		this.giocatoriVincenti = giocatori;
	}
	
	public void creazionePartite() 
	{
		for(int i = 0, k = 1; i < this.giocatori.length; i+=2,k++) 
		{
			ArrayList<Giocatore> g = new ArrayList<>();
			g.add(giocatori[i]);
			g.add(giocatori[i+1]);
			Partita p = new Partita(g,this.codice+k);
			partite.add(p);
		}
	}
	
	public ArrayList<Partita> getPartite()
	{
		return this.partite;
	}
	
	public void aggiungiPartita(Partita p)
	{
		this.partite.add(p);
	}
	
	public Partita getPartitaTorneo()
	{
		return this.partite.get(0);
	}
	public String mostraRisultati() 
	{
		return super.mostraRisultati();
	}
	public void showFinePartita(ActionEvent event, String aliasVincente, String aliasPerdente, Alert_cambiaForm alert) throws IOException
	{
		this.giocatoriVincenti.remove(trovaGiocatore(aliasPerdente));
		alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO LA PARTITA");
		//la partita è terminata, mostro all'utente la leaderboard e elimino la cartella della partita
		alert.passaAlForm("/application/FormTabelloneTorneo.fxml", event);
		s.deleteDirectory("tornei/"+this.getCodiceGara());
	}
	private Giocatore trovaGiocatore(String alias) {
		Giocatore trovato = null;
		for(Giocatore g : this.giocatoriVincenti) {
			if(g.getAlias().equals(alias))
				trovato = g;
		}
		return trovato;
	}
}