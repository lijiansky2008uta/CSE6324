import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.*;


public class MainViewController {
    private Main mainApp = new Main();
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/Cobra?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "12345678";

    @FXML
    AnchorPane mainPane;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnEnter;
    @FXML
    private Button btnSubmit;
    @FXML
    private ChoiceBox languageChoice;
    @FXML
    private ImageView ivCobra;

    @FXML
    private ChoiceBox programLanguageChoice;
    @FXML
    private TextArea txtCode;
    @FXML
    private TextField txtSpeech;
    static String programLanguage = null;
    static String speechLanguage = null;


    @FXML
    private void initialize() {
        languageChoice.setItems(FXCollections.observableArrayList("English", "Chinese", "Franche"));
        programLanguageChoice.setItems(FXCollections.observableArrayList("Java", "Python"));
        ivCobra.setImage(new Image("Cobra.png"));
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

    @FXML
    private void handleSubmitLabelClickedAction() throws IOException {
        programLanguage = programLanguageChoice.getSelectionModel().getSelectedItem().toString();
        Connection conn = null;
        Statement stmt = null;
        try{
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Link database
            System.out.println("Connecting database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //Add
            String sql = "";
            if (programLanguage == "Java") {
                sql = "insert into customized_java (speech,code) values(?,?)";
            }
            else sql = "insert into customized_python (speech,code) values(?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
//            pst.setInt(1,4);
            pst.setString(1, txtSpeech.getText());
            pst.setString(2, txtCode.getText());
            pst.executeUpdate();

            // Close
            pst.close();
            conn.close();
        }catch(SQLException se){
            // Handle JDBC Error
            se.printStackTrace();
        }catch(Exception e){
            // Handle Class.forName Error
            e.printStackTrace();
        }finally{
            // Close resource
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye Mysql!");
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setCodeLanguage() {
        programLanguage = programLanguageChoice.getSelectionModel().getSelectedItem().toString();
    }
    public String getCodeLanguage() {
        return programLanguage;
    }

    public void setSpeechLanguage() {
        speechLanguage = languageChoice.getSelectionModel().getSelectedItem().toString();
    }
    public String getSpeechLanguage() {
        return speechLanguage;
    }

}
