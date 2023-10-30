package classi;
import java.io.File;
import javafx.scene.image.Image;
public enum Carta {
	ATTACCANTE(Tipologia.ATTACCO, new Image(getUrl("attacco_attaccante.jpg"))),
	BOMBER_VERO(Tipologia.ATTACCO, new Image(getUrl("attacco_bomberVero.jpg")), "BOMBER VERO"),
	RIGORE(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG"))),
	RIGORE_DX(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.DESTRA),
	RIGORE_C(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.CENTRO),
	RIGORE_SX(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.SINISTRA),
	ROVESCIATA_DELLANNO(Tipologia.ATTACCO,new Image(getUrl("attacco_rovesciataDellAnno.jpg")),"ROVESCIATA DELL'ANNO"),
	TIRO_DOMENICA(Tipologia.ATTACCO, new Image(getUrl("attacco_tiroDellaDomenica.jpg")), "TIRO DELLA DOMENICA"),
	DIFENSORE(Tipologia.DIFESA, new Image(getUrl("difesa_difensore.jpg"))),
	DIFENSORE_ROCCIA(Tipologia.DIFESA, new Image(getUrl("difesa_difensoreRoccia.jpg")), "DIFENSORE ROCCIA"),
	PORTIERE(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG"))),
	PORTIERE_DX(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.DESTRA),
	PORTIERE_C(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.CENTRO),
	PORTIERE_SX(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.SINISTRA),
	FUORIGIOCO(Tipologia.SPECIALE, new Image(getUrl("speciale_fuorigioco.jpg"))),
	GOAL(Tipologia.SPECIALE, new Image(getUrl("speciale_goal.jpg"))),
	MISTER(Tipologia.SPECIALE, new Image(getUrl("speciale_mister.JPG"))),
	VAR(Tipologia.SPECIALE, new Image(getUrl("speciale_var.jpg"))),
	INDICATORE_GOAL(null,new Image(getUrl("indicatoreGoal.jpg")));
	private Tipologia tipo;
	private Image img;
	private Direzione d;
	private String stampa;
	private Carta(Tipologia tipo, Image img) {
		this.tipo = tipo;
		this.img = img;
	}
	private Carta(Tipologia tipo, Image img, Direzione d) {
		this.tipo = tipo;
		this.img = img;
		this.d = d;
	}
	private Carta(Tipologia tipo, Image img, String stampa) {
		this.tipo = tipo;
		this.img = img;
		this.stampa = stampa;
	}
	public Direzione getDirezione() {
		return d;
	}
	public Image getImmagine() {
		return img;
	}
	public Tipologia getTipologia() {
		return tipo;
	}
	public String getStampa() {
		return this.stampa;
	}
	public static String getUrl(String nomeCarta) {
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "mazzo_di_carte";
		String absolutePath = currentDirectory + File.separator + relativePath + "\\"+nomeCarta;
		return absolutePath;
	}
}