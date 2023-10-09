package classi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		carte.mischia();
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

	public void setMazzo(Mazzo m) 
	{
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
			}
			else 
			{
				dif.getMano().add(this.carte.pesca());
			}
		}
		else 
		{
			//bomber vero
			if(cartaAtt.equals(Carta.BOMBER_VERO)) 
			{
				if(!cartaDif.equals(Carta.DIFENSORE_ROCCIA)) 
				{
					att.aggiungiGoal();
				}
				else 
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
						}
						else 
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
						else 
						{
							//caso del rigore
							if(!cartaDif.equals(Carta.PORTIERE)) 
							{
								att.aggiungiGoal();
							}
						}
					}
				}
			}
		}
	}
	
	public boolean checkGiocaTurno(Carta cartaGiocata, ArrayList<Carta> manoAvversario) 
	{
		if(cartaGiocata.equals(Carta.ROVESCIATA_DELLANNO) || cartaGiocata.equals(Carta.TIRO_DOMENICA) || cartaGiocata.equals(Carta.MISTER)) 
		{
			return false;
		}
		else 
		{
			for(Carta c : manoAvversario) 
			{
				if(c.getTipologia().equals(Tipologia.DIFESA))
					return true;
			}
			return false;
		}
	}
	
	public boolean checkGiocaTurno(Giocatore att) 
	{
		for(Carta c : att.getMano()) 
		{
			if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.GOAL) || c.equals(Carta.MISTER))
				return true;
		}
		return false;
	}
	public String mostraRisultati() {
		String output = "CLASSIFICA FINALE:\n";
		for(Giocatore g : giocatori) {
			output += g.getAlias()+": "+g.getPunteggio()+"\n";
		}
		return output;
	}
}