package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import classi.Alert_cambiaForm;
import classi.Gare;
import classi.Gara;
import classi.Partita;
import classi.Salvataggio;
import classi.Torneo;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

//classe che permette di eliminare una partita non terminata
public class FormEliminaEventoController implements Initializable
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	//creo un oggetto della classe "Gare"
	Gare g = new Gare();
	//la listView con tutti i codici delle partite non terminate
	@FXML
	private ListView<String> lblPartite = new ListView<>();
	//la "textArea" in cui verranno stampate le informazioni della partita/torneo con il codice selezionato
	@FXML
	private TextArea txtInfo;
	//il bottone di eliminazione di una partita o un torneo non terminato
	@FXML
	private Button btnElimina;

	//bottone che permette di eliminare una partita o un torneo non terminato
	@FXML
	public void elimina(ActionEvent event) throws IOException
	{
		//restituisce la gara con il codice selezionato sulla listView
		Gara gara = g.getGara(lblPartite.getSelectionModel().getSelectedItem());
		String codice = "";
		//ottengo il codice della gara (partita o torneo)
		if(gara.getCodiceGara().charAt(0) == 't') 
		{
			//elimino l'ultimo carattere del codice perchè sarà il numero della partita del torneo
			codice = gara.getCodiceGara().substring(0, gara.getCodiceGara().length()-1);
		}
		else 
		{
			codice = gara.getCodiceGara();
		}
		Salvataggio s;
		//chiedo all'utente la conferma di cancellazione della gara, se preme il bottone "OK" elimino la gara
		if(alert.chiediConferma("Sei sicuro di voler eliminare l'evento avente codice '"+codice+"' ?", "CONFERMA DI ELIMINAZIONE")) 
		{
			//ottengo il codice della gara (partita o torneo) e la elimino
			if(gara.getCodiceGara().charAt(0) == 'p') 
			{
				s = new Salvataggio(gara);
				//elimino la cartella della partita singola
				s.deleteDirectory("partite/"+codice);
			}
			else 
			{
				s = new Salvataggio(g.getTorneo(codice));
				//elimino la cartella del torneo
				s.deleteDirectory("tornei/"+codice);
			}
			//mando un alert all'utente della cartella eliminata
			alert.mostraInformazione("Eliminazione dell'evento '" + codice + "' avvenuta con successo.", "ELIMINAZIONE ESEGUITA CON SUCCESSO");
			//passo al form di modalità admin amministratore
			alert.passaAlForm("/application/FormModalitaAdminMenu.fxml", event);
		}
	}

	//metodo che viene scatenato al click della freccia indietro sull'interfaccia grafica
	@FXML
	public void tornaIndietro(MouseEvent event) throws IOException 
	{
		//passo al form di modalità admin amministratore
		alert.passaAlForm("/application/FormModalitaAdminMenu.fxml",event);
	}
	
	//questo codice viene scatenato ad ogni click sulla listView di stringhe con tutti i codici di partite e tornei
	@FXML
	public void cliccaGara(MouseEvent event) throws FileNotFoundException
	{
		//mi chiedo se ha selezionato un codice all'interno della listView
		if(lblPartite.getSelectionModel().getSelectedItem() != null) 
		{
			//restituisce la gara con il codice selezionato sulla listView
			Gara gara = g.getGara(lblPartite.getSelectionModel().getSelectedItem());
			String codice = gara.getCodiceGara();
			//la stringa che conterrà la stampa da fare nella textArea
			String info = "";
			//salvo le informazioni della partita selezionata facendo una stampa differente se si tratta di una partita o un torneo
			if(codice.charAt(0) == 'p') 
			{
				info = "Codice partita: "+codice+"\n" + "Numero giocatori: "+gara.getGiocatori().length+"\n\n";
				//converto la "Gara" in tipo "Partita"
				Partita p = (Partita) gara;
				//stampo le informazioni della partita richiamando il metodo "toString" della classe "Partita"
				info += p.toString();
			}
			else 
			{
				//elimino l'ultimo carattere del codice perchè sarà il numero della partita del torneo
				codice = codice.substring(0, codice.length()-1);
				gara = g.getTorneo(codice);
				info = "Codice torneo: "+codice+"\n";
				//converto la "Gara" in tipo "Torneo"
				Torneo t = (Torneo) gara;
				//mi restituisce la prossima partita del torneo da giocare
				Partita p = t.getPartitaTorneo();
				//stampo le informazioni della prossima partita del torneo da giocare
				info += "\nPROSSIMA PARTITA DA INIZIARE/TERMINARE: " + p.getGiocatori()[0].getAlias() + " VS " + p.getGiocatori()[1].getAlias() + "\n";
				//stampo le informazioni della partita richiamando il metodo "toString" della classe "Partita"
				info += p.toString();
				//stampo i giocatori che ancora sono in gara per vincere il torneo
				info +="GIOCATORI RIMASTI ANCORA IN LOTTA PER LA VITTORIA FINALE\n" + t.stampaVincenti();
			}
			//stampo la stringa appena creata all'interno della textArea
			txtInfo.setText(info);
			//rendo visibile il bottone di eliminazione
			btnElimina.setVisible(true);
		}
	}
	
	//metodo che viene eseguito all'apertura del form
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		//aggiungo tutti i codici delle gare alla label. al click su di esse, il programma stamperà il contenuto di esse
		for(Gara gara : g.getGare()) 
		{
			lblPartite.getItems().add(gara.getCodiceGara());
		}
	}
}