package classi;

import java.util.ArrayList;
import java.io.*;

//classe Gare che gestisce tutti gli oggetti di classe "Gara" (partite e tornei) create e presenti nel programma 
public class Gare 
{	
	//l'ArrayList con tutte le gare
	private ArrayList<Gara> gare;

	//metodo costruttore della classe "Gare" che permette di caricare nell'ArrayList tutte le gare salvate nei file tramite la classe "Salvataggio"
	public Gare()
	{
		//istanzio l'ArrayList delle gare
		gare = new ArrayList<Gara>();
		//tramite un blocco try/catch controllo se ci fossero problemi nell'apertura dei file di testo
		try 
		{
			//dichiario un oggetto della classe "Salvataggio" che permette di leggere tutte le gare presenti nei file
			Salvataggio caricaGare = new Salvataggio();
			gare = caricaGare.leggiGare();
		}
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	public ArrayList<Gara> getGare()
	{
		return this.gare;
	}
	
	//metodo che permette di aggiungere una gara all'interno dell'ArrayList di gare
	public void aggiungiGara(Gara g)
	{
		gare.add(g);
	}

	//metodo booleano che mi restituisce se è presente il codice all'interno dell'ArrayList diversificandolo se è una partita o un torneo
	public boolean cercaCodice(String codiceUtente, char partitaTorneo)
	{
		boolean infoCodice = false;
		for(Gara g : gare)
		{
			//viene anche passato come parametro se è una partita ('p') oppure un torneo ('t') per fare la ricerca di una partita o di un torneo
			if(codiceUtente.charAt(0) == partitaTorneo)
			{
				//ricerco all'interno delle gare se è presente il codice inserito dall'utente 
				if(g.getCodiceGara().equals(codiceUtente))
				{
					infoCodice = true;
					break;
				}
			}
		}
		//restituisce true se il codice esiste, false se il codice non esiste
		return infoCodice;
	}

	//metodo che mi restituisce l'oggetto "Gara" dato come parametro il codice della gara
	public Gara getGara(String codice)  throws FileNotFoundException
	{
		Gara gara = null;
		//determino con questo if se il codice inserito è di una partita oppure di un torneo
		if(codice.charAt(0) == 't') 
		{
			for(Gara g: this.gare)
			{
				if(g.getCodiceGara().equals(codice))
				{
					//converto l'oggetto "Gara" in un oggetto di tipo "Torneo" e restituisco un oggetto con la prima partita da disputare
					Torneo t = (Torneo) g;
					gara = t.partite.get(0);
					break;
				}
			}
		}
		else 
		{
			for(Gara g: this.gare)
			{
				if(g.getCodiceGara().equals(codice))
				{
					//restituisco la gara trovata dal suo codice
					gara = g;
					break;
				}
			}
		}
		//ritorna nel programma principale l'oggetto "Gara" dal suo codice
		return gara;
	}

	//metodo che mi restituisce l'oggetto di tipo "Torneo" dato come parametro il codice del torneo
	public Torneo getTorneo(String codice) 
	{
		Torneo torneo = null;
		for(Gara t : gare) 
		{
			//ricerco il codice della torneo all'interno delle gare
			if(t.getCodiceGara().equals(codice))
			{
				//converto l'oggetto di tipo "Gara" a tipo "Torneo"
				torneo = (Torneo) t;
				break;
			}
		}
		//ritorna nel programma principale l'oggetto di tipo "Torneo"
		return torneo;
	}

	//metodo che permette, una volta finiti i quarti o le semifinali di un torneo, di creare il turno successivo
	public void updateTorneo(ArrayList<Giocatore> vincenti, String codice) throws IOException
	{
		//rimuovo la gara precedente e istanzio un nuovo oggetto di tipo "Torneo" con i giocatori che hanno vinto
		gare.remove(getTorneo(codice));
		Torneo t = new Torneo(vincenti,codice);
		//richiamo un metodo della classe "Torneo" che permette di creare le partite e aggiungo il torneo all'ArrayList di "Gara"
		t.creazionePartite();
		gare.add(t);
		//all'interno della cartella del torneo creo tante cartelle per ogni partita, ognuna con tutte le sue informazioni
		for(Partita p : t.getPartite()) 
		{
			Salvataggio salvaGara = new Salvataggio(p);
			salvaGara.salvaPartita(p, "tornei/"+t.getCodiceGara());
		}
	}
	
	//metodo che restuisce i giocatori di una gara dal codice della gara
	public ArrayList<String> restituisciGiocatori(String codiceUtente)
	{
		ArrayList<String> giocatori = new ArrayList<String>();
		for(Gara g : gare)
		{
			if(g.getCodiceGara().equals(codiceUtente))
			{
				for(int i = 0; i < g.getGiocatori().length; i++)
				{
					//aggiungo all'ArrayList tutti gli alias dei giocatori che ne prendono parte
					giocatori.add(g.getGiocatori()[i].getAlias());
				}
			}
		}
		//restituisco l'ArrayList di String con gli alias dei giocatori
		return giocatori;
	}
}