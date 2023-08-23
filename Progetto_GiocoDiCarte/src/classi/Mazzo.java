package classi;
import java.util.ArrayList;

public class Mazzo 
{
	private Carta[] carte;
	
	public Mazzo()
	{
		carte = new Carta[] {Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,
				Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,Carta.ATTACCANTE,
				Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.BOMBER_VERO,Carta.RIGORE,Carta.RIGORE,Carta.RIGORE,Carta.RIGORE
				,Carta.ROVESCIATA_DELLANNO,Carta.TIRO_DOMENICA,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE
				,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE,Carta.DIFENSORE
				,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.DIFENSORE_ROCCIA,Carta.PORTIERE,Carta.PORTIERE
				,Carta.PORTIERE,Carta.PORTIERE,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.GOAL,Carta.VAR,Carta.VAR,Carta.VAR
				,Carta.FUORIGIOCO,Carta.FUORIGIOCO,Carta.FUORIGIOCO,Carta.MISTER,Carta.MISTER,null,null};
	}
	public void aggiungiBonusMalus() {
		carte[carte.length-2] = Carta.CAMBIO_SCHEMA;
		carte[carte.length-1] = Carta.AUTOGOAL;
	}
	public void mischia() {
		for(int i = 0; i<carte.length; i++) {
            int posizioneCasuale = (int)Math.floor(Math.random() * i); 
            // scambia a[k] con a[posizioneCasuale]
            Carta tmp = carte[i];
            carte[i] = carte[posizioneCasuale];
            carte[posizioneCasuale] = tmp;
         }
	}
	//metodo di prova
	public Carta getCarta() {
		return carte[0];
	}
}
