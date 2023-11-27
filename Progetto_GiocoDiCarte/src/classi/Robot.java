package classi;
import java.util.ArrayList;
import java.util.Random;

public class Robot 
{
	private Giocatore player;
	private Carta cartaGiocataAvversario;
	
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
	
	public Giocatore getPlayer() 
	{
		return player;
	}
	
	public void setPlayer(Giocatore player) 
	{
		this.player = player;
	}
	
	public Carta scegliCarta(ArrayList<Carta> carte, char turno) 
	{
		if(turno == 'a') 
		{
			return getMaxPriorityAtt(carte);
		}
		else 
		{
			return cartaCorrettaDif(carte,cartaGiocataAvversario);
		}
	}
	
	private Carta cartaCorrettaDif(ArrayList<Carta> carteDif, Carta cartaGiocata) 
	{
		boolean ugualePriority = false;
		Carta carta = null;
		for(int i = 0; i < carteDif.size(); i++)
		{
			if(cartaGiocata.getPriority() == carteDif.get(i).getPriority())
			{
				ugualePriority = true;
				carta = carteDif.get(i);
			}
		}
		if(ugualePriority == false)
		{
			carta = Carta.INDICATORE_GOAL;
		}
		return carta;
	}
	
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
		return max;
	}
	
	//metodo che ritorna la carta che giocherà il giocatore
	public Carta cartaGiocata(char turno) 
	{
		ArrayList<Carta> carteGiocabili = new ArrayList<>();
		for(Carta c : player.getMano()) 
		{
			if(turno == 'a') 
			{
				if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.MISTER) || c.equals(Carta.GOAL))
				{
					carteGiocabili.add(c);
				}
			}
			else 
			{
				if(c.getTipologia().equals(Tipologia.DIFESA) || c.equals(Carta.VAR) || c.equals(Carta.FUORIGIOCO))
				{
					carteGiocabili.add(c);
				}
			}
		}
		if(carteGiocabili.size()>0)
		{
			return scegliCarta(carteGiocabili, turno);
		}
		else
		{
			return Carta.INDICATORE_GOAL;
		}
	}

	public int scegliDirezione() 
	{
		Random r =  new Random();
		int randomNumber = r.nextInt(3);
		return randomNumber;
	}
}