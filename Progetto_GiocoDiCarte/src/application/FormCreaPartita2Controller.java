package application;

import java.awt.TextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
public class FormCreaPartita2Controller {
	@FXML
	private TextField txtAlias1;
	@FXML
	private CheckBox chbRobot1;
	@FXML
	private TextField txtAlias2;
	@FXML
	private CheckBox chbRobot2;
	public void creaForm() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/FormCreaPartita.fxml"));
		FormCreaPartitaController controller = loader.getController();
		int nGiocatori = controller.getNumGiocatori();
		for(int i = 0; i < 2; i++) {
			TextField txt = new TextField();
			txt.setName("txtAlias"+(i+3));
		}
	}
}
