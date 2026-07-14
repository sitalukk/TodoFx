package fi.jyu.ohj2.simoL.todo;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // Parempi tapa, käytä tätä!
    private final ObservableList<Tehtava> tehtavat
            = FXCollections.observableArrayList(tehtava -> new Observable[] {tehtava.tehtyProperty()});

    @FXML
    private Button lisaaUusiTehtavaPainike;

    @FXML
    private TextField uusiTehtavaNimi;

    @FXML
    private TableView<Tehtava> tehtavaTaulu;

    @FXML
    private Button poistaValittuPainike;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SortedList<Tehtava> tehtavatLajiteltu = tehtavat.sorted(Comparator.comparing(Tehtava::getTehty));
        tehtavaTaulu.setItems(tehtavatLajiteltu);
        //tehtavaTaulu.setItems(tehtavat);

        tehtavaTaulu.setEditable(true);

        TableColumn<Tehtava, Boolean> tehtySarake = new TableColumn<>("Tehty");
        tehtySarake.setCellValueFactory(cd -> cd.getValue().tehtyProperty());
        tehtySarake.setCellFactory(CheckBoxTableCell.forTableColumn(tehtySarake));
        tehtavaTaulu.getColumns().add(tehtySarake);

        TableColumn<Tehtava, String> tekstiSarake = new TableColumn<>("Tehtävä");
        tekstiSarake.setCellValueFactory(cd -> cd.getValue().tekstiProperty());
        tehtavaTaulu.getColumns().add(tekstiSarake);

        tehtavat.addListener((ListChangeListener<Tehtava>) change -> {
            tallenna();
        });

        lataa();
        uusiTehtavaNimi.setOnAction(event -> lisaaTehtava());
        lisaaUusiTehtavaPainike.setOnAction(event -> lisaaTehtava());

        poistaValittuPainike.setOnAction(event -> poistaValittu());
        poistaValittuPainike.setVisible(false);
        tehtavaTaulu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                poistaValittuPainike.setVisible(false);
            } else {
                poistaValittuPainike.setVisible(true);
            }
        });
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

    private void poistaValittu() {
        // 1. Hae valittu tehtävä taulukon valintamallista
        Tehtava valittuTehtava = tehtavaTaulu.getSelectionModel().getSelectedItem();
        // 2. Jos mitään ei ole valittu, ei tehdä mitään
        if (valittuTehtava == null) {
            return;
        }
        // 3. Poistetaan tehtävä mallilistasta
        tehtavat.remove(valittuTehtava);
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
                //tekemattomat.getChildren().remove(tehtava);
                //tehdyt.getChildren().add(tehtava);
            } else { // Tehtävä ei-valittu--> Siirretään takaisin tekemättömien joukkoon
                //tehdyt.getChildren().remove(tehtava);
                //tekemattomat.getChildren().add(tehtava);
            }
            tallenna();
        });

            uusiTehtavaNimi.clear();
            uusiTehtavaNimi.requestFocus();

            //tallenna();
        }

    }


