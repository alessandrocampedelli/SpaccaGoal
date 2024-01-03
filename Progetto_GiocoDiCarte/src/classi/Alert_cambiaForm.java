package classi;

import java.io.IOException;
import java.util.Optional;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

//classe che viene richiamata ogni volta che è necessario un cambio di schermata del form e per mandare gli alert all'utente
public class Alert_cambiaForm 
{
	private Stage stage;
	private Scene scene;
	private AnchorPane root;
	
	public Stage getStage() 
	{
		return stage;
	}
	
	//proprietà "set" per settare lo stage da parte dell'evento "ActionEvent"
	public void setStage(ActionEvent event) 
	{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	}
	
	//proprietà "set" per settare lo stage da parte dell'evento "MouseEvent"
	public void setStage(MouseEvent event) 
	{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	}
	
	public Scene getScene() 
	{
		return scene;
	}
	
	public void setScene(Scene scene) 
	{
		this.scene = scene;
	}
	
	public AnchorPane getRoot() 
	{	
		return root;
	}
	
	//proprietà che permette di settare la root in base alla stringa del form in cui viene richiamato questo metodo
	public void setRoot(String form) throws IOException 
	{
		this.root = FXMLLoader.load(getClass().getResource(form));
	}
	
	//metodo che permette il passaggio da un form all'altro (riconosciuto dal nome) tramite l'evento "ActionEvent"
	public void passaAlForm(String form, ActionEvent event)  throws IOException
	{
		try 
		{
			if(((Node)event.getSource()).getScene().equals(null))
			{
				throw new NullPointerException();
			}
			//setto la room e lo stage con le informazioni passate
			setRoot(form);
		    setStage(event);
		    this.scene = new Scene(getRoot());
		    scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		    this.stage.setScene(this.scene);
		    //permetto all'utente la visibilità del form successivo
		    this.stage.show();
		}
		catch(NullPointerException e) 
		{
			//è stata sollevata un eccezione, consumo l'evento permettendone la chiusura
			event.consume();
		}
	}
	
	//metodo che permette il passaggio da un form all'altro (riconosciuto dal nome) tramite l'evento "MouseEvent"
	public void passaAlForm(String form, MouseEvent event)  throws IOException
	{
		try 
		{
			if(((Node)event.getSource()).getScene().equals(null))
			{
				throw new NullPointerException();
			}
			//setto la room e lo stage con le informazioni passate
			setRoot(form);
		    setStage(event);
		    this.scene = new Scene(getRoot());
		    this.stage.setScene(this.scene);
		    scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		    //permetto all'utente la visibilità del form successivo
		    this.stage.show();
		}
		catch(NullPointerException e) 
		{
			//è stata sollevata un eccezione, consumo l'evento permettendone la chiusura
			event.consume();
		}
	}
	
	//metodo che manda un alert all'utente di errore con il messaggio passato come parametro
	public void mostraErrore(String setContent, String setHeader) 
	{
		//questa tipologia di alert è di tipo errore
		AlertType message = AlertType.ERROR;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText(setContent);
		alert.getDialogPane().lookup(".content").setStyle("-fx-font-size: 15;");
		alert.getDialogPane().setHeaderText(setHeader);
		alert.getDialogPane().lookup(".header-panel").setStyle("-fx-font-size: 15;-fx-font-weight: bold;");
		alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0)).setStyle("-fx-font-size: 15;");
		alert.showAndWait();
	}
	
	//metodo che manda un alert all'utente di informazione con il messaggio passato come parametro
	public void mostraInformazione(String setContent, String setHeader) 
	{
		//questa tipologia di alert è di tipo informazione
		AlertType message = AlertType.INFORMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText(setContent);
		alert.getDialogPane().lookup(".content").setStyle("-fx-font-size: 15;");
		alert.getDialogPane().setHeaderText(setHeader);
		alert.getDialogPane().lookup(".header-panel").setStyle("-fx-font-size: 15;-fx-font-weight: bold;");
		alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0)).setStyle("-fx-font-size: 15;");
		alert.showAndWait();
	}
	
	//metodo che manda un alert all'utente di conferma all'utente con il messaggio passato come parametro
	public boolean chiediConferma(String setContent, String setHeader) 
	{
		//questa tipologia di alert è di tipo conferma	
		AlertType message = AlertType.CONFIRMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.getDialogPane().setContentText(setContent);
		alert.getDialogPane().lookup(".content").setStyle("-fx-font-size: 15;");
		alert.getDialogPane().setHeaderText(setHeader);
		alert.getDialogPane().lookup(".header-panel").setStyle("-fx-font-size: 15;-fx-font-weight: bold;");
		alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0)).setStyle("-fx-font-size: 15;");
		//bottone utile per ricevere un feedback da parte dell'utente
		Optional<ButtonType> result = alert.showAndWait();
		//controllo che l'utente abbia premuto il bottone "OK" e proseguo, altrimenti resto nella schermata precedente
		if(result.get() == ButtonType.OK)
			return true;
		else
			return false;
	}
}