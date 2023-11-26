package classi;
import java.io.File;
import javafx.scene.image.Image;
public enum Carta 
{
	ATTACCANTE(Tipologia.ATTACCO, new Image(getUrl("attacco_attaccante.jpg")),1),
	BOMBER_VERO(Tipologia.ATTACCO, new Image(getUrl("attacco_bomberVero.jpg")), "BOMBER VERO",2),
	RIGORE(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),3),
	RIGORE_DX(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.DESTRA,3),
	RIGORE_C(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.CENTRO,3),
	RIGORE_SX(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.SINISTRA,3),
	ROVESCIATA_DELLANNO(Tipologia.ATTACCO,new Image(getUrl("attacco_rovesciataDellAnno.jpg")),"ROVESCIATA DELL'ANNO",5),
	TIRO_DOMENICA(Tipologia.ATTACCO, new Image(getUrl("attacco_tiroDellaDomenica.jpg")), "TIRO DELLA DOMENICA",5),
	DIFENSORE(Tipologia.DIFESA, new Image(getUrl("difesa_difensore.jpg")),1),
	DIFENSORE_ROCCIA(Tipologia.DIFESA, new Image(getUrl("difesa_difensoreRoccia.jpg")), "DIFENSORE ROCCIA",2),
	PORTIERE(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")),3),
	PORTIERE_DX(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.DESTRA,3),
	PORTIERE_C(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.CENTRO,3),
	PORTIERE_SX(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.SINISTRA,3),
	FUORIGIOCO(Tipologia.SPECIALE, new Image(getUrl("speciale_fuorigioco.jpg")),4),
	GOAL(Tipologia.SPECIALE, new Image(getUrl("speciale_goal.jpg")),4),
	MISTER(Tipologia.SPECIALE, new Image(getUrl("speciale_mister.JPG")),4),
	VAR(Tipologia.SPECIALE, new Image(getUrl("speciale_var.jpg")),4),
	INDICATORE_GOAL(null,new Image(getUrl("indicatoreGoal.jpg")),0);
	
	private Tipologia tipo;
	private Image img;
	private Direzione d;
	private String stampa;
	private int priority;
	
	private Carta(Tipologia tipo, Image img, int priority) 
	{
		this.tipo = tipo;
		this.img = img;
		this.priority = priority;
	}
	
	private Carta(Tipologia tipo, Image img, Direzione d, int priority) 
	{
		this.tipo = tipo;
		this.img = img;
		this.d = d;
		this.priority = priority;
	}
	
	private Carta(Tipologia tipo, Image img, String stampa,int priority) 
	{
		this.tipo = tipo;
		this.img = img;
		this.stampa = stampa;
		this.priority = priority;
	}
	public Direzione getDirezione() 
	{
		return d;
	}
	
	public int getPriority() {
		return priority;
	}

	public Image getImmagine() 
	{
		return img;
	}
	
	public Tipologia getTipologia() 
	{
		return tipo;
	}
	
	public String getStampa() 
	{
		return this.stampa;
	}
	
	public static String getUrl(String nomeCarta) 
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "mazzo_di_carte";
		String absolutePath = currentDirectory + File.separator + relativePath + "\\"+nomeCarta;
		return absolutePath;
	}
}