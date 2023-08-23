package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;
import classi.Gara;
import classi.Gare;
import classi.Partita;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;


public class FormGiocaPartitaController implements Initializable
{	
	static Gare g = new Gare();
	static String codicePartita;
	static Partita partita = (Partita) g.getGara(codicePartita);
	public void copiaInfo(String codice) 
	{
		codicePartita = codice;
	}
	@FXML
	private ListView<String> listCarte = new ListView<String>();
	// Event Listener on Button.onAction
	@FXML
	public void btnPartita(ActionEvent event) 
	{
		System.out.println("OK");
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		GridPane pane = new GridPane();
		listCarte.setCellFactory(param -> new Cell());
	}
	//classe che identifica la singola carta nella listBox
	static class Cell extends ListCell<String>{
		HBox hBox = new HBox();
		Pane pane = new Pane();
		Image carta = new Image(partita.getMazzo().getCarta().getImmagine().getUrl());
		ImageView img = new ImageView(carta);
		
		public Cell() {
			super();
			hBox.getChildren().addAll(img,pane);
			hBox.setHgrow(pane, Priority.ALWAYS);
		}
		public void updateItem(String name, boolean empty) {
			super.updateItem(name, empty);
			setText(null);
			setGraphic(null);
			if(name != null && !empty) {
				setGraphic(hBox);
			}
		}
	}
}
