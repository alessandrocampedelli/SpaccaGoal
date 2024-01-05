package application;

import javafx.fxml.FXML;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import classi.Alert_cambiaForm;

//classe del form modalità giocatore principale che contiene le due modalità che esso può scegliere, partita singola o torneo
public class FormModalitaGiocatoreController
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	
	@FXML
	public void btnPartitaSingola(ActionEvent event) throws IOException
	{
		//passo al form di avvio di una partita singola
		alert.passaAlForm("/application/FormPartitaSingola.fxml", event);
	}

	@FXML
	public void btnTorneo(ActionEvent event) throws IOException
	{
		//passo al form di avvio di un torneo
		alert.passaAlForm("/application/FormTorneo.fxml", event);
	}

	//metodo che viene scatenato al click della freccia indietro sull'interfaccia grafica
	@FXML
	public void btnTornaModalitaPrincipale(MouseEvent event) throws IOException
	{
		//passo al form principale (main)
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}
}