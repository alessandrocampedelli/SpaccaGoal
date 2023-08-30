package classi;
import java.io.File;

import javafx.scene.image.Image;
public enum Carta {
	ATTACCANTE(Tipologia.ATTACCO, new Image(getUrl("attacco_attaccante.jpg"))),
	BOMBER_VERO(Tipologia.ATTACCO, new Image(getUrl("attacco_bomberVero.jpg"))),
	RIGORE(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG"))),
	ROVESCIATA_DELLANNO(Tipologia.ATTACCO,new Image(getUrl("attacco_rovesciataDellAnno.jpg"))),
	TIRO_DOMENICA(Tipologia.ATTACCO, new Image(getUrl("attacco_tiroDellaDomenica.jpg"))),
	CAMBIO_SCHEMA(Tipologia.BONUS_MALUS, new Image(getUrl("bonus_cambioSchema.JPG"))),
	DIFENSORE(Tipologia.DIFESA, new Image(getUrl("difesa_difensore.jpg"))),
	DIFENSORE_ROCCIA(Tipologia.DIFESA, new Image(getUrl("difesa_difensoreRoccia.jpg"))),
	PORTIERE(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG"))),
	AUTOGOAL(Tipologia.BONUS_MALUS, new Image(getUrl("malus_autogoal.jpg"))),
	FUORIGIOCO(Tipologia.SPECIALE, new Image(getUrl("speciale_fuorigioco.jpg"))),
	GOAL(Tipologia.SPECIALE, new Image(getUrl("speciale_goal.jpg"))),
	MISTER(Tipologia.SPECIALE, new Image(getUrl("speciale_mister.JPG"))),
	VAR(Tipologia.SPECIALE, new Image(getUrl("speciale_var.jpg"))),
	INDICATORE_GOAL(null,new Image(getUrl("indicatoreGoal.jpg")));
	private Tipologia tipo;
	private Image img;
	private Carta(Tipologia tipo, Image img) {
		this.tipo = tipo;
		this.img = img;
	}
	public Image getImmagine() {
		return img;
	}
	private static String getUrl(String nomeCarta) {
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "mazzo_di_carte";
		String absolutePath = currentDirectory + File.separator + relativePath + "\\"+nomeCarta;
		return absolutePath;
	}
}