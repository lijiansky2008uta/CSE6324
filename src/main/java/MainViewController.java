import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class MainViewController {
    private Main mainApp = new Main();

    @FXML
    AnchorPane mainPane;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnEnter;

    @FXML
    private ChoiceBox languageChoice;

    @FXML
    private ChoiceBox programLanguageChoice;

    @FXML
    private void initialize() {
        languageChoice.setItems(FXCollections.observableArrayList("English", "Chinese", "Franche"));
        programLanguageChoice.setItems(FXCollections.observableArrayList("Java", "Python"));
    }

    @FXML
    private void handleLogoutLabelClickedAction() throws IOException {
        Stage stage = (Stage)btnLogout.getScene().getWindow();
        stage.close();
        mainApp.showLoginView();
//        mainApp.setUser(null);
    }

    @FXML
    private void handleEnterLabelClickedAction() throws IOException {
        Stage stage = (Stage)btnEnter.getScene().getWindow();
        stage.close();
        mainApp.showSpeechToTextDemo();
//        mainApp.setUser(null);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}
