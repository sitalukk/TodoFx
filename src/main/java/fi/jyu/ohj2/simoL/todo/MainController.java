package fi.jyu.ohj2.simoL.todo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button lisaaUusiTehtavaPainike;

    @FXML
    private TextField uusiTehtavaNimi;

    @FXML
    private Label tekemattomat;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uusiTehtavaNimi.requestFocus();
        lisaaUusiTehtavaPainike.setOnAction(event -> {
            String teksti = uusiTehtavaNimi.getText(); // Haetaan tekstikentän sisältö
            System.out.println("Tekstikentän sisältö: " + teksti);
            tekemattomat.setText(tekemattomat.getText() + teksti + "\n");
            });
        // Write initialization code here
    }

}
