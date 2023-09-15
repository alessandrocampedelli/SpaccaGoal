package classi;

import java.util.ArrayList;
import java.io.IOException;
public abstract class Gara 
{
	protected Giocatore[] giocatori;
	protected Codice codice;
	protected Mazzo carte;
	protected final int N_CARTE_INIZIO = 5;

	public Gara(ArrayList<Giocatore> giocatori, String codice)
	{
		this.giocatori = giocatori.toArray(new Giocatore[giocatori.size()]);
		this.codice = new Codice(codice);
		this.carte = new Mazzo();
	}

	public void distribuzioneCarte()
	{
		//pulisco le mani dei giocatori da eventuali partite precedenti
		pulisciMani();
		carte.mischia();
		//distribuzione delle carte
		for(int j = 0; j < this.giocatori.length; j++) {
			for(int i = 0; i < N_CARTE_INIZIO; i++) {
				giocatori[j].getMano().add(carte.pesca());
				//il primo giocatore deve pescare una carta in più
				if(i == 4 && j == 0)
					giocatori[j].getMano().add(carte.pesca());
			}
		}
	}

	private void pulisciMani() {
		for(Giocatore g : giocatori)
			g.getMano().clear();
	}

	public Codice getCodiceGara()
	{
		return this.codice;
	}

	public Giocatore[] getGiocatori()
	{
		return this.giocatori;
	}

	public Mazzo getMazzo() 
	{
		return carte;
	}

	public void setMazzo(Mazzo m) {
		this.carte = m;
	}
	public void gioca(Giocatore att, Giocatore dif, Carta cartaAtt, Carta cartaDif) 
	{
		dif.getMano().add(this.carte.pesca());
		//attaccante
		if(cartaAtt.equals(Carta.ATTACCANTE)) 
		{
			if(!(cartaDif.equals(Carta.DIFENSORE) || cartaDif.equals(Carta.DIFENSORE_ROCCIA)))
			{
				att.aggiungiGoal();
			}else 
			{
				dif.getMano().add(this.carte.pesca());
			}
		}else {
			//bomber vero
			if(cartaAtt.equals(Carta.BOMBER_VERO)) 
			{
				if(!cartaDif.equals(Carta.DIFENSORE_ROCCIA)) 
				{
					att.aggiungiGoal();
				}else 
				{
					dif.getMano().add(this.carte.pesca());
				}
			}
			else 
			{
				//rovesciata dell'anno e tiro della domenica
				if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA))
				{
					att.aggiungiGoal();
				}
				else 
				{
					//goal
					if(cartaAtt.equals(Carta.GOAL)) 
					{
						if(!(cartaDif.equals(Carta.VAR) || cartaDif.equals(Carta.FUORIGIOCO))) 
						{
							att.aggiungiGoal();
						}else 
						{
							dif.getMano().add(this.carte.pesca());
						}
					}
					else 
					{
						if(cartaAtt.equals(Carta.MISTER)) 
						{
							att.getMano().add(this.carte.pesca());
							att.getMano().add(this.carte.pesca());
						}
					}
				}
			}
		}
	}
	public boolean checkGiocaTurno(Carta cartaGiocata, ArrayList<Carta> manoAvversario) 
	{
		boolean giocaTurno = true;
		if(cartaGiocata.equals(Carta.ATTACCANTE)) 
		{
			giocaTurno = manoAvversario.contains(Carta.DIFENSORE) || manoAvversario.contains(Carta.DIFENSORE_ROCCIA);
		}
		else if(cartaGiocata.equals(Carta.BOMBER_VERO)) 
		{
			giocaTurno = manoAvversario.contains(Carta.DIFENSORE_ROCCIA);
		}
		else if(cartaGiocata.equals(Carta.RIGORE)) 
		{
			giocaTurno = manoAvversario.contains(Carta.PORTIERE);
		}
		else if(cartaGiocata.equals(Carta.ROVESCIATA_DELLANNO) || cartaGiocata.equals(Carta.TIRO_DOMENICA)) 
		{
			giocaTurno = false;
		}
		else if(cartaGiocata.equals(Carta.GOAL)) 
		{
			giocaTurno = manoAvversario.contains(Carta.VAR) || manoAvversario.contains(Carta.FUORIGIOCO);
		}
		else if(cartaGiocata.equals(Carta.MISTER)) 
		{
			giocaTurno = false;
		}
		return giocaTurno;
	}
	public boolean checkGiocaTurno(Giocatore att) {
		for(Carta c : att.getMano()) {
			if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.GOAL) || c.equals(Carta.MISTER))
				return true;
		}
		return false;
	}
}