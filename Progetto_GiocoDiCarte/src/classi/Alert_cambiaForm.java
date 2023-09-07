package classi;

import java.io.IOException;

import java.util.Optional;

import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class Alert_cambiaForm {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void passaAlForm(String form, ActionEvent event)  throws IOException{
		root = FXMLLoader.load(getClass().getResource(form));
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	public void passaAlForm(String form, MouseEvent event)  throws IOException{
		root = FXMLLoader.load(getClass().getResource(form));
	    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	public void mostraErrore(String setContent, String setHeader) {
		AlertType message = AlertType.ERROR;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText(setContent);
		alert.getDialogPane().setHeaderText(setHeader);
		alert.showAndWait();
	}
	public void mostraInformazione(Gare g, String setContent, String[] setHeader,String codiceUtente) {

		AlertType message = AlertType.INFORMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText(setContent);
		boolean nuovoTorneo = g.cercaCodice(codiceUtente);
		if(nuovoTorneo)
		{
			alert.getDialogPane().setContentText(setHeader[0]);
		}
		else
		{
			alert.getDialogPane().setContentText(setHeader[1]);
		}
		alert.showAndWait();
	}
	public void mostraInformazione(String setContent, String setHeader) 
	{
		AlertType message = AlertType.INFORMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText(setHeader);
		alert.getDialogPane().setContentText(setContent);
		alert.showAndWait();
	}
	public void mostraCartaPescata() {
		Alert alert = new Alert(AlertType.WARNING);

	    alert.initModality(Modality.APPLICATION_MODAL);
	    alert.initOwner(stage);
	    alert.getDialogPane().setContentText("CARTA PESCATA");

	    DialogPane dialogPane = alert.getDialogPane();
	    GridPane grid = new GridPane();
	    ColumnConstraints graphicColumn = new ColumnConstraints();
	    graphicColumn.setFillWidth(false);
	    graphicColumn.setHgrow(Priority.NEVER);
	    ColumnConstraints textColumn = new ColumnConstraints();
	    textColumn.setFillWidth(true);
	    textColumn.setHgrow(Priority.ALWAYS);
	    grid.getColumnConstraints().setAll(graphicColumn, textColumn);
	    grid.setPadding(new Insets(5));

	    Image image1 = new Image(Carta.getUrl("attacco_attaccante.jpg"));
	    ImageView imageView = new ImageView(image1);
	    imageView.setFitWidth(100);
	    imageView.setFitHeight(100);
	    StackPane stackPane = new StackPane(imageView);
	    stackPane.setAlignment(Pos.CENTER);
	    grid.add(stackPane, 0, 0);

	    Label headerLabel = new Label("Warning");
	    headerLabel.setWrapText(true);
	    headerLabel.setAlignment(Pos.CENTER_RIGHT);
	    headerLabel.setMaxWidth(Double.MAX_VALUE);
	    headerLabel.setMaxHeight(Double.MAX_VALUE);
	    grid.add(headerLabel, 1, 0);

	    dialogPane.setHeader(grid);
	    dialogPane.setGraphic(null);

	    alert.showAndWait()
	        .filter(response -> response == ButtonType.OK)
	        .ifPresent(response -> System.out.println("The alert was approved"));
	}
	public boolean chiediConferma(String setContent, String setHeader) {
		AlertType message = AlertType.CONFIRMATION;
		Alert alert = new Alert(message, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText(setHeader);
		alert.getDialogPane().setContentText(setContent);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK)
			return true;
		else
			return false;
	}
}