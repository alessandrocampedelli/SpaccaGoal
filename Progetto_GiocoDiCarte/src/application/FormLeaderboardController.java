package application;

import java.io.IOException;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;
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

//classe che permette la visualizzazione della leaderboard
public class FormLeaderboardController implements Initializable
{
	//creo un oggetto di classe "Leaderboard" 
	Leaderboard leaderboard = new Leaderboard();
	//creo un oggetto di classe "Gare" 
	Gare gare = new Gare();
	//l'oggetto "Alert_cambiaForm" per cambiare da un form all'altro
	Alert_cambiaForm alert = new Alert_cambiaForm();
	//creo un oggetto "Image" per creare la visualizzazione nel form dell'icon "stampaPdf"
	@FXML
	ImageView imgPdf = new ImageView(new Image(System.getProperty("user.dir")+"/img/icon_pdf.jpg"));
	//la TableView che conterrà tutti i giocatori con le informazioni dell'alias, il numero di partite e tornei vinti e se è un robot oppure no
	@FXML
	private TableView<Giocatore> table = new TableView<Giocatore>();

	//metodo che viene scatenato al click della casa sull'interfaccia grafica
	@FXML
	public void tornaHome(MouseEvent event) throws IOException
	{
		//passo al form principale
		alert.passaAlForm("/application/FormPrincipale.fxml", event);
	}

	//metodo che permette di evidenziare la riga
	@FXML
	public void evidenziaRiga(MouseEvent event) 
	{
		//l'indice della riga selezionata
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0) 
		{
			//richiamo l'utilizzo del metodo per settare il colore di sfondo giallo per la riga selezionata
			impostaRowFactorySelected();
		}
	}

	//metodo che permette di aprire il file al click sull'imageView del pdf
	@FXML
	public void apriPdf(MouseEvent event) 
	{
		//con un blocco try/catch controllo che non ci siano errori nell'apertura del file pdf
		try 
		{
			String path = System.getProperty("user.dir")+"/leaderboard.pdf";
			//creo l'oggetto "File" con il percorso relativo
			File filePDF = new File(path);

			if(filePDF.exists()) {
				//verifico che il supporto Desktop sia disponibile
				if(Desktop.isDesktopSupported()) 
				{
					//creo un oggetto "Desktop"
					Desktop desktop = Desktop.getDesktop();

					//verifico se l'azione OPEN sia supportata
					if (desktop.isSupported(Desktop.Action.OPEN)) 
					{
						//è supportata, apro il file pdf
						desktop.open(filePDF);
					} 
					else 
					{
						//l'apertura diretta non è supportata, prova ad aprire il browser con l'URL del file
						apriPDFConBrowser(filePDF.toURI());
					}
				} 
				else 
				{
					//Desktop non è supportato, prova ad aprire il browser con l'URL del file
					apriPDFConBrowser(filePDF.toURI());
				}
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	//metodo che permette di aprire il pdf con il browser visto che non è possibile l'accesso diretto dal Desktop
	private void apriPDFConBrowser(URI uri) 
	{
		try 
		{
			//apri l'URL nel browser predefinito
			Desktop.getDesktop().browse(uri);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	//metodo che viene eseguito all'apertura del form leaderboard
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		//creo la colonna con l'alias del giocatore (stringa)
		TableColumn<Giocatore, String> alias = new TableColumn<>("ALIAS");
		//la colonna conterrà la proprietà di nome 'alias' della classe giocatore
		alias.setCellValueFactory(new PropertyValueFactory<Giocatore, String>("alias"));
		//stampo con una "textField" l'alias nella riga della colonna
		alias.setCellFactory(TextFieldTableCell.forTableColumn());

		//creo la colonna con l'operatore booleano per sapere se il giocatore è un robot oppure no
		TableColumn<Giocatore, Boolean> robot = new TableColumn<>("ROBOT");
		//la colonna conterrà la proprietà di nome 'robot' della classe giocatore
		robot.setCellValueFactory(cellData -> cellData.getValue().getRobot());
		//stampo con una "checkBox" con il baffo se il giocatore è un robot, altrimenti senza baffo se il giocatore non è un robot
		robot.setCellFactory(CheckBoxTableCell.forTableColumn(robot));

		//creo la colonna con il numero di partite vinte dal giocatore (intero)
		TableColumn<Giocatore, Integer> partiteVinte = new TableColumn<>("PARTITE VINTE");
		//la colonna conterrà la proprietà di nome 'nPartiteVinte' della classe giocatore
		partiteVinte.setCellValueFactory(new PropertyValueFactory<Giocatore, Integer>("nPartiteVinte"));
		//stampo con una "textField" il numero di partite vinte dal giocatore (convertito a stringa)
		partiteVinte.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		//creo la colonna con il numero di tornei vinti dal giocatore (intero)
		TableColumn<Giocatore, Integer> torneiVinti = new TableColumn<>("TORNEI VINTI");
		//la colonna conterrà la proprietà di nome 'nTorneiVinti' della classe giocatore
		torneiVinti.setCellValueFactory(new PropertyValueFactory<Giocatore, Integer>("nTorneiVinti"));
		//stampo con una "textField" il numero di tornei vinti dal giocatore (convertito a stringa)
		torneiVinti.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		//aggiungo le colonne alla "tableView"
		table.getColumns().add(alias);
		table.getColumns().add(robot);
		table.getColumns().add(partiteVinte);
		table.getColumns().add(torneiVinti);

		//richiamo l'utilizzo del metodo per impostare lo stile della tableView
		impostaRowFactoryDefault();

		//setto lo stile della tableView di una grandezza più grande e con allineamento di tutte le colonne centrale
		table.setStyle("-fx-font-size: 18;");
		alias.setStyle("-fx-alignment: CENTER;");
		robot.setStyle("-fx-alignment: CENTER;");
		partiteVinte.setStyle("-fx-alignment: CENTER;");
		torneiVinti.setStyle("-fx-alignment: CENTER;");
		//aggiungo tutti i giocatori della leaderboard alla tableView 
		for(Giocatore g : leaderboard.getPlayers()) 
		{
			table.getItems().add(g);
		}		
		//crea un tooltip e assegna il testo all'immagine per aprire il file pdf
		Tooltip tooltip = new Tooltip("Apri la leaderboard aggiornata su file pdf");
		//associa il tooltip all'ImageView
		Tooltip.install(imgPdf, tooltip);
	}
	//metodo che permette di impostare uno stile differente per righe pari e dispari della tableView
	private void impostaRowFactoryDefault() 
	{
		//impostazione della RowFactory per colorare le righe con colori alternati
		table.setRowFactory(new Callback<TableView<Giocatore>, TableRow<Giocatore>>() 
		{
			@Override
			public TableRow<Giocatore> call(TableView<Giocatore> tableView) 
			{
				return new TableRow<Giocatore>() 
				{
					@Override
					protected void updateItem(Giocatore item, boolean empty) 
					{
						super.updateItem(item, empty);
						//imposta uno stile diverso per le righe pari e dispari (uno più chiaro e uno più scuro)
						if(getIndex() % 2 == 0) 
						{
							setStyle("-fx-background-color: #F5F5F5;-fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;"); 
						}
						else 
						{
							setStyle("-fx-background-color: #E0E0E0;-fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;"); 
						}
					}
				};
			}
		});

	}
	//metodo che permette di colorare la riga selezionata di giallo
	private void impostaRowFactorySelected() 
	{
		//impostazione della RowFactory per colorare l'elemento selezionaato
		table.setRowFactory(row -> new TableRow<Giocatore>() 
		{
			@Override
			protected void updateItem(Giocatore item, boolean empty) 
			{
				super.updateItem(item, empty);
				//controllo se l'elemento è stato selezionato
				if(isSelected()) 
				{
					//l'elemento è stato selezionato, lo coloro di giallo
					setStyle("-fx-background-color: yellow;");
				} 
				else 
				{
					//l'elemento non è stato selezionato, non lo coloro
					setStyle("");
				}
			}
		});
	}
}