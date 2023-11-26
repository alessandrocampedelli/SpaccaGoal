package classi;
import java.util.ArrayList;
import java.util.Random;

public class Robot 
{
	protected Giocatore player;
	public Robot(Giocatore player) 
	{
		this.player = player;
	}
	
	public Giocatore getPlayer() 
	{
		return player;
	}
	
	public void setPlayer(Giocatore player) 
	{
		this.player = player;
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
	
	protected Carta scegliCarta(ArrayList<Carta> carte, char turno) 
	{
		Random r = new Random();
		int randomNumber = r.nextInt(carte.size());
		return carte.get(randomNumber);
	}
	
	public int scegliDirezione() 
	{
		Random r =  new Random();
		int randomNumber = r.nextInt(3);
		return randomNumber;
	}
}