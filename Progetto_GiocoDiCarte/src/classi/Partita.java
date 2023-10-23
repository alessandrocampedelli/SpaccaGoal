package classi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;

public class Partita extends Gara{
	protected final int N_CARTE_INIZIO = 5;
	protected String turno;
	protected int posizioneGiocatoreAttaccante;
	protected int posizioneGiocatoreDifensore;
	protected String nomeCarta;
	protected int cartePescate;

	public Partita(ArrayList<Giocatore> giocatori, String codice)
	{
		super(giocatori,codice);
	}
	public Partita() {}
	public void distribuzioneCarte()
	{
		//pulisco le mani dei giocatori da eventuali partite precedenti
		pulisciMani();
		carte.mischia();
		//distribuzione delle carte
		for(int j = 0; j < this.giocatori.length; j++) {
			for(int i = 0; i < N_CARTE_INIZIO; i++) {
				giocatori[j].getMano().add(carte.pesca());
				//il primo giocatore deve pescare una carta in più
				if(i == 4 && j == 0)
					giocatori[j].getMano().add(carte.pesca());
			}
		}
		carte.mischia();
	}
	
	public String getTurno()
	{
		return this.turno;
	}
	
	public int getPosAttaccante()
	{
		return this.posizioneGiocatoreAttaccante;
	}
	
	public int getPosDifensore()
	{
		return this.posizioneGiocatoreDifensore;
	}
	
	public String getNomeCarta()
	{
		return this.nomeCarta;
	}

	private void pulisciMani() 
	{
		for(Giocatore g : giocatori)
			g.getMano().clear();
	}
	
	public int getCartePescate() 
	{
		return this.cartePescate;
	}
	
	public void setCartePescate(int cartePescate) 
	{
		this.cartePescate = cartePescate;
	}
	public void gioca(Giocatore att, Giocatore dif, Carta cartaAtt, Carta cartaDif) 
	{
		dif.getMano().add(this.carte.pesca());
		cartePescate = 1;
		//attaccante
		if(cartaAtt.equals(Carta.ATTACCANTE)) 
		{
			if(!(cartaDif.equals(Carta.DIFENSORE) || cartaDif.equals(Carta.DIFENSORE_ROCCIA)))
			{
				att.aggiungiGoal();
			}
			else 
			{
				dif.getMano().add(this.carte.pesca());
				cartePescate++;
			}
		}
		else 
		{
			//bomber vero
			if(cartaAtt.equals(Carta.BOMBER_VERO)) 
			{
				if(!cartaDif.equals(Carta.DIFENSORE_ROCCIA)) 
				{
					att.aggiungiGoal();
				}
				else 
				{
					dif.getMano().add(this.carte.pesca());
					cartePescate++;
				}
			}
			else 
			{
				//rovesciata dell'anno e tiro della domenica
				if(cartaAtt.equals(Carta.ROVESCIATA_DELLANNO) || cartaAtt.equals(Carta.TIRO_DOMENICA))
				{
					att.aggiungiGoal();
				}
				else 
				{
					//goal
					if(cartaAtt.equals(Carta.GOAL)) 
					{
						if(!(cartaDif.equals(Carta.VAR) || cartaDif.equals(Carta.FUORIGIOCO))) 
						{
							att.aggiungiGoal();
						}
						else 
						{
							dif.getMano().add(this.carte.pesca());
							cartePescate++;
						}
					}
					else 
					{
						if(cartaAtt.equals(Carta.MISTER)) 
						{
							att.getMano().add(this.carte.pesca());
							att.getMano().add(this.carte.pesca());
							cartePescate = 2;
						}
						else 
						{
							//caso del rigore
							if(!cartaDif.equals(Carta.PORTIERE)) 
							{
								att.aggiungiGoal();
							}
						}
					}
				}
			}
		}
	}
	
	public boolean checkGiocaTurno(Carta cartaGiocata, ArrayList<Carta> manoAvversario) 
	{
		if(cartaGiocata.equals(Carta.ROVESCIATA_DELLANNO) || cartaGiocata.equals(Carta.TIRO_DOMENICA) || cartaGiocata.equals(Carta.MISTER)) 
		{
			return false;
		}
		else 
		{
			for(Carta c : manoAvversario) 
			{
				if(c.getTipologia().equals(Tipologia.DIFESA))
					return true;
			}
			return false;
		}
	}
	
	public boolean checkGiocaTurno(Giocatore att) 
	{
		for(Carta c : att.getMano()) 
		{
			if(c.getTipologia().equals(Tipologia.ATTACCO) || c.equals(Carta.GOAL) || c.equals(Carta.MISTER))
				return true;
		}
		return false;
	}
	
	public boolean finePartita(int iPosAtt) 
	{
		boolean fine = false;
		if(giocatori[iPosAtt].getPunteggio() == 5)
			fine = true;
		return fine;
	}
	
	public String mostraRisultati() 
	{
		String output = "CLASSIFICA FINALE:\n";
		for(Giocatore g : giocatori) {
			output += g.getAlias()+": "+g.getPunteggio()+"\n";
		}
		return output;
	}
	
	public void leggiTurno() throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "";
		if(codice.charAt(0) == 'p')
			relativePath = "src/partite/"+codice+"/turno.txt";
		else
			relativePath = "src/tornei/"+codice.substring(0,codice.length() - 1)+"/"+codice+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		File f = new File(absolutePath);
		Scanner scan = new Scanner(f);
		turno = scan.nextLine();
		posizioneGiocatoreAttaccante = scan.nextInt();
		posizioneGiocatoreDifensore = scan.nextInt();
		cartePescate = scan.nextInt();
		//devo dire che se va nel form rigore deve leggere anche la carta giocata
		if(turno.equals("d"))
		{
			nomeCarta = scan.next();
		}
		scan.close();
	}

	
	public void salvaTurno(String partitaTorneo) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath = "src/"+partitaTorneo+"/"+this.codice+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;
		PrintWriter pw = new PrintWriter(absolutePath);
		
		pw.println("a");
		pw.println(0);
		pw.println(1);
		pw.println(1);
		pw.close();
	}
	public void salvaTurnoGara(String partiteTornei, Carta cartaGiocata, Carta cartaAtt) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath; 
		if(this.getCodiceGara().charAt(0) == 'p')
			relativePath = "src/partite/"+this.getCodiceGara()+"/turno.txt";
		else
			relativePath = "src/tornei/"+this.getCodiceGara().substring(0, this.getCodiceGara().length() - 1)+"/"+this.getCodiceGara()+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		if(turno.equals("a")) 
		{
			if(cartaGiocata.equals(Carta.RIGORE)) 
			{
				fw.println("a");
			}
			else 
			{
				fw.println("d");
			}
			fw.println(posizioneGiocatoreAttaccante);
			fw.println(posizioneGiocatoreDifensore);
			fw.println(cartePescate);
			fw.println(cartaGiocata.name());
		}
		else 
		{
			if(!(cartaGiocata == null)) 
			{
				if((cartaAtt.equals(Carta.RIGORE_SX)||cartaAtt.equals(Carta.RIGORE_C)||cartaAtt.equals(Carta.RIGORE_DX)) && (cartaGiocata.equals(Carta.PORTIERE ))) 
				{
					fw.println("d");
					fw.println(posizioneGiocatoreAttaccante);
					fw.println(posizioneGiocatoreDifensore);
					fw.println(cartePescate);
					fw.println(cartaAtt.name());
				}
			}
			fw.println("a");
			if((giocatori.length-1) != posizioneGiocatoreAttaccante)
				fw.println(posizioneGiocatoreAttaccante+1);
			else
				fw.println(0);

			if((giocatori.length-1) != posizioneGiocatoreDifensore)
				fw.println(posizioneGiocatoreDifensore+1);
			else
				fw.println(0);
			fw.println(cartePescate);
		}
		fw.close();
	}
	
	public void salvaTurnoRigore(Carta cartaGiocata) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		String relativePath;
		if(this.getCodiceGara().charAt(0) == 'p')
			relativePath = "src/partite/"+this.getCodiceGara()+"/turno.txt";
		else
			relativePath = "src/tornei/"+this.getCodiceGara().substring(0, this.getCodiceGara().length() - 1)+"/"+this.getCodiceGara()+"/turno.txt";
		String absolutePath = currentDirectory + File.separator + relativePath;

		PrintWriter fw = new PrintWriter(absolutePath);
		if(turno.equals("a")) 
		{
			fw.println("d");
			fw.println(posizioneGiocatoreAttaccante);
			fw.println(posizioneGiocatoreDifensore);
			fw.println(cartePescate);
			fw.println(cartaGiocata.name());
		}
		else 
		{
			fw.println("a");
			if((giocatori.length-1) != posizioneGiocatoreAttaccante)
				fw.println(posizioneGiocatoreAttaccante+1);
			else
				fw.println(0);

			if((giocatori.length-1) != posizioneGiocatoreDifensore)
				fw.println(posizioneGiocatoreDifensore+1);
			else
				fw.println(0);
			fw.println(cartePescate);
		}
		fw.close();
	}
	
	public void showFinePartita(ActionEvent event, String aliasVincente, Leaderboard leaderboard, Alert_cambiaForm alert) throws IOException
	{
		giocatori[posizioneGiocatoreAttaccante].aggiungiVittoriaPartita();
		leaderboard.getPlayers().get(leaderboard.indexPlayer(aliasVincente)).aggiungiVittoriaPartita();
		//aggiornata una vittoria nella leaderboard, risalvo il file di testo con i valori aggiornati
		leaderboard.salvaPlayers();
		alert.mostraInformazione(mostraRisultati(), aliasVincente.toUpperCase()+" HA VINTO LA PARTITA");
		//la partita è terminata, mostro all'utente la leaderboard e elimino la cartella della partita
		alert.passaAlForm("/application/FormLeaderboard.fxml", event);
		//s.deleteDirectory("partite");
	}
	//in forse
	/*
	public String toString() {
		String output = "Codice: "+codice.getCodice()+"\nGiocatori:\n";
		for(int i = 0; i < giocatori.length; i++) {
			output+= (i+1)+") "+giocatori[i].getAlias()+"\n";
		}
		return output;
	}*/
}
