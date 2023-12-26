package classi;
import java.util.ArrayList;
import java.util.Random;

//la classe "Robot" intelligente del gioco
public class Robot 
{
	//i campi privati con il giocatore e l'eventuale carta giocata dall'avversario
	private Giocatore player;
	private Carta cartaGiocataAvversario;
	
	//metodo costruttore della classe "Robot" che assegna alla variabile player il giocatore
	public Robot(Giocatore player) 
	{
		this.player = player;
	}
	
	public Carta getCartaGiocata() 
	{
		return cartaGiocataAvversario;
	}
	
	public void setCartaGiocata(Carta cartaGiocata) 
	{
		this.cartaGiocataAvversario = cartaGiocata;
	}
	
	//metodo che ritorna la carta che giocherà il giocatore con la priorità più alta
	public Carta cartaGiocata(char turno) 
	{
		//l'ArrayList che conterrà le carte giocabili
		ArrayList<Carta> carteGiocabili = new ArrayList<>();
		for(Carta c : player.getMano()) 
		{
			if(turno == 'a') 
			{
				//il giocatore robot è in un turno di attacco quindi le carte giocabili saranno tutte quelle di attacco
				if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.MISTER) || c.equals(Carta.GOAL))
				{
					carteGiocabili.add(c);
				}
			}
			else 
			{
				//il giocatore robot è in un turno di difesa quindi le carte giocabili saranno tutte quelle di difesa
				if(c.getTipologia().equals(Tipologia.DIFESA) || c.equals(Carta.VAR) || c.equals(Carta.FUORIGIOCO))
				{
					carteGiocabili.add(c);
				}
			}
		}
		//se non fossero state trovate carte giocabili ritorna una carta di default, altrimenti ritorna la carta scelta con priorità maggiore
		if(carteGiocabili.size()>0)
		{
			return scegliCarta(carteGiocabili, turno);
		}
		else
		{
			return Carta.INDICATORE_GOAL;
		}
	}

	//metodo che permette la selezione della carta dai parametri passati con la lista di carte e se è un turno di attacco o di difesa
	public Carta scegliCarta(ArrayList<Carta> carte, char turno) 
	{
		if(turno == 'a') 
		{
			//è un turno di attacco quindi ritorna la carta con la più alta priorità in mano del giocatore
			return getMaxPriorityAtt(carte);
		}
		else 
		{
			//è un turno di difesa quindi ritorna la carta, se presente, che difende la carta giocata con la priorità giusta
			return cartaCorrettaDif(carte,cartaGiocataAvversario);
		}
	}
	
	//metodo che ricerca la carta con la più alta priorità in mano
	private Carta getMaxPriorityAtt(ArrayList<Carta> carteAtt) 
	{
		Carta max = carteAtt.get(0);
		int maxPriority = max.getPriority();
		for(int i = 1; i < carteAtt.size(); i++) 
		{
			Carta c = carteAtt.get(i);
			if(c.getPriority() > maxPriority) 
			{
				max = c;
				maxPriority = c.getPriority();
			}
		}
		//ritorna la carta con priorità più alta
		return max;
	}
	
	//metodo che ricerca la carta corretta da utilizzare in un turno di difesa
	private Carta cartaCorrettaDif(ArrayList<Carta> carteDif, Carta cartaGiocata) 
	{
		boolean ugualePriority = false;
		boolean presenzaRoccia = false;
		Carta carta = null;
		for(int i = 0; i < carteDif.size(); i++)
		{
			//cerco all'interno della mano del giocatore una carta difensiva con la stessa priorità della carta offensiva giocata
			if(cartaGiocata.getPriority() == carteDif.get(i).getPriority())
			{
				//ho trovato la carta con uguale priorità, setto la variabile booleana a "true" e la variabile con la carta difensiva
				ugualePriority = true;
				carta = carteDif.get(i);
			}
			//mi serve sapere se fra le carte di difesa c'è un "difensore roccia" solo se l'attaccante ha giocato la carta "attaccante"
			//il "difensore roccia" difende, oltre al tiro del "bomber vero", anche quello dell'attaccante
			if(cartaGiocata.getPriority() == 2 && carteDif.get(i).getPriority() == 3)
			{
				presenzaRoccia = true;
			}
		}		
		//se non avessi trovato carte difensive faccio ritornare la carta di default
		if(!ugualePriority) 
		{
		    carta = Carta.INDICATORE_GOAL;
		    //se fosse presente una carta "difensore roccia" per difendere una carta "attaccante" restituisco quella
		    if(presenzaRoccia) 
		    {
		        carta = Carta.DIFENSORE_ROCCIA;
		    }
		}
		//ritorna la carta difensiva giusta da utilizzare in questo turno di difesa
		return carta;
	}
	
	//metodo che restituisce un numero da 1 a 3 che permetterà di sciegliere la direzione di battuta del rigore (sinistra, centro, destra)
	public int scegliDirezione() 
	{
		Random r =  new Random();
		int randomNumber = r.nextInt(3);
		return randomNumber;
	}
}