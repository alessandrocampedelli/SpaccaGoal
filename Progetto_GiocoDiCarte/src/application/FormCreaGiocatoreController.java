package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import classi.Alert_cambiaForm;
import classi.Giocatore;
import classi.Leaderboard;

//classe che permette di creare i giocatori che prenderanno parte a partite e tornei
public class FormCreaGiocatoreController implements Initializable
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	private Alert_cambiaForm alert = new Alert_cambiaForm();
	private Leaderboard leaderboard = new Leaderboard();
	//la checkBox che restituisce se il giocatore è un robot (ha il baffo) oppure no
	@FXML
	private CheckBox chbRobot;
	//la textField con il nome del giocatore
	@FXML
	private TextField txtAlias;
	//il bottone che permette l'aggiunta di un giocatore al programma
	@FXML
	private Button btnAggiungiGiocatore = new Button();
	//la textField con l'usermail della mail (non c'è il dominio)
	@FXML
	private TextField txtUsermail;
	//la checkBox con i domini predefiniti più comuni inseriti di default (cerchiamo di ridurre il più possibile l'errore da parte dell'utente)
	@FXML
	private ComboBox<String> chbDominio;
	//la tableView sottostante con la stampa di tutti i giocatori inseriti fino a questo momento
	@FXML
	private TableView<Giocatore> tableGiocatoriInseriti = new TableView<Giocatore>();

	//il bottone che permette di aggiungere un giocatore
	@FXML
	public void btnAggiungiGiocatore(ActionEvent event) 
	{
		try 
		{
			//controllo che l'alias inserito abbia almeno un carattere, altrimenti faccio saltare l'eccezione
			if(txtAlias.getText().trim().equals("")) 
			{
				chbRobot.setSelected(false);
				throw new IOException("Il giocatore deve avere un nome");
			}
			//controllo che la usermail e il dominio siano stati inseriti, altrimenti faccio saltare l'eccezione
			if(txtUsermail.getText().trim().equals("") || chbDominio.getSelectionModel().getSelectedItem() == null) 
			{
				throw new IOException("Il giocatore deve avere una mail");
			}
			//il nome inserito nella textField
			String nome = txtAlias.getText();
			//la usermail inserita nella textField e il dominio scelto dalla comboBox
			String email = txtUsermail.getText() + chbDominio.getSelectionModel().getSelectedItem();
			//controllo che l'alias sia stato inserito correttamente tramite l'utilizzo del metodo, se non fosse corretto faccio saltare l'eccezione
			if(!checkCharacters(nome)) 
			{
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IllegalStateException("Il nome utilizzato deve contenere solo lettere e/o numeri");
			}
			//controllo che la mail sia stata inserita correttamente tramite l'utilizzo del metodo, se non fosse corretta faccio saltare l'eccezione
			if(!isValidEmail(email)) 
			{
				txtUsermail.clear();
				chbDominio.setValue(null);
				throw new IllegalStateException("La mail contiene caratteri non accettabili");
			}
			//controllo che l'alias abbia massimo 12 caratteri, se non fosse così faccio saltare l'eccezione
			if(nome.length() > 13) 
			{
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IndexOutOfBoundsException();
			}
			//controllo che l'alias sia già stato utilizzato tramite l'utilizzo del metodo, se fosse così faccio saltare l'eccezione
			if(nomeGiaUsato(nome)) 
			{
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IllegalArgumentException();
			}
			//se siamo arrivati qui vuole dire che i dati sono stati inseriti correttamente
			//creo l'oggetto Giocatore con l'alias, la mail e se è un robot oppure no
			Giocatore nuovoGiocatore =  new Giocatore(nome,chbRobot.isSelected(),email);
			//aggiungo il giocatore alla lista di giocatori globali e salvo la leaderboard
			leaderboard.addPlayers(nuovoGiocatore);
			leaderboard.salvaPlayers();
			//aggiungo il giocatore appena creato alla tableView e setto textField, checkBox e comboBox come in partenza
			tableGiocatoriInseriti.getItems().add(nuovoGiocatore);
			txtAlias.clear();
			txtUsermail.clear();
			chbDominio.setValue(null);
			chbRobot.setSelected(false);
			//mostro graficamente che il giocatore è stato inserito
			tableGiocatoriInseriti.scrollTo(nuovoGiocatore);
		}
		catch(IOException e) 
		{
			//mando un alert all'utente che non ha inserito l'alias e/o la mail
			alert.mostraErrore(e.getMessage(),"ERRORE");
		}
		catch(IllegalStateException e) 
		{
			//mando un alert all'utente che non ha inserito con i caratteri corretti l'alias e/o la mail
			alert.mostraErrore(e.getMessage(), "ERRORE");
		}
		catch(IllegalArgumentException e) 
		{
			//mando un alert all'utente che evidenzia che è già stato utilizzato il nome inserito
			alert.mostraErrore("Nome già utilizzato. Non sono ammessi omonimi","ERRORE");
		}
		catch(IndexOutOfBoundsException e) 
		{
			//mando un alert all'utente all'utente che la stringa inserita supera la lunghezza massima predefinita (12 caratteri)
			alert.mostraErrore("L'alias deve avere una lunghezza massima di 12 caratteri", "ERRORE");
		}
	}

	//metodo che restituisce se l'alias inserito è già esistente, se così fosse restituisce "true", altrimenti "false"
	private boolean nomeGiaUsato(String nome) 
	{
		for(Giocatore g : leaderboard.getPlayers()) 
		{
			//controllo se il nome inserito nella textField è già presente nell'arrayList della leaderboard
			if(g.getAlias().equals(nome))
			{
				//è già presente, ritorna la variabile booleana "true"
				return true;
			}
		}
		//non è presente, ritorna la variabile booleana "false"
		return false;
	}

	//metodo che ritorna il vettore degli alias dei giocatori presenti nella leaderboard
	public String[] nomiGiocatori() 
	{
		//vettore di stringhe di dimensione del numero di giocatori della leaderboard
		String[] g = new String[leaderboard.getPlayers().size()];
		for(int i = 0; i < g.length; i++) 
		{
			//riempio il vettore con tutti gli alias della leaderboard
			g[i] = leaderboard.getPlayers().get(i).getAlias();
		}
		//ritorna nel programma principale il vettore di giocatori
		return g;
	}

	//metodo che controlla se la stringa inserita è corretta tramite un espressione regolare
	public boolean checkCharacters(String input) 
	{
		//utilizza un'espressione regolare per verificare se la stringa contiene solo lettere e numeri
		String regex = "^[a-zA-Z0-9]+$";
		//ritorna true se la stringa è corretta
		return Pattern.matches(regex, input);
	}

	//metodo che controlla se la mail inserita è corretta tramite un espressione regolare
	public static boolean isValidEmail(String email) 
	{
		//definizione del pattern per l'indirizzo mail corretto
		String emailRegex = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+$";
		//ritorna true se la stringa è corretta
		return Pattern.matches(emailRegex, email);
	}

	//metodo che viene eseguito all'apertura del form giocatore
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		//creo la colonna con l'alias del giocatore (stringa)
		TableColumn<Giocatore, String> alias = new TableColumn<>("ALIAS");
		//la colonna conterrà la proprietà di nome 'alias' della classe giocatore
		alias.setCellValueFactory(new PropertyValueFactory<Giocatore, String>("alias"));
		//stampo con una "textField" l'alias nella riga della colonna
		alias.setCellFactory(TextFieldTableCell.forTableColumn());

		//creo la colonna con la mail del giocatore (stringa)
		TableColumn<Giocatore, String> email = new TableColumn<>("E-MAIL");
		//la colonna conterrà la proprietà di nome 'email' della classe giocatore
		email.setCellValueFactory(new PropertyValueFactory<Giocatore, String>("email"));
		//stampo con una "textField" la mail nella riga della colonna
		email.setCellFactory(TextFieldTableCell.forTableColumn());

		//creo la colonna con l'operatore booleano per sapere se il giocatore è un robot oppure no
		TableColumn<Giocatore, Boolean> robot = new TableColumn<>("ROBOT");
		//la colonna conterrà la proprietà di nome 'robot' della classe giocatore
		robot.setCellValueFactory(cellData -> cellData.getValue().getRobot());
		//stampo con una "checkBox" con il baffo se il giocatore è un robot, altrimenti senza baffo se il giocatore non è un robot
		robot.setCellFactory(CheckBoxTableCell.forTableColumn(robot));

		//aggiungo le colonne alla "tableView"
		tableGiocatoriInseriti.getColumns().add(alias);
		tableGiocatoriInseriti.getColumns().add(email);
		tableGiocatoriInseriti.getColumns().add(robot);
		
		//setto la visualizzazione della tableView
		tableGiocatoriInseriti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		//setto lo stile della tableView di una grandezza più grande e con allineamento delle colonne alias e robot centrale
		tableGiocatoriInseriti.setStyle("-fx-font-size: 18;");
		alias.setStyle("-fx-alignment: CENTER;");
		robot.setStyle("-fx-alignment: CENTER;");
		
		//aggiungo tutti i giocatori della leaderboard inseriti alla tableView 
		for(Giocatore g : leaderboard.getPlayers()) 
		{
			tableGiocatoriInseriti.getItems().add(g);
		}
		
		//il vettore di tutti i domini possibili da inserire nella mail
		String[] domini = new String[]
				{"@gmail.com","@unibo.it","@studio.unibo.it","@yahoo.com",
			     "@outlook.com","@icloud.com","@aol.com","@mail.com","@gmx.com"};
		//converto il vettore dei domini in una "ObservableList" di stringhe
		ObservableList<String> items =FXCollections.observableArrayList(domini);
		//setto il colore della comboBox a bianco e dimensione predefinita a 18
		chbDominio.setStyle("-fx-background-color: white; -fx-font-size: 18;");
		//inserisco i vari domini della mail nella comboBox
		chbDominio.setItems(items);
	}
	
	//metodo che viene scatenato al click della freccia indietro sull'interfaccia grafica
	@FXML
	public void btnVaiIndietro1(MouseEvent event) throws IOException
	{
		//passo al form di modalità admin amministratore
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);	
	}
}