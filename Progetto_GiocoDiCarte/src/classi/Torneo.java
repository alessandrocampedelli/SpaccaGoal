package classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;

public class Torneo extends Gara
{	
	ArrayList<Partita> partite;
	private ArrayList<Giocatore> giocatoriVincenti;
	private ArrayList<Giocatore> tabelloneGiocatori;
	
	public Torneo(ArrayList<Giocatore> giocatori, String codice) 
	{
		super(giocatori, codice);
		this.partite = new ArrayList<>();
		Salvataggio s = new Salvataggio(this);
		try 
		{
			this.tabelloneGiocatori = s.leggiTabelloneTorneo();
		}
		catch(FileNotFoundException e) 
		{
			System.out.println(e.getMessage());
		}
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
	
	public ArrayList<Giocatore> getGiocatoreVincenti()
	{
		return this.giocatoriVincenti;
	}
	
	public ArrayList<Giocatore> getTabelloneGiocatori()
	{
		return this.tabelloneGiocatori;
	}
	
	public Partita getPartitaTorneo()
	{
		return this.partite.get(0);
	}

	public void setTabelloneGiocatori(ArrayList<Giocatore> tabelloneGiocatori) 
	{
		this.tabelloneGiocatori = tabelloneGiocatori;
	}
	
	public String mostraRisultati() 
	{
		String output = "CLASSIFICA FINALE:\n";
		for(Giocatore g : partite.get(0).giocatori) 
		{
			output += g.getAlias()+": "+g.getPunteggio()+"\n";
		}
		return output;
	}
	
	public String stampaVincenti() 
	{
		String players = "";
		for(Giocatore g : this.giocatoriVincenti) 
		{
			players +=g.getAlias()+"\n";
		}
		return players;
	}
	
	public void finePartita(ActionEvent event, String aliasVincente, String aliasPerdente, Alert_cambiaForm alert, Leaderboard leaderboard) throws IOException
	{	
		this.giocatoriVincenti.remove(trovaGiocatore(aliasPerdente));
		s = new Salvataggio(partite.get(0));
		s.deleteDirectory("tornei/"+this.getCodiceGara());
		//riscrivere il file di testo
		Salvataggio s = new Salvataggio(this);
		s.salvaGiocatoriTorneo();
		for(Giocatore giocatoreVincente: this.tabelloneGiocatori)
		{
			if(giocatoreVincente.getAlias().equals(aliasVincente))
			{
				this.tabelloneGiocatori.add(giocatoreVincente);
				break;
			}
		}
		s.salvaTabelloneTorneo();
		if(this.giocatoriVincenti.size() != 1)
		{
			alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO LA PARTITA");
			//controllare se il turno è finito
			if(fineTurno()) 
			{
				//ricreare le partite--> sovrascrivere il torneo nell'arraylist di gara e ricrearlo solo con i giocatori vincenti
				Gare g = new Gare();
				g.updateTorneo(giocatoriVincenti, codice);
			}
		}
		else
		{
			this.giocatoriVincenti.get(0).aggiungiVittoriaTorneo();
			leaderboard.getPlayers().get(leaderboard.indexPlayer(aliasVincente)).aggiungiVittoriaTorneo();
			//aggiornata una vittoria nella leaderboard, risalvo il file di testo con i valori aggiornati
			leaderboard.salvaPlayers();
			alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO IL TORNEO");
		}
		//la partita è terminata, mostro all'utente la leaderboard e elimino la cartella della partita
		alert.passaAlForm("/application/FormTabelloneTorneo.fxml", event);
	}
	
	private boolean fineTurno()
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/tornei/"+this.codice;
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		//ottengo il vettore di file con tutti i file presenti nella cartella
		File[] tornei = f.listFiles();
		//mi chiedo se la directory contiene solo i file di testo dei giocatori
		if(tornei.length == 2 && !tornei[0].isDirectory() && !tornei[1].isDirectory()) 
		{
			return true;	
		}
		return false;
	}
	
	private Giocatore trovaGiocatore(String alias) 
	{
		Giocatore trovato = null;
		for(Giocatore g : this.giocatoriVincenti) 
		{
			if(g.getAlias().equals(alias))
				trovato = g;
		}
		return trovato;
	}
}