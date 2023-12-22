package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import classi.Alert_cambiaForm;
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
	@FXML
	public void evidenziaRiga(MouseEvent event) {
		// Set background color to yellow for the selected row
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        System.out.println(selectedIndex);
        if (selectedIndex >= 0) {
        	impostaRowFactorySelected();
        }
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
		
		impostaRowFactoryDefault();
		
		table.setStyle("-fx-font-size: 18;");
		alias.setStyle("-fx-alignment: CENTER;");
		robot.setStyle("-fx-alignment: CENTER;");
		partiteVinte.setStyle("-fx-alignment: CENTER;");
		torneiVinti.setStyle("-fx-alignment: CENTER;");

		for(Giocatore g : leaderboard.getPlayers()) 
		{
			table.getItems().add(g);
		}
	}
	private void impostaRowFactoryDefault() {
		// Impostazione della RowFactory per colorare le righe con colori alternati
        table.setRowFactory(new Callback<TableView<Giocatore>, TableRow<Giocatore>>() {
            @Override
            public TableRow<Giocatore> call(TableView<Giocatore> tableView) {
                return new TableRow<Giocatore>() {
                    @Override
                    protected void updateItem(Giocatore item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            // Imposta uno stile diverso per le righe pari e dispari
                            if (getIndex() % 2 == 0) {
                                setStyle("-fx-background-color: #F5F5F5;-fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;"); 
                            }else {
                            	setStyle("-fx-background-color: #E0E0E0;-fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;"); 
                            }
                        } else {
                            setStyle(null); 
                        }
                    }
                };
            }
        });
        
	}
	private void impostaRowFactorySelected() {
		table.setRowFactory(row -> new TableRow<Giocatore>() {
            @Override
            protected void updateItem(Giocatore item, boolean empty) {
                super.updateItem(item, empty);
                if (isSelected()) {
                    setStyle("-fx-background-color: yellow;");
                } else {
                    setStyle("");
                }
            }
        });
	}
}