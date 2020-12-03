import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

import com.sun.glass.events.KeyEvent;
import com.sun.javafx.sg.prism.NGNode;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//import javax.swing.*;



public class InfiniteStreamRecognize implements GSpeechResponseListener{

    @FXML
    AnchorPane speechPane;
    @FXML
    private Button btnRecord;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnLogout;
    @FXML
    private TextArea response;
    @FXML
    private TextArea code;
    @FXML
    private ImageView logo;
    private Stage stage = new Stage();
    MainViewController mainViewController = new MainViewController();

//    GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyAUdcJX8qSgNS-gFBfoN0h64d3vi8wByjc");
    GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw"); //usable
    final Microphone mic = new Microphone(FLACFileWriter.FLAC);
//    private Main mainApp = new Main();

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/Cobra?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "12345678";

    @FXML
    private void initialize() {
        logo.setImage(new Image("Cobra.png"));
    }

    @FXML
    private void Demo() throws IOException {

//        GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyAUdcJX8qSgNS-gFBfoN0h64d3vi8wByjc");
//        final Microphone mic = new Microphone(FLACFileWriter.FLAC);

        duplex.setLanguage("en");
//        btnStop.setDisable(true);

        new Thread(() -> {
            try {
                duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }).start();
//        btnRecord.setDisable(true);
//        btnStop.setDisable(false);

        duplex.addResponseListener(new GSpeechResponseListener() {
            String old_text = "";

            public void onResponse(GoogleResponse gr) {
                String output = "";
                output = gr.getResponse();
                if (gr.getResponse() == null) {
                    this.old_text = response.getText();
                    if (this.old_text.contains("(")) {
                        this.old_text = this.old_text.substring(0, this.old_text.indexOf('('));
                    }
                    System.out.println("Paragraph Line Added");
                    this.old_text = ( response.getText() + "\n" );
                    this.old_text = this.old_text.replace(")", "").replace("( ", "");
                    response.setText(this.old_text);
                    return;
                }
                if (output.contains("(")) {
                    output = output.substring(0, output.indexOf('('));
                }
                if (!gr.getOtherPossibleResponses().isEmpty()) {
                    output = output + " (" + (String) gr.getOtherPossibleResponses().get(0) + ")";
                }
                System.out.println(output);
                response.setText("");
                response.appendText(this.old_text);
                response.appendText(output);
            }
        });

//        if(response.getText().contains("cobra")){
//            writeCode(response.getText());
//        }
    }


    @Override
    public void onResponse(GoogleResponse googleResponse) {

    }

    @FXML
    private void writeCode(String text){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("connect database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Select from database

            stmt = conn.createStatement();
            String sql;
            if (mainViewController.getCodeLanguage() == "Java") {sql = "SELECT speech, code FROM customized_java";}
            else {sql = "SELECT speech, code FROM customized_python";}
            sql = "SELECT speech, code FROM customized_java";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<String> arr = new ArrayList<String>();
            arr.addAll(Arrays.asList(text.split("\\s+")));
            while(rs.next()){
                String codeContent = rs.getString("code");
                if(text.contains(rs.getString("speech"))){
                    System.out.println(codeContent);
                    code.setText(codeContent);
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // handle JDBC error
            se.printStackTrace();
        }catch(Exception e){
            // handle Class.forName error
            e.printStackTrace();
        }finally{
            // close
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

//        // Test Code display
//        ArrayList<String> arr = new ArrayList<String>();
//        arr.addAll(Arrays.asList(text.split("\\s+")));
////        String [] arr = text.split("\\s+");
//        String codeContent = "";
//        if(text.contains("write")){
//            int start = arr.indexOf("write") + 1;
//            int end = arr.size();
//            List<String> lists = arr.subList(start,end);
//            String str1= String.join(" " , lists);
//            codeContent = "System.out.println(\"" + str1 + "\");";
//            System.out.println(codeContent);
//            code.setText(codeContent);
//        }
    }

    @FXML
    private void stopRecord() throws IOException {
        mic.close();
        duplex.stopSpeechRecognition();
//        btnRecord.setDisable(false);
//        btnStop.setDisable(true);
        String text = response.getText();
        System.out.println("-----" + text);
        if(response.getText() != null && response.getText().contains("Cobra")){
            writeCode(text);
        }
        else{
            response.setText("Please Re-record");
        }
    }
//
    @FXML
    private void Logout() throws IOException, InterruptedException, AWTException {
//        Stage stage = (Stage)btnLogout.getScene().getWindow();
//        stage.close();
//        mainApp.showLoginView();
//        mainApp.setUser(null);
        String text = code.getText();
        setClipboardString(text);
        Thread.sleep(3000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_COMMAND);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_COMMAND);



    }
    public static void setClipboardString(String text) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }

}