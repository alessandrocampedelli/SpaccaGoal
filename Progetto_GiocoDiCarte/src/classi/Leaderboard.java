package classi;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Leaderboard 
{
	private ArrayList<Giocatore> players;
	private String path;

	public Leaderboard() 
	{	
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/giocatori.txt";
		this.path = currentDirectory + File.separator + relativePath;

		this.players = new ArrayList<>();
		try 
		{
			caricaPlayers(this.path);
		}
		catch(FileNotFoundException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	public Giocatore getPlayers(String alias) {
		for(Giocatore g : this.getPlayers()) 
		{
			if(g.getAlias().equals(alias)) {
				return g;
			}
		}
		return null;
	}
	//metodo che carica i giocatori gia presenti salvati sul file di testo nell'arrayList
	private void caricaPlayers(String path) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(path));
		while(scan.hasNextLine()) 
		{
			String riga = scan.nextLine();
			String[] infoPlayer = riga.split(",");
			Giocatore g;
			if(infoPlayer[3].equals("p"))
				g = new Giocatore(infoPlayer[0],false,infoPlayer[4]);
			else
				g = new Giocatore(infoPlayer[0],true,infoPlayer[4]);
			g.setVittoriePartite(Integer.parseInt(infoPlayer[1]));
			g.setVittorieTornei(Integer.parseInt(infoPlayer[2]));
			addPlayers(g);
		}
		scan.close();
	}

	//metodo che salva i giocatori (alias, numero vittorie torneo, numero vittorie partite, robot) su file
	public void salvaPlayers() throws FileNotFoundException
	{
		PrintWriter fw = new PrintWriter(path);
		for(Giocatore g: players) {
			String riga = g.getAlias()+","+g.getNPartiteVinte()+","+g.getNTorneiVinti();
			if(g.isRobot())
				riga += ",r";
			else
				riga += ",p";
			riga +=","+g.getEmail();
			fw.println(riga);
		}
		fw.close();
	}

	public void addPlayers(Giocatore g) 
	{
		players.add(g);
	}

	//metodo che mi ritorna l'indice del giocatore dato l'alias
	public int indexPlayer(String alias) 
	{
		for(int i = 0; i < players.size(); i++) 
		{
			if(players.get(i).getAlias().equals(alias))
				return i;
		}
		return -1;
	}

	public ArrayList<Giocatore> getPlayers()
	{
		return this.players;
	}

	//controllo se e' gia stato creato un giocatore con quell'alias
	public Giocatore giocatoreGiaCreato(String alias) 
	{
		for(Giocatore g : players) 
		{
			if(g.getAlias().equals(alias))
				return g;
		}
		return null;
	}
	public String[][] toMatrix(){
		String[][] matrice = new String[players.size()+1][4];
		
		matrice[0][0] = "ALIAS";
		matrice[0][1] = "PARTITE VINTE";
		matrice[0][2] = "TORNEI VINTI";
		matrice[0][3] = "GIOCATORE ROBOT";
		int r = 1;
		for(Giocatore g : this.players) {
			matrice[r][0] = g.getAlias();
			matrice[r][1] = Integer.toString(g.getNPartiteVinte());
			matrice[r][2] = Integer.toString(g.getNTorneiVinti());
			matrice[r][3] = (g.isRobot() ? "SI" : "NO");
			r+=1;
		}
		return matrice;
	}
}