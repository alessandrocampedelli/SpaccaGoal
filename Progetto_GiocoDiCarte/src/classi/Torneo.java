package classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;

//la classe "Torneo" derivata dalla classe "Gara"
public class Torneo extends Gara
{	
	//l'ArrayList di partite con tutte le partite già create da svolgere nel torneo
	ArrayList<Partita> partite;
	//l'ArrayList dei giocatori vincenti che proseguiranno il torneo e del tabellone dei giocatori per la stampa in output
	private ArrayList<Giocatore> giocatoriVincenti;
	private ArrayList<Giocatore> tabelloneGiocatori;
	
	//metodo costruttore della classe "Torneo"
	public Torneo(ArrayList<Giocatore> giocatori, String codice) 
	{
		//viene ripreso il contenuto della classe "Gara"
		super(giocatori, codice);
		//l'ArrayList dei giocatori vincenti conterrà tutti i giocatori che stanno ancora partecipando al torneo
		this.giocatoriVincenti = giocatori;
		this.partite = new ArrayList<>();
		Salvataggio s = new Salvataggio(this);
		try 
		{
			//metodo che permette all'ArrayList l'ordine di tutti i giocatori del tabellone
			this.tabelloneGiocatori = s.leggiTabelloneTorneo();
		}
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	//proprietà che restituisce l'ArrayList di partite
	public ArrayList<Partita> getPartite()
	{
		return this.partite;
	}
	
	//metodo che permette di aggiungere una partita all'ArrayList di partite
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
	
	public void setTabelloneGiocatori(ArrayList<Giocatore> tabelloneGiocatori) 
	{
		this.tabelloneGiocatori = tabelloneGiocatori;
	}
	
	//metodo che permette di restituire la prima partita da giocare del torneo
	public Partita getPartitaTorneo()
	{
		return this.partite.get(0);
	}

	//metodo che permette di trovare un giocatore dall'ArrayList dei giocatori vincenti dato l'alias
	private Giocatore trovaGiocatore(String alias) 
	{
		Giocatore trovato = null;
		for(Giocatore g : this.giocatoriVincenti) 
		{
			if(g.getAlias().equals(alias))
				trovato = g;
		}
		//ritorna un oggetto di tipo "Giocatore"
		return trovato;
	}
	
	//metodo che permette la creazione delle partite di un torneo
	public void creazionePartite() 
	{
		//creo le partite e le aggiungo all'ArrayList di partite da giocare
		for(int i = 0, k = 1; i < this.giocatori.length; i = i + 2,k++) 
		{
			ArrayList<Giocatore> g = new ArrayList<>();
			g.add(giocatori[i]);
			g.add(giocatori[i+1]);
			//creo l'oggetto di tipo partita con il codice e un numero crescente per distinguerla tra le altre
			Partita p = new Partita(g,this.codice+k);
			partite.add(p);
		}
	}	

	//metodo che restituisce la classifica finale della partita del torneo
	public String mostraRisultati() 
	{
		String output = "CLASSIFICA FINALE:\n";
		for(Giocatore g : partite.get(0).giocatori) 
		{
			output += g.getAlias() + ": " + g.getPunteggio() + "\n";
		}
		//ritorna nel programma principale la stringa appena creata
		return output;
	}
	
	//metodo che restituisce il giocatore vincente e se il giocatore è un robot oppure no
	public String stampaVincenti() 
	{
		String players = "";
		for(Giocatore g : this.giocatoriVincenti) 
		{
			players += g.getAlias() + (g.isRobot() ? " (Robot)" : "") + "\n";
		}
		//ritorna la stringa con il giocatore vincente
		return players;
	}
	
	//metodo che permette di decretare la fine partita di una partita di un torneo o del torneo generale se le partite fossero finite
	public void finePartita(ActionEvent event, String aliasVincente, String aliasPerdente, Alert_cambiaForm alert, Leaderboard leaderboard) throws IOException
	{
		Giocatore vincente = trovaGiocatore(aliasVincente);
		Giocatore perdente = trovaGiocatore(aliasPerdente);
		//rimuovo il giocatore perdende dall'ArrayList contenente i giocatori vincenti
		this.giocatoriVincenti.remove(perdente);
		s = new Salvataggio(partite.get(0));
		//elimino la cartella della partita appena terminata
		s.deleteDirectory("tornei/"+this.getCodiceGara()+"/"+partite.get(0).codice);
		Salvataggio s = new Salvataggio(this);
		s.salvaGiocatoriTorneo();
		for(Giocatore giocatoreVincente: this.tabelloneGiocatori)
		{
			if(giocatoreVincente.getAlias().equals(aliasVincente))
			{
				//aggiungo al tabellone dei giocatori una riga con il giocatore che ha vinto la partita del torneo
				this.tabelloneGiocatori.add(giocatoreVincente);
				break;
			}
		}
		//salvo il tabellone dei giocatori del torneo
		s.salvaTabelloneTorneo();
		//controllo se è rimasto un solo giocatore vincente (significa che il giocatore ha vinto il torneo) altrimenti il giocatore ha vinto la partita
		if(this.giocatoriVincenti.size() != 1)
		{
			alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO LA PARTITA");
			//controllo se il turno è terminato
			if(fineTurno()) 
			{
				//ricreo le partite in gare, sovrascrivo il torneo nell'Arraylist di "Gara" e lo ricreo solo con i giocatori vincenti
				Gare g = new Gare();
				g.updateTorneo(giocatoriVincenti, codice);
			}
		}
		else
		{
			//aggiungo la vittoria del torneo al giocatore e aggiorno la leaderboard
			this.giocatoriVincenti.get(0).aggiungiVittoriaTorneo();
			leaderboard.getPlayers().get(leaderboard.indexPlayer(aliasVincente)).aggiungiVittoriaTorneo();
			//salvo il file di testo con i valori aggiornati e mostro le informazioni all'utente in output
			leaderboard.salvaPlayers();
			alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO IL TORNEO");
		}
		inviaMail(new Giocatore[] {vincente,perdente});
		alert.passaAlForm("/application/FormTabelloneTorneo.fxml", event);
	}
	
	//metodo booleano che mi permette di controllare se sono finite le partite di un torneo da giocare
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
			//è presente solo il file di testo, quindi le partite da giocare sono finite e ritorna la variabile booleana con valore "true"
			return true;	
		}
		//sono presenti altre partite quindi ritorna la variabile booleana con valore "false"
		return false;
	}
}