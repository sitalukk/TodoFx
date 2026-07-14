package fi.jyu.ohj2.simoL.todo;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

    private ObservableList<Tehtava> tehtavat = FXCollections.observableArrayList();

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
        tehtavat.addListener((ListChangeListener<Tehtava>) change -> {
            paivitaNakyma();
            tallenna();
        });
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(Path.of("tehtavat.json"), tehtavat);
    }

    private void lataa() {
        Path path = Path.of("tehtavat.json");
        if (Files.notExists(path)) {
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Tehtava> kaikkiTehtavat = mapper.readValue(path.toFile(), new TypeReference<>() {});
            tehtavat.addAll(kaikkiTehtavat);
        } catch (JacksonException je) {
            System.out.println("JSONin lukeminen epäonnistui: " + je.getMessage());
        }
    }

    private CheckBox luoCheckBox(Tehtava t) {
        CheckBox tehtava = new CheckBox(t.getTeksti());
        tehtava.setSelected(t.getTehty());
        // metodin runko piilotettu...
        tehtava.setOnAction(event -> {
            // MUUTOS: Emme enää siirrä komponenttia käsin VBoxista toiseen.
            // Sen sijaan päivitämme mallilistaa, mikä laukaisee näkymän päivityksen.
            tehtavat.remove(t);
            tehtavat.add(new Tehtava(t.getTeksti(), !t.getTehty()));
        });
        return tehtava;
    }
    private void paivitaNakyma() {
        // Tyhjennetään nykyiset listat
        tekemattomat.getChildren().clear();
        tehdyt.getChildren().clear();

        // Rakennetaan näkymä uudestaan mallin perusteella.
        // Metodi luoCheckBox(tehtava) saa nyt koko olion parametrina.
        for (Tehtava tehtava : tehtavat) {
            CheckBox cb = luoCheckBox(tehtava);
            if (tehtava.getTehty()) {
                tehdyt.getChildren().add(cb);
            } else {
                tekemattomat.getChildren().add(cb);
            }
        }
    }


    private void lisaaTehtava() {
        String teksti = uusiTehtavaNimi.getText();
        if (teksti == null || teksti.isBlank()) {
            uusiTehtavaNimi.requestFocus();
            return;
        }
        teksti = teksti.trim();
        tehtavat.add(new Tehtava(teksti, false));
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

            //tallenna();
        }

    }


