package application;

import java.io.IOException;


import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import classi.Alert_cambiaForm;
import classi.Gara;
import classi.Gare;
import classi.Leaderboard;
import classi.Giocatore;

public class FormLeaderboardController implements Initializable
{
	Leaderboard leaderboard = new Leaderboard();
	Gare gare = new Gare();
	Alert_cambiaForm alert = new Alert_cambiaForm();
	@FXML
	private TableView<Giocatore> table = new TableView<Giocatore>();
	
	@FXML
	public void tornaHome(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
	
	public void initialize(URL arg0, ResourceBundle arg1)
	{
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
		
		TableColumn<Giocatore, Integer> partiteVinte = new TableColumn<>("PARTITE VINTE");
		partiteVinte.setCellValueFactory(new PropertyValueFactory<Giocatore, Integer>("nPartiteVinte"));
		partiteVinte.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		
		TableColumn<Giocatore, Integer> torneiVinti = new TableColumn<>("TORNEI VINTI");
		torneiVinti.setCellValueFactory(new PropertyValueFactory<Giocatore, Integer>("nTorneiVinti"));
		torneiVinti.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		
		//aggiungo le colonne
		table.getColumns().add(alias);
		table.getColumns().add(robot);
		table.getColumns().add(partiteVinte);
		table.getColumns().add(torneiVinti);
		
		alias.setStyle("-fx-alignment: CENTER;");
		robot.setStyle("-fx-alignment: CENTER;");
		partiteVinte.setStyle("-fx-alignment: CENTER;");
		torneiVinti.setStyle("-fx-alignment: CENTER;");

		for(Giocatore g : leaderboard.getPlayers()) 
		{
			table.getItems().add(g);
		}
	}
}