package classi;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Mazzo 
{
	private LinkedList<Carta> carte;
	private Carta[] _carte = new Carta[] {Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,
			Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,
			Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.RIGORE,Carta.RIGORE,Carta.RIGORE,Carta.RIGORE
			,Carta.ROVESCIATA_DELLANNO,Carta.TIRO_DOMENICA,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE
			,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE
			,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.PORTIERE,Carta.PORTIERE
			,Carta.PORTIERE,Carta.PORTIERE,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.VAR,Carta.VAR,Carta.VAR
			,Carta.FUORIGIOCO,Carta.FUORIGIOCO,Carta.FUORIGIOCO,Carta.MISTER,Carta.MISTER};
	public Mazzo()
	{
		carte = new LinkedList<Carta>(Arrays.asList(_carte));
	}
	public void aggiungiBonusMalus() {
		carte.add(Carta.CAMBIO_SCHEMA);
		carte.add(Carta.AUTOGOAL);
	}
	public void mischia() {
		Collections.shuffle(carte);
	}

	public Carta pesca() {
		return carte.removeFirst();
	}
	public void scarta(Carta c) {
		carte.addLast(c);
	}
}
