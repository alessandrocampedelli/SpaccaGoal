package classi;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

//classe che contiene tutto il mazzo che viene utilizzato durante la gara
public class Mazzo 
{
	//utilizzo una LinkedList perchè mi servono due indici (carta pescata rimuovo la prima posizione e carta scartata la aggiungo all'ultima posizione)
	private LinkedList<Carta> carte;
	//vettore che contiene tutte le 60 carte del mazzo
	private Carta[] _carte = new Carta[] {Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,
			Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,
			Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.RIGORE,Carta.RIGORE,Carta.RIGORE,Carta.RIGORE
			,Carta.ROVESCIATA_DELLANNO,Carta.TIRO_DOMENICA,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE
			,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE
			,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.PORTIERE,Carta.PORTIERE
			,Carta.PORTIERE,Carta.PORTIERE,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.VAR,Carta.VAR,Carta.VAR
			,Carta.FUORIGIOCO,Carta.FUORIGIOCO,Carta.FUORIGIOCO,Carta.MISTER,Carta.MISTER};
	
	//metodo costuttore della classe "Mazzo" che istanzia la LinkedList di carte e la riempie con le carte nell'ordine del vettore
	public Mazzo()
	{
		carte = new LinkedList<Carta>(Arrays.asList(_carte));
	}
	
	//metodo overridato della classe "Mazzo" che permette di istanziare la LinkedList da quella passata come parametro
	public Mazzo(LinkedList<Carta> carteMazzo)
	{
		carte = new LinkedList<Carta>(carteMazzo);
	}
	
	public LinkedList<Carta> getCarte()
	{
		return this.carte;
	}
	
	//metodo che permette tramite il metodo "shuffle" di mischiare il mazzo
	public void mischia() 
	{
		Collections.shuffle(carte);
	}

	//metodo che permette di pescare una carta (rimuove la prima posizione della LinkedList)
	public Carta pesca() 
	{
		return carte.removeFirst();
	}
	
	//metodo che permette di scartare una carta (aggiunge la carta scartata nell'ultima posizione della LinkedList)
	public void scarta(Carta c) 
	{
		carte.addLast(c);
	}
}