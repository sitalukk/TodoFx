package fi.jyu.ohj2.simoL.todo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setMinHeight(400);
        stage.setMinWidth(400);
        stage.setTitle("Todo-sovellus");
        stage.show();
    }

}
