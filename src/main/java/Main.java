import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage stage = new Stage();
    private Scene scene;
    private InfiniteStreamRecognize demo = new InfiniteStreamRecognize();

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage  = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Cobra--Voice Writing Code");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }

    public void showSystemView() {
        try {
            stage.setTitle("MainSystem");
            Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
            stage.setScene(new Scene(root, 607, 480));
            stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *	获取scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     *	获取Stage
     */
    public Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showLoginView() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("Cobra--Voice writing Code");
        stage.setScene(new Scene(root, 500, 300));
        stage.show();
    }

    public void showSpeechToTextDemo() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("speech.fxml"));
        stage.setTitle("Cobra--Voice writing Code");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();

    }
}
