package classi;

import java.io.File;
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
		String output = "CLASSIFICA FINALE:\n";
		for(Giocatore g : partite.get(0).giocatori) {
			output += g.getAlias()+": "+g.getPunteggio()+"\n";
		}
		return output;
	}
	public void showFinePartita(ActionEvent event, String aliasVincente, String aliasPerdente, Alert_cambiaForm alert) throws IOException
	{
		this.giocatoriVincenti.remove(trovaGiocatore(aliasPerdente));
		alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO LA PARTITA");
		//la partita è terminata, mostro all'utente la leaderboard e elimino la cartella della partita
		alert.passaAlForm("/application/FormTabelloneTorneo.fxml", event);
		s = new Salvataggio(partite.get(0));
		s.deleteDirectory("tornei/"+this.getCodiceGara());
		//controllare se il turno è finito
		if(fineTurno()) {
			//riscrivere il file di testo
			Salvataggio s = new Salvataggio(this);
			s.salvaGiocatoriTorneo();
			//ricreare le partite--> sovrascrivere il torneo nell'arraylist di gara e ricrearlo solo con i giocatori vincenti
			Gare g = new Gare();
			g.updateTorneo(giocatoriVincenti, codice);
		}
	}
	private boolean fineTurno(){
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+this.codice;
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		//ottengo il vettore di file con tutti i file presenti nella cartella
		File[] tornei = f.listFiles();
		//mi chiedo se la directory contiene solo il file di testo dei giocatori
		if(tornei.length == 1 && !tornei[0].isDirectory()) {
			return true;	
		}
		return false;
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