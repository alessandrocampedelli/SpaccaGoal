package classi;
import java.util.ArrayList;
import java.util.Random;

public class Robot 
{
	private Giocatore player;
	private Carta cartaGiocataAvversario;
	
	public Carta getCartaGiocata() {
		return cartaGiocataAvversario;
	}
	public void setCartaGiocata(Carta cartaGiocata) {
		this.cartaGiocataAvversario = cartaGiocata;
	}
	public Carta scegliCarta(ArrayList<Carta> carte, char turno) {
		if(turno == 'a') {
			return getMaxPriorityAtt(carte);
		}else {
			return cartaCorrettaDif(carte,getCartaGiocata().getPriority());
		}
	}
	private Carta cartaCorrettaDif(ArrayList<Carta> carteDif, int priority) {
		//se le carte di difesa sono meno della metà delle carte che ha in mano fa passa turno
		if(this.player.getMano().size() > 2*carteDif.size()) {
			return Carta.INDICATORE_GOAL;
		}else {
			//altrimenti gioco la carta di difesa con priorità minore
			return getMinPriorityDif(carteDif);
		}
	}
	private Carta getMaxPriorityAtt(ArrayList<Carta> carteAtt) {
		Carta max = carteAtt.get(0);
		int maxPriority = max.getPriority();
		for(int i = 1; i < carteAtt.size(); i++) {
			Carta c = carteAtt.get(i);
			if(c.getPriority() > maxPriority) {
				max = c;
				maxPriority = c.getPriority();
			}
		}
		return max;
	}
	private Carta getMinPriorityDif(ArrayList<Carta> carteDif) {
		Carta min = carteDif.get(0);
		int minPriority = min.getPriority();
		for(int i = 1; i < carteDif.size(); i++) {
			Carta c = carteDif.get(i);
			if(c.getPriority() < minPriority) {
				min = c;
				minPriority = c.getPriority();
			}
		}
		return min;
	}
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
	/*
	protected Carta scegliCarta(ArrayList<Carta> carte, char turno) 
	{
		Random r = new Random();
		int randomNumber = r.nextInt(carte.size());
		return carte.get(randomNumber);
	}
	*/
	public int scegliDirezione() 
	{
		Random r =  new Random();
		int randomNumber = r.nextInt(3);
		return randomNumber;
	}
}