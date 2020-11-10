import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogionController {
    private Main mainApp = new Main();
    @FXML
    private Button btnLogin;

    @FXML
    private TextField fieldUser;

    @FXML
    private PasswordField fieldPassword;


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

//    @FXML
//    private void initialize() {
//        this.mainApp = mainApp;
//    }


    private boolean checkedValue() {
        String errorMessage = "";
        if (fieldUser.getText().isEmpty()) {
            errorMessage = "Username can not be empty!";
        }
        if (fieldPassword.getText().isEmpty()) {
            errorMessage = "PassWord can not be empty!";
        }
        if (fieldPassword.getText().length() < 4) {
            errorMessage = "Password length cannot be less than 4 digits!";
        }
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("The data is illegal");
            alert.setTitle("Please check the data you entered");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
        return true;
    }

    @FXML
    private void btnLogin(javafx.event.ActionEvent actionEvent) {
        if (checkedValue()) {
            if (fieldUser.getText().equals("jianli") && fieldPassword.getText().equals("123456")) {
//                GUIState.getStage().close();
                //窗口最大化
//                Stage stage = new Stage();
//                stage.initStyle(StageStyle.UNDECORATED);
//                stage.setMaximized(true);
//                stage.setScene(GUIState.getScene());
//                GUIState.setStage(stage);
                Stage primaryStage=(Stage)btnLogin.getScene().getWindow();//将btLogin(登录按钮)与Main类中的primaryStage(新窗口)绑定 并执行close()
                primaryStage.close();
                mainApp.showSystemView();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Wrong User Name or Password");
                alert.setContentText("Sorry, the information you entered could not be correctly verified!");
                alert.show();
            }
        }
    }

}

