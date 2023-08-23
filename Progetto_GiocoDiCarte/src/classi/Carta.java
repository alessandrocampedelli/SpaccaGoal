package classi;
import javafx.scene.image.Image;
public enum Carta {
	ATTACCANTE(Tipologia.ATTACCO, new Image("/mazzo_di_carte/attacco_attaccante.jpg")),
	BOMBER_VERO(Tipologia.ATTACCO, new Image("/mazzo_di_carte/attacco_bomberVero.jpg")),
	RIGORE(Tipologia.ATTACCO, new Image("/mazzo_di_carte/attacco_rigore.JPG")),
	ROVESCIATA_DELLANNO(Tipologia.ATTACCO,new Image("/mazzo_di_carte/rovesciataDellAnno.jpg")),
	TIRO_DOMENICA(Tipologia.ATTACCO, new Image("/mazzo_di_carte/tiroDellaDomenica.jpg")),
	CAMBIO_SCHEMA(Tipologia.BONUS_MALUS, new Image("/mazzo_di_carte/bonus_cambioSchema.JPG")),
	DIFENSORE(Tipologia.DIFESA, new Image("/mazzo_di_carte/difesa_difensore.jpg")),
	DIFENSORE_ROCCIA(Tipologia.DIFESA, new Image("/mazzo_di_carte/difesa_difensoreRoccia.jpg")),
	PORTIERE(Tipologia.DIFESA, new Image("/mazzo_di_carte/difesa_portiere.JPG")),
	AUTOGOAL(Tipologia.BONUS_MALUS, new Image("/mazzo_di_carte/malus_autogoal.jpg")),
	FUORIGIOCO(Tipologia.SPECIALE, new Image("/mazzo_di_carte/speciale_fuorigioco.jpg")),
	GOAL(Tipologia.SPECIALE, new Image("/mazzo_di_carte/speciale_goal.jpg")),
	MISTER(Tipologia.SPECIALE, new Image("/mazzo_di_carte/speciale_mister.JPG")),
	VAR(Tipologia.SPECIALE, new Image("/mazzo_di_carte/speciale_var.JPG")),
	INDICATORE_GOAL(null,new Image("/mazzo_di_carte/indicatoreGoal.jpg"));
	private Tipologia tipo;
	private Image img;
	private Carta(Tipologia tipo, Image img) {
		this.tipo = tipo;
		this.img = img;
	}
	public Image getImmagine() {
		return img;
	}
}