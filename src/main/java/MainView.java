import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class MainView {

    @FXML
    private ChoiceBox languageChoice;

    ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
            "First", "Second", "Third")
    );

    public void setLanguageChoice(ChoiceBox languageChoice) {
        this.languageChoice = languageChoice;
        languageChoice.setItems(FXCollections.observableArrayList(
                "First", "Second", "Third"));
    }
}
