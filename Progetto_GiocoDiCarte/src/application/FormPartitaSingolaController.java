package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import classi.Alert_cambiaForm;
import classi.Gare;

//classe per inserire il codice di una partita singola
public class FormPartitaSingolaController 
{
	@FXML
	private TextField txtCodicePartitaSingola;
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	Gare gare = new Gare();
	
	@FXML
	public void btnGiocaPartitaSingola(ActionEvent event) throws IOException
	{
		try
		{
			if(txtCodicePartitaSingola.getText().trim().equals("")) 
			{
				txtCodicePartitaSingola.clear();
				throw new IOException();
			}
			
			String codiceUtente = txtCodicePartitaSingola.getText().trim();

			//controllo se il codice inserito dall'utente è funzionante
			if(gare.cercaCodice(codiceUtente, 'p')) 
			{			
				alert.mostraInformazione("Codice inserito corretto! E' in corso l'avvio di una partita...","AVVIO PARTITA IN CORSO");

				//classe da cui partono i dati
				FXMLLoader loader = new FXMLLoader(getClass().getResource("FormIniziaPartita.fxml"));
				loader.load();
				FormIniziaPartitaController form = loader.getController();
				form.copiaInfo(codiceUtente);
				
				alert.passaAlForm("/application/FormIniziaPartita.fxml", event);
			}
			else 
			{
				alert.mostraErrore("Codice della partita singola errata!","CODICE PARTITA ERRATO");
			}
		}
		catch(IOException e) 
		{
			alert.mostraErrore("Codice della partita singola errata!","CODICE PARTITA ERRATO");
		}
	}

	//metodo che viene scatenato al click della freccia indietro sull'interfaccia grafica
	@FXML
	public void btnTornaFormModalitaGiocatore(MouseEvent event) throws IOException
	{
		alert.passaAlForm("/application/FormModalitaGiocatore.fxml", event);
	}
}