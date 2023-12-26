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

public class FormCreaGiocatoreController implements Initializable
{
	@FXML
	private CheckBox chbRobot;
	@FXML
	private TextField txtAlias;
	@FXML
	private Button btnAggiungiGiocatore = new Button();
	@FXML
	private TextField txtUsermail;
	@FXML
	private ComboBox<String> chbDominio;
	@FXML
	private TableView<Giocatore> tableGiocatoriInseriti = new TableView<Giocatore>();
	private Leaderboard leaderboard = new Leaderboard();
	private Alert_cambiaForm alert = new Alert_cambiaForm();
	TableColumn<Giocatore, String> alias;
	
	@FXML
	public void btnVaiIndietro1(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);	
	}
	
	private boolean nomeGiaUsato(String nome) 
	{
		for(Giocatore g : leaderboard.getPlayers()) 
		{
			if(g.getAlias().equals(nome))
			{
				return true;
			}
		}
		return false;
	}
	
	@FXML
	public void btnAggiungiGiocatore(ActionEvent event) 
	{
		try 
		{
			if(txtAlias.getText().trim().equals("")) 
			{
				chbRobot.setSelected(false);
				throw new IOException("Il giocatore deve avere un nome");
			}
			if(txtUsermail.getText().trim().equals("") || chbDominio.getSelectionModel().getSelectedItem() == null) 
			{
				throw new IOException("Il giocatore deve avere una mail");
			}
			String nome = txtAlias.getText();
			String email = txtUsermail.getText() + chbDominio.getSelectionModel().getSelectedItem();
			if(!checkCharacters(nome)) {
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IllegalStateException("Il nome utilizzato deve contenere solo lettere e/o numeri");
			}
			if(!isValidEmail(email)) {
				txtUsermail.clear();
				chbDominio.setValue(null);
				throw new IllegalStateException("La mail contiene caratteri non accettabili");
			}
			if(nome.length() > 13) {
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IndexOutOfBoundsException();
			}
			if(nomeGiaUsato(nome)) 
			{
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IllegalArgumentException();
			}
			Giocatore nuovoGiocatore = leaderboard.getPlayers(nome);
			//se è vero significa che questo alias non è mai stato usato e non è collegato a nessun giocatore
			if(nuovoGiocatore == null) 
			{
				nuovoGiocatore = new Giocatore(nome,chbRobot.isSelected(),email);
				//aggiungo il giocatore alla lista di giocatori globali
				leaderboard.addPlayers(nuovoGiocatore);
				leaderboard.salvaPlayers();
			}
			tableGiocatoriInseriti.getItems().add(nuovoGiocatore);
			txtAlias.clear();
			txtUsermail.clear();
			chbDominio.setValue(null);
			chbRobot.setSelected(false);
		}
		catch (IOException e) 
		{
			alert.mostraErrore(e.getMessage(),"ERRORE");
		}
		catch (IllegalStateException e) 
		{
			alert.mostraErrore(e.getMessage(), "ERRORE");
		}
		catch (IllegalArgumentException e) 
		{
			alert.mostraErrore("Nome già utilizzato. Non sono ammessi omonimi","ERRORE");
		}
		catch (IndexOutOfBoundsException e) 
		{
			alert.mostraErrore("L'alias deve avere una lunghezza massima di 12 caratteri", "ERRORE");
		}
	}
	public String[] nomiGiocatori() 
	{
		String[] g = new String[leaderboard.getPlayers().size()];
		for(int i = 0; i < g.length; i++) 
		{
			g[i] = leaderboard.getPlayers().get(i).getAlias();
		}
		return g;
	}
	//ritorna true se la stringa è corretta
	public boolean checkCharacters(String input) {
        // Utilizza un'espressione regolare per verificare se la stringa contiene solo lettere e numeri
        String regex = "^[a-zA-Z0-9]+$";
        return Pattern.matches(regex, input);
    }
	 public static boolean isValidEmail(String email) {
	        // Definizione del pattern per l'indirizzo email
	        String emailRegex = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+$";

	        // Creazione dell'oggetto Pattern
	        Pattern pattern = Pattern.compile(emailRegex);

	        // Creazione dell'oggetto Matcher
	        Matcher matcher = pattern.matcher(email);

	        // Verifica della corrispondenza
	        return matcher.matches();
	    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//creo la colonna col nome
		TableColumn<Giocatore, String> alias = new TableColumn<>("ALIAS");
		//la colonna conterrà la proprieta di nome 'alias' della classe giocatore
		alias.setCellValueFactory(new PropertyValueFactory<Giocatore, String>("alias"));
		alias.setCellFactory(TextFieldTableCell.forTableColumn());
		
		//creo la colonna con la mail
		TableColumn<Giocatore, String> email = new TableColumn<>("E-MAIL");
		//la colonna conterrà la proprieta di nome 'alias' della classe giocatore
		email.setCellValueFactory(new PropertyValueFactory<Giocatore, String>("email"));
		email.setCellFactory(TextFieldTableCell.forTableColumn());

		//creo la colonna col nome
		TableColumn<Giocatore, Boolean> robot = new TableColumn<>("ROBOT");
		//la colonna conterrà la proprieta di nome 'alias' della classe giocatore
		robot.setCellValueFactory(cellData -> cellData.getValue().getRobot());
		robot.setCellFactory(CheckBoxTableCell.forTableColumn(robot));
		
		//aggiungo le colonne
		tableGiocatoriInseriti.getColumns().add(alias);
		tableGiocatoriInseriti.getColumns().add(email);
		tableGiocatoriInseriti.getColumns().add(robot);

		tableGiocatoriInseriti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		tableGiocatoriInseriti.setStyle("-fx-font-size: 18;");
		
		alias.setStyle("-fx-alignment: CENTER;");
		robot.setStyle("-fx-alignment: CENTER;");
		for(Giocatore g : leaderboard.getPlayers()) 
		{
			tableGiocatoriInseriti.getItems().add(g);
		}
		//inserisco i domini nella choiceBox
		String[] domini = new String[]{
				"@gmail.com",
				"@yahoo.com", 
				"@outlook.com", 
				"@icloud.com", 
				"@aol.com", 
				"@protonmail.com", 
				"@yandex.com", 
				"@zoho.com", 
				"@mail.com", 
				"@gmx.com"};
		ObservableList<String> items =FXCollections.observableArrayList(domini);
		chbDominio.setStyle("-fx-background-color: white; -fx-font-size: 18;");
		chbDominio.setItems(items);
	}
}