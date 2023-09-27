package classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.beans.property.SimpleIntegerProperty;
public class Giocatore 
{
	private String alias;
	private Integer nPartiteVinte;
	private Integer nTorneiVinti;
	private boolean robot;
	private ArrayList<Carta> mano;
	private int punteggio;

	private final SimpleIntegerProperty numPartiteVinte;
	private final SimpleIntegerProperty numTorneiVinti;
	
	public Giocatore(String alias, boolean robot) 
	{
		this.alias = alias;
		this.robot = robot;
		this.punteggio = 0;
		this.mano = new ArrayList<>();
		caricaVittoriePartiteTorneo();
		
		numPartiteVinte = new SimpleIntegerProperty(nPartiteVinte);
		numTorneiVinti = new SimpleIntegerProperty(nTorneiVinti);
	}

	public final SimpleIntegerProperty getNumPartiteVinte() {
		   return numPartiteVinte;
	}
	
	public final SimpleIntegerProperty getNumTorneiVinti() {
		   return numTorneiVinti;
		}
	
	public String getAlias() 
	{
		return alias;
	}
	
	public int getNVittoriePartite() 
	{
		return nPartiteVinte;
	}
	
	public int getNVittorieTornei() 
	{
		return nTorneiVinti;
	}
	
	public boolean getRobot()
	{
		return robot;
	}
	
	public int getPunteggio() {
		return this.punteggio;
	}
	
	public ArrayList<Carta> getMano(){
		return mano;
	}
	
	public void setMano(ArrayList<Carta> mano) {
		this.mano = mano;
	}
	
	public void setPunteggio(int p) {
		this.punteggio = p;
	}
	
	public void setVittoriePartite(int v) {
		this.nPartiteVinte = v;
	}

	public void setVittorieTornei(int v) {
		this.nTorneiVinti = v;
	}
	
	//metodo che restituisce un vettore di stringhe contenenti i nomi delle carte della mano
	public String[] getManoNomi(){
		String[] nomiCarte = new String[mano.size()];
		for(int i = 0; i < nomiCarte.length; i++) {
			nomiCarte[i] = mano.get(i).name();
		}
		return nomiCarte;
	}
	
	public void aggiungiVittoriaPartita() 
	{
		System.out.println("AA" + this.nPartiteVinte);
		this.nPartiteVinte+=1;
	}
	
	public void aggiungiGoal() {
		this.punteggio+=1;
	}
	
	public void rimuoviGoal() {
		this.punteggio-=1;
	}

	private void caricaVittoriePartiteTorneo()
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/giocatori.txt";
		String path = currentDirectory + File.separator + relativePath;
		try 
		{
			Scanner scan = new Scanner(new File(path));
			while(scan.hasNextLine()) 
			{
				String riga = scan.nextLine();
				String[] infoPlayer = riga.split(",");
				if(infoPlayer[0].equals(alias)) 
				{
					setVittoriePartite(Integer.parseInt(infoPlayer[1]));
					setVittorieTornei(Integer.parseInt(infoPlayer[2]));
					break;
				}
			}
			scan.close();
			System.out.println(alias + " " + nPartiteVinte);
		}
		catch(FileNotFoundException e) 
		{
			System.out.println(e.getMessage());
		}
	}
}