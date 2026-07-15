package fi.jyu.ohj2.simoL.todo.controller;

import fi.jyu.ohj2.simoL.todo.model.Prioriteetti;
import fi.jyu.ohj2.simoL.todo.model.Tehtava;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TehtavaEditController implements Initializable {

    private Tehtava muokattavaTehtava;


    @FXML
    private TextField otsikkoKentta;

    @FXML
    private ComboBox<Prioriteetti> prioriteettiCombo;

    @FXML
    private TextArea kuvausKentta;

    @FXML
    private Button tallennaPainike;

    @FXML
    private Button peruutaPainike;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prioriteettiCombo.setItems(FXCollections.observableArrayList(Prioriteetti.values()));
        tallennaPainike.setOnAction(event -> {
            if (!validoi()) {
                return;
            }
            muokattavaTehtava.setOtsikko(otsikkoKentta.getText());
            muokattavaTehtava.setPrioriteetti(prioriteettiCombo.getValue());
            muokattavaTehtava.setKuvaus(kuvausKentta.getText());
            sulje();
        });
        peruutaPainike.setOnAction(event -> sulje());
    }
    private void sulje() {
        // Kikka: haetaan Scene-olio jostain komponentista
        Scene scene = otsikkoKentta.getScene();
        // Scene-olion getWindow()-metodi palauttaa tämänhetkisen ikkunan
        // Tiedämme, että ikkuna on nyt tyyppiä Stage, joten tehdään tyyppimuunnos
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }


    public void setTehtava(Tehtava tehtava) {
        this.muokattavaTehtava = tehtava;
        otsikkoKentta.setText(tehtava.getOtsikko());
        prioriteettiCombo.setValue(tehtava.getPrioriteetti());
        kuvausKentta.setText(tehtava.getKuvaus());
    }
    private boolean validoi() {
        // Nollataan mahdolliset aiemmat virhetyylit ja vihjetekstit
        otsikkoKentta.setStyle("");
        otsikkoKentta.setPromptText("");

        String otsikko = otsikkoKentta.getText();
        if (otsikko == null || otsikko.isBlank()) {
            // Vaihdetaan reunus punaiseksi virheen merkiksi
            otsikkoKentta.setStyle(
                    "-fx-border-color: red; " +
                            "-fx-background-color: #fdf2f2;");
            // Lisätään kenttään vihjeteksti, joka sisältää virheen
            otsikkoKentta.clear();
            otsikkoKentta.setPromptText("Otsikko puuttuu!");
            // Palautetaan false sen merkiksi, että validointi epäonnistui
            return false;
        }

        return true;
    }


}
