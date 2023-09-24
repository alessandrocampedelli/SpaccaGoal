package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import classi.Alert_cambiaForm;
import classi.Gara;
import classi.Gare;

import javafx.fxml.Initializable;

public class FormLeaderboardController implements Initializable{
	static String codicePartita;
	public void copiaCodice(String codice) 
	{
		codicePartita = codice;
	}
	Gare gare = new Gare();
	//Gara g = gare.getGara(null)
	Alert_cambiaForm alert = new Alert_cambiaForm();
	@FXML
	private TableView<String> tblLeaderboard = new TableView<String>();
	@FXML
	public void tornaHome(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		if(!(codicePartita == null)) 
		{

		}
	}
}
