import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

    GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
//    GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyAUdcJX8qSgNS-gFBfoN0h64d3vi8wByjc");
    final Microphone mic = new Microphone(FLACFileWriter.FLAC);
//    private Main mainApp = new Main();

    @FXML
    private void Demo() throws IOException {

//        GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyAUdcJX8qSgNS-gFBfoN0h64d3vi8wByjc");
//        final Microphone mic = new Microphone(FLACFileWriter.FLAC);

        duplex.setLanguage("en");
        btnStop.setDisable(true);

        new Thread(() -> {
            try {
                duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }).start();
        btnRecord.setDisable(true);
        btnStop.setDisable(false);

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
        ArrayList<String> arr = new ArrayList<String>();
        arr.addAll(Arrays.asList(text.split("\\s+")));
//        String [] arr = text.split("\\s+");
        String codeContent = "";
        if(text.contains("write")){
            int start = arr.indexOf("write") + 1;
            int end = arr.size();
            List<String> lists = arr.subList(start,end);
            String str1= String.join(" " , lists);
            codeContent = "System.out.println(\"" + str1 + "\");";
            System.out.println(codeContent);
            code.setText(codeContent);
        }
    }

    @FXML
    private void stopRecord() throws IOException {
        mic.close();
        duplex.stopSpeechRecognition();
        btnRecord.setDisable(false);
        btnStop.setDisable(true);
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
//    @FXML
//    private void Logout() throws IOException {
//        Stage stage = (Stage)btnLogout.getScene().getWindow();
//        stage.close();
//        mainApp.showLoginView();
////        mainApp.setUser(null);
//    }

}