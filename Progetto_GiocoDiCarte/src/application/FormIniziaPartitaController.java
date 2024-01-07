package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.InterruptedException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import classi.Alert_cambiaForm;
import javafx.event.ActionEvent;
import classi.Gare;
import classi.Gara;
import classi.Partita;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.application.Platform;

public class FormIniziaPartitaController implements Initializable
{
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	//creo un oggetto di classe "Gare" che permetterà di ricercare il codice della partita in tutte le gare presenti nel programma
	Gare gare = new Gare();
	//le label che conterranno i nomi dei giocatori della partita
	@FXML
	private Label lblNomeGiocatore1;
	@FXML
	private Label lblNomeGiocatore2;
	@FXML
	private Label lblNomeGiocatore3;
	@FXML
	private Label lblNomeGiocatore4;
	//la progressBar di caricamento per passare al form successivo
	@FXML
	private ProgressBar progressBar;
	//la label con la percentuale della progressBar che verrà incrementata
	@FXML
	private Label lblPercentualeProgressBar = new Label();
	//il bottone non visibile che, una volta terminata la progressBar, avvierà la partita
	@FXML
	private Button btnAvviaPartita = new Button();
	//l'ArrayList che conterrà i giocatori che prendono parte alla partita
	private ArrayList<String> giocatori;
	//il codice dell'utente inserito nel form precedente
	static String codiceUtente;

	//metodo che permette il passaggio del codice da un form ad un altro
	public void copiaInfo(String codice) 
	{
		codiceUtente = codice;
	}

	//il bottone non visibile all'utente che permette di avviare la partita
	@FXML
	public void btnAvviaPartita(ActionEvent event) throws IOException
	{	
		//classe che permette di passare il codice della partita inserito dall'utente al form "giocaPartita" 
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FormGiocaPartita.fxml"));
		loader.load();
		FormGiocaPartitaController form = loader.getController();
		//copiaCodice è un metodo del form "giocaPartita"
		form.copiaCodice(codiceUtente);
		//passo al form gioca partita
		alert.passaAlForm("/application/FormGiocaPartita.fxml", event);
	}
	
	//metodo che viene eseguito all'apertura del form
	public void initialize(URL arg0, ResourceBundle arg1)
	{	
		//controllo se il codice passato non sia null
		if(!(codiceUtente == null)) 
		{
			//coloro la progressBar di verde
			progressBar.setStyle("-fx-accent: green;");
			try 
			{
				//restituisce l'oggetto "Gara" con il codice della partita
				Gara p = (Partita) gare.getGara(codiceUtente);

				//DA QUI!
				if(codiceUtente.charAt(0) == 't') 
				{
					giocatori = new ArrayList<>();
					giocatori.add(p.getGiocatori()[0].getAlias());
					giocatori.add(p.getGiocatori()[1].getAlias());
				}
				else
				{
					giocatori = gare.restituisciGiocatori(codiceUtente);
				}

				if(giocatori.size() >= 2)
				{
					lblNomeGiocatore1.setText(giocatori.get(0));
					lblNomeGiocatore2.setText(giocatori.get(1));
				}
				
				if(giocatori.size() >= 3)
				{
					lblNomeGiocatore3.setText(giocatori.get(2));
				}
				
				if(giocatori.size() == 4)
				{
					lblNomeGiocatore4.setText(giocatori.get(3));
				}

				Thread taskThread = new Thread(() -> 
				{
					double[] i = new double[] {0.0};
				    for (i[0] = 0; i[0] <= 1; i[0] = i[0] + 0.1) 
				    {
				        try 
				        {
				        	//simula un'attività di 0.2 secondi
				            Thread.sleep(200); 
					        Platform.runLater(new Runnable() 
					        {
						        @Override
						        public void run() 
						        {
						        	//la percentuale double del numero
						        	double k = i[0]*100;
						        	//arrotondo la percentuale double a due decimali e la converto in un numero intero (troncamento)
						        	int percentualeIntera = (int)(Math.round(k * 100.0) / 100.0);
						        	//stampo la percentuale in output nella label
							        lblPercentualeProgressBar.setText(percentualeIntera+"%");
						        }
						    });
					        progressBar.setProgress(i[0]);
					        Thread.sleep(300);
				        } 
				        catch (InterruptedException e) 
				        {
				            e.printStackTrace();
				        }
				    }
				    Platform.runLater(new Runnable() 
				    {
				        @Override
				        public void run() 
				        {
							btnAvviaPartita.fire();
				        }
				    });
				});
				//avvia il thread
				taskThread.start();
			}
			catch(FileNotFoundException e) 
			{
				System.out.println(e.getMessage());
			}
		}
	}

	public String getGiocatoriString() 
	{
		String output = "";
		for(int i = 0; i < this.giocatori.size(); i++) 
		{
			output += (i+1) +") "+giocatori.get(i)+"\n";
		}
		return output;
	}
}