package classi;
import java.util.ArrayList;
import java.util.Random;

public class Robot {
	private Giocatore player;
	public Robot(Giocatore player) {
		this.player = player;
	}
	public Giocatore getPlayer() {
		return player;
	}
	public void setPlayer(Giocatore player) {
		this.player = player;
	}
	//metodo che ritorna il nome della carta che giocherà il giocatore
	public String cartaGiocata(char turno) {
		ArrayList<String> carteGiocabili = new ArrayList<>();
		for(Carta c : player.getMano()) {
			if(turno == 'a') {
				if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.MISTER) || c.equals(Carta.GOAL)){
					carteGiocabili.add(c.name());
				}
			}else {
				if(c.getTipologia().equals(Tipologia.DIFESA) || c.equals(Carta.VAR) || c.equals(Carta.FUORIGIOCO)){
					carteGiocabili.add(c.name());
				}
			}
		}
		if(carteGiocabili.size()>0)
			return scegliCarta(carteGiocabili);
		else
			return "INDICATORE_GOAL";
	}
	private String scegliCarta(ArrayList<String> carte) {
		Random r = new Random();
		int randomNumber = r.nextInt(carte.size());
		return carte.get(randomNumber);
	}
	public int scegliDirezione() {
		Random r =  new Random();
		int randomNumber = r.nextInt(3);
		return randomNumber;
	}
}
