package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.CheckBox;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import classi.Salvataggio;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.sun.tools.javac.Main;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import classi.Alert_cambiaForm;
import classi.Giocatore;
import classi.Partita;
import classi.Gara;
import classi.Gare;
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
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IOException();
			}
			String nome = txtAlias.getText();
			if(nomeGiaUsato(nome)) 
			{
				txtAlias.clear();
				chbRobot.setSelected(false);
				throw new IllegalArgumentException();
			}
			Giocatore nuovoGiocatore = leaderboard.giocatoreGiaCreato(nome);
			//se è vero significa che questo alias non è mai stato usato e non è collegato a nessun giocatore
			if(nuovoGiocatore == null) 
			{
				nuovoGiocatore = new Giocatore(nome,chbRobot.isSelected());
				//aggiungo il giocatore alla lista di giocatori globali
				leaderboard.addPlayers(nuovoGiocatore);
				leaderboard.salvaPlayers();
			}
			tableGiocatoriInseriti.getItems().add(nuovoGiocatore);
			txtAlias.clear();
			chbRobot.setSelected(false);
		}
		catch (IOException e) 
		{
			alert.mostraErrore("Il giocatore deve avere un nome","ERRORE");
		}
		catch (IllegalArgumentException e) 
		{
			alert.mostraErrore("Nome già utilizzato. Non sono ammessi omonimi","ERRORE");
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//creo la colonna col nome
		TableColumn<Giocatore, String> alias = new TableColumn<>("ALIAS");
		//la colonna conterrà la proprieta di nome 'alias' della classe giocatore
		alias.setCellValueFactory(new PropertyValueFactory<Giocatore, String>("alias"));
		alias.setCellFactory(TextFieldTableCell.forTableColumn());

		//creo la colonna col nome
		TableColumn<Giocatore, Boolean> robot = new TableColumn<>("ROBOT");
		//la colonna conterrà la proprieta di nome 'alias' della classe giocatore
		robot.setCellValueFactory(cellData -> cellData.getValue().getRobot());
		robot.setCellFactory(CheckBoxTableCell.forTableColumn(robot));
		
		//aggiungo le colonne
		tableGiocatoriInseriti.getColumns().add(alias);
		tableGiocatoriInseriti.getColumns().add(robot);

		tableGiocatoriInseriti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		alias.setStyle("-fx-alignment: CENTER;");
		robot.setStyle("-fx-alignment: CENTER;");
		for(Giocatore g : leaderboard.getPlayers()) 
		{
			tableGiocatoriInseriti.getItems().add(g);
		}
	}
}