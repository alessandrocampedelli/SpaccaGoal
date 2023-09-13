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
	public void gioca(Giocatore att, Giocatore dif, Carta cartaAtt, Carta cartaDif) {
		//attaccante
		if(cartaAtt.equals(Carta.ATTACCANTE)) {
			if(!(cartaDif.equals(Carta.DIFENSORE) || cartaDif.equals(Carta.DIFENSORE_ROCCIA))){
				att.aggiungiGoal();
			}
		}else {
			//bomber vero
			if(cartaAtt.equals(Carta.BOMBER_VERO)) {
				if(!cartaDif.equals(Carta.DIFENSORE_ROCCIA)) {
					att.aggiungiGoal();
				}
			}else {
				//rovesciata dell'anno e tiro della domenica
				if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA)) {
					att.aggiungiGoal();
				}else {
					//goal
					if(cartaAtt.equals(Carta.GOAL)) {
						if((cartaDif.equals(Carta.VAR) || cartaDif.equals(Carta.FUORIGIOCO))) {
							att.aggiungiGoal();
						}
					}
				}
			}
		}
	}
}