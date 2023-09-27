package classi;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
public class Leaderboard {
	private ArrayList<Giocatore> players;
	private String path;
	
	public Leaderboard() {
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/giocatori.txt";
		this.path = currentDirectory + File.separator + relativePath;
		
		players = new ArrayList<>();
		try {
			caricaPlayers(this.path);
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//DUBBIO - ELIMINAZIONE METODO CARICAMENTO GIOCATORE -- INUTILE -- (utile per ricaricare leaderboard in grafica)
	
	
	//metodo che carica i giocatori gia presenti salvati sul file di testo nell'arrayList
	private void caricaPlayers(String path) throws FileNotFoundException{
		Scanner scan = new Scanner(new File(path));
		while(scan.hasNextLine()) 
		{
			String riga = scan.nextLine();
			String[] infoPlayer = riga.split(",");
			Giocatore g;
			if(infoPlayer[3].equals("p"))
				g = new Giocatore(infoPlayer[0],false);
			else
				g = new Giocatore(infoPlayer[0],true);
			g.setVittoriePartite(Integer.parseInt(infoPlayer[1]));
			g.setVittorieTornei(Integer.parseInt(infoPlayer[2]));
			System.out.println("leggi " + g.getAlias()+ " " + g.getNVittoriePartite());
			addPlayers(g);
		}
		scan.close();
	}
	
	//metodo che salva i giocatori (alias, numero vittorie torneo, numero vittorie partite, robot) su file
	public void salvaPlayers() throws FileNotFoundException{
		PrintWriter fw = new PrintWriter(path);
		for(Giocatore g: players) {
			System.out.println("salva " + g.getAlias()+ " " + g.getNVittoriePartite());
			String riga = g.getAlias()+","+g.getNVittoriePartite()+","+g.getNVittorieTornei();
			if(g.getRobot())
				riga += ",r";
			else
				riga += ",p";
			fw.println(riga);
		}
		fw.close();
	}
	public void addPlayers(Giocatore g) {
		players.add(g);
	}
	public void updatePlayers(Giocatore g) {
		players.remove(indexPlayer(g.getAlias()));
		players.add(g);
	}
	private int indexPlayer(String alias) {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getAlias().equals(alias))
				return i;
		}
		return -1;
	}
	public ArrayList<Giocatore> getPlayers(){
		return this.players;
	}
	//controllo se è gia stato creato un giocatore con quell'alias
	public Giocatore giocatoreGiaCreato(String alias) {
		for(Giocatore g : players) {
			if(g.getAlias().equals(alias))
				return g;
		}
		return null;
	}
}
