package fi.jyu.ohj2.simoL.todo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button lisaaUusiTehtavaPainike;

    @FXML
    private TextField uusiTehtavaNimi;

    @FXML
    private VBox tekemattomat;

    @FXML
    private VBox tehdyt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lataa();
        lisaaUusiTehtavaPainike.setOnAction(event -> lisaaTehtava());
        uusiTehtavaNimi.setOnAction(event -> lisaaTehtava());
    }

    private List<Tehtava> haeTehtavat(VBox sailio) {
        return sailio.getChildren().stream()
                .map(n -> (CheckBox) n)
                .map(cb -> new Tehtava(cb.getText(), cb.isSelected()))
                .toList();
    }

    private void tallenna() {
        List<Tehtava> kaikkiTehtavat = new ArrayList<>();
        kaikkiTehtavat.addAll(haeTehtavat(tekemattomat));
        kaikkiTehtavat.addAll(haeTehtavat(tehdyt));
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(Path.of("tehtavat.json"), kaikkiTehtavat);
    }

    private void lataa() {
        Path path = Path.of("tehtavat.json");
        if (Files.notExists(path)) {
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Tehtava> kaikkiTehtavat = mapper.readValue(path.toFile(), new TypeReference<>() {});
            kaikkiTehtavat.forEach(tehtava -> {
                CheckBox checkbox = luoCheckBox(tehtava.getTeksti(), tehtava.getTehty());
                if (tehtava.getTehty()) {
                    tehdyt.getChildren().add(checkbox);
                } else {
                    tekemattomat.getChildren().add(checkbox);
                }
            });
        } catch (JacksonException je) {
            System.out.println("JSONin lukeminen epäonnistui: " + je.getMessage());
        }
    }


    private CheckBox luoCheckBox(String teksti,boolean valittu) {
        CheckBox tehtava = new CheckBox(teksti);
        tehtava.setSelected(valittu);
        // metodin runko piilotettu...
        tehtava.setOnAction(event -> {
            if (tehtava.isSelected()) {
                tekemattomat.getChildren().remove(tehtava);
                tehdyt.getChildren().add(tehtava);
            } else {
                tehdyt.getChildren().remove(tehtava);
                tekemattomat.getChildren().add(tehtava);
            }
            tallenna();
        });
        return tehtava;
    }

    private void lisaaTehtava() {
        String teksti = uusiTehtavaNimi.getText();
        if (teksti == null || teksti.isBlank()) {
            uusiTehtavaNimi.requestFocus();
            return;
        }
        teksti = teksti.trim();
        tekemattomat.getChildren().add(luoCheckBox(teksti, false));
        CheckBox tehtava = new CheckBox(teksti);
        tehtava.setOnAction(event -> {
            if (tehtava.isSelected()) { // Tehtävä valittu --> Siirretään tehtyjen joukkoon
                tekemattomat.getChildren().remove(tehtava);
                tehdyt.getChildren().add(tehtava);
            } else { // Tehtävä ei-valittu--> Siirretään takaisin tekemättömien joukkoon
                tehdyt.getChildren().remove(tehtava);
                tekemattomat.getChildren().add(tehtava);
            }
            tallenna();
        });
        
            uusiTehtavaNimi.clear();
            uusiTehtavaNimi.requestFocus();

            tallenna();
        }

    }


