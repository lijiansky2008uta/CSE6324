import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LogionController {
    private Main mainApp = new Main();
    @FXML
    private Button btnLogin;

    @FXML
    private TextField fieldUser;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private ImageView logo;


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/Cobra?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "12345678";
    Connection conn = null;
    Statement stmt = null;

    @FXML
    private void initialize() {
        logo.setImage(new Image("Cobra.png"));
    }


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
        Connection conn = null;
        Statement stmt = null;
        if (checkedValue()) {
            try {
                Class.forName(JDBC_DRIVER);
                System.out.println("connect database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                // Select from database
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT userName, password FROM user";
                ResultSet rs = stmt.executeQuery(sql);
                HashMap<String, String> user = new HashMap<>();
                while (rs.next()) {
                    String userName = rs.getString("userName");
                    String password = rs.getString("password");
                    user.put(userName, password);
                }
                if (user.containsKey(fieldUser.getText()) && user.get(fieldUser.getText()).equals(fieldPassword.getText())) {
                    Stage primaryStage = (Stage) btnLogin.getScene().getWindow();//将btLogin(登录按钮)与Main类中的primaryStage(新窗口)绑定 并执行close()
                    primaryStage.close();
                    mainApp.showSystemView();
                } else if(!user.containsKey(fieldUser.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText("User not exist");
                    alert.setContentText("Please register first.");
                    alert.show();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText("Wrong User Name or Password");
                    alert.setContentText("Sorry, the information you entered could not be correctly verified!");
                    alert.show();
                }
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                // handle JDBC error
                se.printStackTrace();
            } catch (Exception e) {
                // handle Class.forName error
                e.printStackTrace();
            } finally {
                // close
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException se2) {
                }
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
//        if (checkedValue()) {
//            if (fieldUser.getText().equals("jianli") && fieldPassword.getText().equals("123456")) {
//                Stage primaryStage=(Stage)btnLogin.getScene().getWindow();//将btLogin(登录按钮)与Main类中的primaryStage(新窗口)绑定 并执行close()
//                primaryStage.close();
//                mainApp.showSystemView();
//
//            } else {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Login Failed");
//                alert.setHeaderText("Wrong User Name or Password");
//                alert.setContentText("Sorry, the information you entered could not be correctly verified!");
//                alert.show();
//            }
//        }
    }

    public void btnRegister(ActionEvent actionEvent) {
        Connection conn = null;
        Statement stmt = null;
        if (checkedValue()) {
            try {
                // Register JDBC driver
                Class.forName(JDBC_DRIVER);

                // Link database
                System.out.println("Connecting database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                String searchUser = "SELECT userName FROM user";
                ResultSet rs = stmt.executeQuery(searchUser);
                ArrayList<String> user = new ArrayList<>();
                while (rs.next()) {
                    String userName = rs.getString("userName");
                    user.add(userName);
                }
                if (user.contains(fieldUser.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Register Failed");
                    alert.setHeaderText("User already exist");
                    alert.setContentText("Please login to the system");
                    alert.show();
                }
                else {
                    String addUser = "insert into user (userName,password) values(?,?)";
                    PreparedStatement pst = conn.prepareStatement(addUser);
//            pst.setInt(1,4);
                    pst.setString(1, fieldUser.getText());
                    pst.setString(2, fieldPassword.getText());
                    pst.executeUpdate();
                    pst.close();
                    conn.close();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Register successful!");
                    alert.setHeaderText("Congratulations");
                    alert.setContentText("Please go back to login.");
                    alert.show();
                }


                //Add


                // Close

            } catch (SQLException se) {
                // Handle JDBC Error
                se.printStackTrace();
            } catch (Exception e) {
                // Handle Class.forName Error
                e.printStackTrace();
            } finally {
                // Close resource
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException se2) {
                }
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }
}

