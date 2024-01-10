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
	@FXML
	private TableView<Giocatore> tableGiocatoriInseriti = new TableView<Giocatore>();
	@FXML
	private Label lblGiocatoriInseriti;
	@FXML
	private Label lblSemifinale;
	@FXML
	private Label lblQuartiDiFinale;
	@FXML
	private Button btnAggiungiGiocatore = new Button();
	@FXML
	private Button btnCreaTorneo = new Button();
	@FXML
	private ComboBox<String> cmbSelectPlayer = new ComboBox<>();

	private ArrayList<Giocatore> giocatori = new ArrayList<>();
	private Salvataggio salvaGara;
	static Torneo t;
	Gare gare = new Gare();
	Leaderboard leaderboard = new Leaderboard();

	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	private Alert_cambiaForm alert = new Alert_cambiaForm();

	@FXML
	public void btnAggiungiGiocatore(ActionEvent event) 
	{
		try 
		{
			if(cmbSelectPlayer.getSelectionModel().getSelectedItem() == null) 
			{
				throw new IOException();
			}
			String nome = cmbSelectPlayer.getSelectionModel().getSelectedItem();
			String subNome = "";
			if(nome.contains("(Robot)")) 
			{
				subNome = nome.substring(0,nome.indexOf('(')-1);
			}
			else 
			{
				subNome = nome;
			}
			if(nomeGiaUsato(subNome)) 
			{
				cmbSelectPlayer.setValue(null);
				throw new IllegalArgumentException();
			}
			Giocatore nuovoGiocatore = leaderboard.getPlayers(subNome);
			tableGiocatoriInseriti.getItems().add(nuovoGiocatore);
			int i = leaderboard.getPlayers().indexOf(nuovoGiocatore);
			giocatori.add(leaderboard.getPlayers().get(i));
			setLabel();
			cmbSelectPlayer.setValue(null);
		}
		catch(IllegalArgumentException e)
		{
			alert.mostraErrore("Il giocatore selezionato è gia stato inserito","ERRORE");
		}
		catch(IOException e) 
		{
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}
	}

	@FXML
	public void btnCreaTorneo(ActionEvent event) throws IOException
	{	
		if(alert.chiediConferma("Sei sicuro di creare questo torneo con i seguenti giocatori:\n"+getGiocatori(), "MESSAGGIO DI CONFERMA")) 
		{
			String codice = getRandomString(6,'a', 'z');
			leaderboard.salvaPlayers();
			codice = "t"+codice;
			//mischio l'ordine in cui i giocatori giocano la partita
			Collections.shuffle(giocatori);
			t = new Torneo(giocatori,codice);
			//creazione delle partite
			t.creazionePartite();
			gare.aggiungiGara(t);
			//creo la cartella del torneo
			salvaGara = new Salvataggio(t);
			salvaGara.createDirectory("tornei");
			salvaGara.salvaGiocatoriTorneo();
			t.setTabelloneGiocatori(giocatori);
			salvaGara.salvaTabelloneTorneo();
			//dentro la cartella del torneo creo tante cartelle per ogni partita, ognuna con tutte le sue info
			for(Partita p : t.getPartite()) 
			{
				Salvataggio salvaGara = new Salvataggio(p);
				salvaGara.salvaPartita(p, "tornei/"+t.getCodiceGara());
			}
			alert.mostraInformazione("Codice del torneo: "+codice,"TORNEO CREATO CON SUCCESSO");
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
			giocatori.clear();
		}
	}


	@FXML
	public void eliminaGiocatore(MouseEvent event) throws IOException 
	{
		int indiceEliminato = tableGiocatoriInseriti.getSelectionModel().getSelectedIndex();
		if(indiceEliminato != -1) 
		{
			Giocatore g = tableGiocatoriInseriti.getSelectionModel().getSelectedItem();
			if(alert.chiediConferma("Sei sicuro di voler eliminare il giocatore di nome: "+g.getAlias(), "MESSAGGIO DI CONFERMA"))
			{
				giocatori.remove(indiceEliminato);
				tableGiocatoriInseriti.getItems().clear();
				tableGiocatoriInseriti.getItems().addAll(giocatori);
				setLabel();
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
		
		//i giocatori massimi da inserire sono 8, disabilito il bottone "aggiungi giocatore" per evitare che ne aggiunga altri
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