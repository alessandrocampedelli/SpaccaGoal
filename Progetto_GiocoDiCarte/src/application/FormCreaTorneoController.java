package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import classi.Salvataggio;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import classi.Alert_cambiaForm;
import classi.Giocatore;
import classi.Torneo;
import classi.Partita;
import classi.Gare;
import classi.Leaderboard;

//classe che permette all'amministratore di creare un torneo 
public class FormCreaTorneoController implements Initializable
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	private Alert_cambiaForm alert = new Alert_cambiaForm();
	//il nuovo torneo che verrà creato e salvato nei file di testo
	static Torneo t;
	//creo l'oggetto della classe "Leaderboard"
	Leaderboard leaderboard = new Leaderboard();
	//creo l'oggetto della classe "Gare"
	Gare gare = new Gare();
	//l'arrayList dei giocatori che conterrà i giocatori che prenderanno parte alla partita
	private ArrayList<Giocatore> giocatori = new ArrayList<>();
	private Salvataggio salvaGara;
	
	//la label con il numero di giocatori inseriti nel programma
	@FXML
	private Label lblGiocatoriInseriti;
	//la label con il numero di giocatori massimo che ancora puoi inserire per creare un torneo partendo dalle semifinali (massimo 4 giocatori)
	@FXML
	private Label lblSemifinale;
	//la label con il numero di giocatori massimo che ancora puoi inserire per creare un torneo partendo dai quarti di finale (massimo 8 giocatori)
	@FXML
	private Label lblQuartiDiFinale;
	//il bottone per aggiungere un giocatore alla tableView
	@FXML
	private Button btnAggiungiGiocatore = new Button();
	//il bottone che permette di creare il torneo con codice e giocatori
	@FXML
	private Button btnCreaTorneo = new Button();
	//la comboBox con tutti i giocatori presenti nella leaderboard (che sono stati creati nel form crea giocatore)
	@FXML
	private ComboBox<String> cmbSelectPlayer = new ComboBox<>();
	//la tableView con i giocatori selezionati che prenderanno parte al torneo
	@FXML
	private TableView<Giocatore> tableGiocatoriInseriti = new TableView<Giocatore>();

	//il bottone che permette di aggiungere un giocatore alla tableView
	@FXML
	public void btnAggiungiGiocatore(ActionEvent event) 
	{
		try 
		{
			//controllo che sia stato selezionato un giocatore nella comboBox, altrimenti genero l'eccezione
			if(cmbSelectPlayer.getSelectionModel().getSelectedItem() == null) 
			{
				throw new IOException();
			}
			//il nome che l'utente ha inserito nella comboBox
			String nome = cmbSelectPlayer.getSelectionModel().getSelectedItem();
			String subNome = "";
			//controllo se il giocatore selezionato contiene la parola robot, nel caso la elimino e mi memorizzo solo l'alias del giocatore
			if(nome.contains("(Robot)")) 
			{
				subNome = nome.substring(0,nome.indexOf('(')-1);
			}
			else 
			{
				subNome = nome;
			}
			//controllo se il nome è già stato selezionato ed è già presente nell'arrayList "giocatori"
			if(nomeGiaUsato(subNome)) 
			{
				//ripulisco la scelta dell'utente effettuata nella comboBox
				cmbSelectPlayer.setValue(null);
				throw new IllegalArgumentException();
			}
			//creo, attraberso l'utilizzo di un metodo della classe "Leaderboard", l'oggetto "Giocatore" aggiungendolo alla leaderboard 
			Giocatore nuovoGiocatore = leaderboard.getPlayers(subNome);
			//aggiungo alla tableview il nuovo giocatore appena creato
			tableGiocatoriInseriti.getItems().add(nuovoGiocatore);
			//mi ricavo l'indice del nuovo giocatore inserito all'interno della leaderboard
			int i = leaderboard.getPlayers().indexOf(nuovoGiocatore);
			//aggiungo il giocatore all'arrayList "giocatori" del torneo
			giocatori.add(leaderboard.getPlayers().get(i));
			//richiamo l'utilizzo del metodo per settare le label di informazioni dei giocatori ancora da inserire
			setLabel();
			//ripulisco la comboBox come all'inizio (senza nessun giocatore selezionato)
			cmbSelectPlayer.setValue(null);
		}
		catch(IOException e) 
		{
			//mando un alert all'utente che non ha selezionato alcun giocatore nella comboBox
			alert.mostraErrore("Il giocatore deve avere un nome" , "ERRORE");
		}
		catch(IllegalArgumentException e)
		{
			//mando un alert all'utente che il giocatore selezionato è già stato inserito (non possono esserci doppioni)
			alert.mostraErrore("Il giocatore selezionato è gia stato inserito" , "ERRORE");
		}
	}

	//il bottone per creare un torneo e salvarlo all'interno della classe "Salvataggio"
	@FXML
	public void btnCreaTorneo(ActionEvent event) throws IOException
	{	
		//chiedo all'utente se è sicuro di volere creare questo torneo, se risponde con il bottone "OK" eseguo questo codice
		if(alert.chiediConferma("Sei sicuro di creare questo torneo con i seguenti giocatori:\n"+getGiocatori(), "MESSAGGIO DI CONFERMA")) 
		{
			//salvo la leaderboard con gli eventuali nuovi giocatori creati
			leaderboard.salvaPlayers();
			//richiamo l'utilizzo del metodo che mi restituisce una stringa di 6 caratteri
			String codice = getRandomString(6,'a','z');
			//aggiungo al codice appena creato il carattere identificativo del torneo "t"
			codice = "t" + codice;
			//mischio l'ordine in cui i giocatori giocheranno le partite
			Collections.shuffle(giocatori);
			//creo un nuovo oggetto "Torneo" con i giocatori che parteciperanno e il codice identificativo del torneo
			t = new Torneo(giocatori,codice);
			//creazione delle partite del torneo
			t.creazionePartite();
			//aggiungo il torneo all'arrayList di gara nella classe "Gare"
			gare.aggiungiGara(t);
			//salvo il torneo all'interno dei file di testo, salvo in un file i giocatori del torneo e nell'altro il tabellone del torneo
			salvaGara = new Salvataggio(t);
			salvaGara.createDirectory("tornei");
			salvaGara.salvaGiocatoriTorneo();
			t.setTabelloneGiocatori(giocatori);
			salvaGara.salvaTabelloneTorneo();
			//dentro la cartella del torneo creo tante cartelle per ogni partita, ognuna con tutte le sue info
			for(Partita p : t.getPartite()) 
			{
				//salvo le partite all'interno della cartella del torneo
				Salvataggio salvaGara = new Salvataggio(p);
				salvaGara.salvaPartita(p, "tornei/"+t.getCodiceGara());
			}
			//mando un alert all'utente informandolo che il torneo è stato creato con successo e gli mostro il codice del torneo
			alert.mostraInformazione("Codice del torneo: "+codice,"TORNEO CREATO CON SUCCESSO");
			//passo al form modalità admin amministratore
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
			//pulisco l'arrayList di giocatori
			giocatori.clear();
		}
	}

	//l'evento che permette di eliminare un giocatore cliccandolo dalla tableView
	@FXML
	public void eliminaGiocatore(MouseEvent event) throws IOException 
	{
		//l'indice del giocatore selezionato nella tableView
		int indiceEliminato = tableGiocatoriInseriti.getSelectionModel().getSelectedIndex();
		//controllo se è stato selezionato un giocatore dalla tableView, altrimenti non eseguo nulla
		if(indiceEliminato != -1) 
		{
			//il giocatore che è stato selezionato dalla tableView
			Giocatore g = tableGiocatoriInseriti.getSelectionModel().getSelectedItem();
			//chiedo all'utente se è sicuro di volere eliminare il giocatore, se risponde con il bottone "OK" eseguo questo codice
			if(alert.chiediConferma("Sei sicuro di voler eliminare il giocatore di nome: " + g.getAlias(), "MESSAGGIO DI CONFERMA"))
			{
				//rimuovo il giocatore dall'arrayList giocatori dall'indice e aggiorno la tableView
				giocatori.remove(indiceEliminato);
				tableGiocatoriInseriti.getItems().clear();
				tableGiocatoriInseriti.getItems().addAll(giocatori);
				//aggiorno le label con il giocatore eliminato
				setLabel();
				//rendo visibile il bottone "aggiungi giocatore" perchè se ha eliminato un giocatore ci sarà sempre un altro giocatore da dover aggiungere
				btnAggiungiGiocatore.setVisible(true);
			}	
		}
	}

	//metodo che viene scatenato al click della freccia indietro sull'interfaccia grafica
	@FXML
	public void btnVaiIndietro1(MouseEvent event) throws IOException
	{
		//elimino tutti i giocatori dall'arrayList
		giocatori.clear();
		//passo al form di modalità admin amministratore
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
	}

	//metodo che permette di creare randomicamente il codice del torneo (il primo carattere di default visto che è un torneo è "t")
	private String getRandomString(int len, char minChar, char maxChar) 
	{
		String s = "";
		for (int i = 0; i < len; ++i)
		{
			//i caratteri vengono scelti randomicamente passando come parametro i codici ASCII in cui deve scegliere (tra 'a' e 'z')
			s += (char) ((Math.random() * (maxChar - minChar)) + minChar);
		}
		//ritorna il codice del torneo creato
		return s;
	}
	
	//metodo che restituisce con una stringa i giocatori che parteciperanno al torneo
	public String getGiocatori() 
	{
		String output = "";
		//stampo tutti i giocatori separati da virgola che parteciperanno al torneo
		for(Giocatore g: giocatori) 
		{
			output += " " + g.getAlias() + ",";
		}
		//elimino l'ultima virgola di troppo
		output = output.substring(0, output.length()-1);
		//ritorna la stringa con i giocatori
		return output;
	}

	//metodo che restituisce un vettore di stringhe con i giocatori che parteciperanno al torneo
	public String[] nomiGiocatori() 
	{
		//il vettore di stringhe avrà la dimensione dell'arrayList "giocatori"
		String[] g = new String[giocatori.size()];
		//riempio il vettore con tutti i giocatori del torneo
		for(int i = 0; i < g.length; i++) 
		{
			g[i] = giocatori.get(i).getAlias();
		}
		//restituisce il vettore di giocatori
		return g;
	}
	
	//metodo che restituisce un operatore booleano che controlla se il nome è gia stato inserito nell'arrayList giocatori
	private boolean nomeGiaUsato(String nome) 
	{
		for(Giocatore g : giocatori) 
		{
			//se il nome è già stato inserito ritorna "true", altrimenti ritorna "false"
			if(g.getAlias().equals(nome))	
			{
				return true;
			}
		}
		return false;
	}

	//metodo che permette di settare la label dei giocatori rimanenti da inserire per creare un torneo
	private void setLabel() 
	{
		//stampo la label con il numero attuale di giocatori inseriti per avviare il torneo
		lblGiocatoriInseriti.setText("Giocatori inseriti: "+giocatori.size());

		//controllo se i giocatori inseriti sono minori o uguali a 4 decrementando la label dei giocatori rimanenti per partire dalla semifinale
		if(giocatori.size() <= 4)
		{
			lblSemifinale.setText("" + (4 - giocatori.size()));
		}
		else
		{
			lblSemifinale.setText("");
		}
		//decremento la label dei giocatori rimanenti per partire il torneo dai quarti di finale
		lblQuartiDiFinale.setText("" + (8 - giocatori.size()));
		
		//i giocatori massimi da inserire sono 8, disabilito il bottone "aggiungi giocatore" per evitare che l'utente ne aggiunga altri
		if(giocatori.size() == 8) 
		{
			btnAggiungiGiocatore.setVisible(false);
		}
		//se i giocatori inseriti sono 4 o 8 rendo visibile il bottone "crea torneo"
		if(giocatori.size() == 4 || giocatori.size() == 8) 
		{
			btnCreaTorneo.setVisible(true);
		}
		else 
		{
			btnCreaTorneo.setVisible(false);
		}
	}

	//metodo che verrà eseguito all'apertura del form
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		//creo la colonna con l'alias del giocatore (stringa)
		TableColumn<Giocatore, String> alias = new TableColumn<>("ALIAS");
		//la colonna conterrà la proprietà di nome 'alias' della classe giocatore
		alias.setCellValueFactory(new PropertyValueFactory<Giocatore, String>("alias"));
		//stampo con una "textField" l'alias nella riga della colonna
		alias.setCellFactory(TextFieldTableCell.forTableColumn());

		//creo la colonna con l'operatore booleano per sapere se il giocatore è un robot oppure no
		TableColumn<Giocatore, Boolean> robot = new TableColumn<>("ROBOT");
		//la colonna conterrà la proprietà di nome 'robot' della classe giocatore
		robot.setCellValueFactory(cellData -> cellData.getValue().getRobot());
		//stampo con una "checkBox" con il baffo se il giocatore è un robot, altrimenti senza baffo se il giocatore non è un robot
		robot.setCellFactory(CheckBoxTableCell.forTableColumn(robot));

		//aggiungo le colonne alla "tableView"
		tableGiocatoriInseriti.getColumns().add(alias);
		tableGiocatoriInseriti.getColumns().add(robot);

		//setto la visualizzazione della tableView
		tableGiocatoriInseriti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

		//setto lo stile della tableView di una grandezza più grande e con allineamento delle colonne alias e robot centrale
		tableGiocatoriInseriti.setStyle("-fx-font-size: 18;");
		alias.setStyle("-fx-alignment: CENTER;");
		robot.setStyle("-fx-alignment: CENTER;");

		//aggiungo alla comboBox tutti i giocatori presenti nella leaderboard
		for(Giocatore g : leaderboard.getPlayers()) 
		{
			//se il giocatore è un robot gli aggiungo la parentesi del robot per farlo presente, in maniera diretta, all'utente
			if(g.isRobot())
			{
				cmbSelectPlayer.getItems().add(g.getAlias()+" (Robot)");
			}
			else
			{
				cmbSelectPlayer.getItems().add(g.getAlias());
			}
		}
	}
}