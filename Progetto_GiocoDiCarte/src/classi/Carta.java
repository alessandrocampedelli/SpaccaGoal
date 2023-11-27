package classi;
import java.io.File;
import javafx.scene.image.Image;
public enum Carta 
{
	ATTACCANTE(Tipologia.ATTACCO, new Image(getUrl("attacco_attaccante.jpg")),2),
	BOMBER_VERO(Tipologia.ATTACCO, new Image(getUrl("attacco_bomberVero.jpg")), "BOMBER VERO",3),
	RIGORE(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),5),
	RIGORE_DX(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.DESTRA,5),
	RIGORE_C(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.CENTRO,5),
	RIGORE_SX(Tipologia.ATTACCO, new Image(getUrl("attacco_rigore.JPG")),Direzione.SINISTRA,5),
	ROVESCIATA_DELLANNO(Tipologia.ATTACCO,new Image(getUrl("attacco_rovesciataDellAnno.jpg")),"ROVESCIATA DELL'ANNO",6),
	TIRO_DOMENICA(Tipologia.ATTACCO, new Image(getUrl("attacco_tiroDellaDomenica.jpg")), "TIRO DELLA DOMENICA",6),
	DIFENSORE(Tipologia.DIFESA, new Image(getUrl("difesa_difensore.jpg")),2),
	DIFENSORE_ROCCIA(Tipologia.DIFESA, new Image(getUrl("difesa_difensoreRoccia.jpg")), "DIFENSORE ROCCIA",3),
	PORTIERE(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")),5),
	PORTIERE_DX(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.DESTRA,5),
	PORTIERE_C(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.CENTRO,5),
	PORTIERE_SX(Tipologia.DIFESA, new Image(getUrl("difesa_portiere.JPG")), Direzione.SINISTRA,5),
	FUORIGIOCO(Tipologia.SPECIALE, new Image(getUrl("speciale_fuorigioco.jpg")),4),
	GOAL(Tipologia.SPECIALE, new Image(getUrl("speciale_goal.jpg")),4),
	MISTER(Tipologia.SPECIALE, new Image(getUrl("speciale_mister.JPG")),1),
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
	
	public int getPriority() 
	{
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