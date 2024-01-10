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
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import classi.Alert_cambiaForm;
import classi.Giocatore;
import classi.Partita;
import classi.Gare;
import classi.Leaderboard;

//classe che permette all'amministratore di creare una partita 
public class FormCreaPartitaController implements Initializable
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	private Alert_cambiaForm alert = new Alert_cambiaForm();
	static Partita p;
	Leaderboard leaderboard = new Leaderboard();
	Gare gare = new Gare();
	private ArrayList<Giocatore> giocatori = new ArrayList<>();

	@FXML
	private Label lblGiocatoriInseriti;
	@FXML
	private Label lblGiocatoriDaInserire;
	@FXML
	private Button btnAggiungiGiocatore = new Button();
	@FXML
	private Button btnCreaPartita = new Button();
	@FXML
	private ComboBox<String> cmbSelectPlayer = new ComboBox<>();
	@FXML
	private TableView<Giocatore> tableGiocatoriInseriti = new TableView<Giocatore>();

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

			lblGiocatoriInseriti.setText("Giocatori inseriti: "+giocatori.size());
			lblGiocatoriDaInserire.setText("Giocatori che puoi ancora inserire: "+(4 - giocatori.size()));
			if(giocatori.size() == 4) 
			{
				btnAggiungiGiocatore.setVisible(false);
			}
			if(giocatori.size() >= 2) 
			{
				btnCreaPartita.setVisible(true);
			}
			else 
			{
				btnCreaPartita.setVisible(false);
			}
			cmbSelectPlayer.setValue(null);
		}
		catch(IOException e) 
		{
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}
		catch(IllegalArgumentException e) 
		{
			alert.mostraErrore("Il giocatore selezionato è gia stato inserito","ERRORE");
		}
	}
	
	@FXML
	public void btnCreaPartita(ActionEvent event) throws IOException
	{	
		if(alert.chiediConferma("Sei sicuro di creare questa partita con i seguenti giocatori: "+getGiocatori(), "MESSAGGIO DI CONFERMA")) 
		{
			//salvo la leaderboard con gli eventuali nuovi giocatori creati
			leaderboard.salvaPlayers();
			String codice = getRandomString(6,'a', 'z');
			codice = "p"+codice;
			p = new Partita(giocatori,codice);
			gare.aggiungiGara(p);
			Salvataggio salvaGara = new Salvataggio(p);
			salvaGara.salvaPartita(p, "partite");
			alert.mostraInformazione("Codice della partita: "+codice,"PARTITA CREATA CON SUCCESSO");
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
				lblGiocatoriInseriti.setText("Giocatori inseriti: "+giocatori.size());
				lblGiocatoriDaInserire.setText("Giocatori che puoi ancora inserire: "+(4 - giocatori.size()));
				if(giocatori.size() != 4) 
				{
					btnAggiungiGiocatore.setVisible(true);
				}
				if(giocatori.size() >= 2) 
				{
					btnCreaPartita.setVisible(true);
				}
				else 
				{
					btnCreaPartita.setVisible(false);
				}
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
	
	//metodo che permette di creare randomicamente il codice della partita (il primo carattere di default visto che è una partita è "p")
	private String getRandomString(int len, char minChar, char maxChar) 
	{
		String s = "";
		for (int i = 0; i < len; ++i)
		{
			//i caratteri vengono scelti randomicamente passando come parametro i codici ASCII in cui deve scegliere (tra 'a' e 'z')
			s += (char) ((Math.random() * (maxChar - minChar)) + minChar);
		}
		//ritorna il codice della partita creato
		return s;
	}

	//metodo che restituisce con una stringa i giocatori che parteciperanno alla partita
	public String getGiocatori() 
	{
		String output = "";
		//stampo tutti i giocatori separati da virgola che parteciperanno alla partita
		for(Giocatore g: giocatori) 
		{
			output += " " + g.getAlias() + ",";
		}
		//elimino l'ultima virgola di troppo
		output = output.substring(0, output.length()-1);
		//ritorna la stringa con i giocatori
		return output;
	}

	//metodo che restituisce un vettore di stringhe con i giocatori che parteciperanno alla partita
	public String[] nomiGiocatori() 
	{
		//il vettore di stringhe avrà la dimensione dell'arrayList "giocatori"
		String[] g = new String[giocatori.size()];
		//riempio il vettore con tutti i giocatori della partita
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