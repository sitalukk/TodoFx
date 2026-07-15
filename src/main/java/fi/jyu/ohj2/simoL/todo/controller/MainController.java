package fi.jyu.ohj2.simoL.todo.controller;

import fi.jyu.ohj2.simoL.todo.App;
import fi.jyu.ohj2.simoL.todo.model.Tehtava;
import fi.jyu.ohj2.simoL.todo.model.Tehtavakokoelma;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    private Tehtavakokoelma tehtavakokoelma = new Tehtavakokoelma();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SortedList<Tehtava> tehtavatLajiteltu = tehtavakokoelma.getTehtavat().sorted(Comparator.comparing(Tehtava::getTehty));
        tehtavaTaulu.setItems(tehtavatLajiteltu);
        tehtavaTaulu.setEditable(true);

        TableColumn<Tehtava, Boolean> tehtySarake = new TableColumn<>("Tehty");
        tehtySarake.setCellValueFactory(cd -> cd.getValue().tehtyProperty());
        tehtySarake.setCellFactory(CheckBoxTableCell.forTableColumn(tehtySarake));
        tehtavaTaulu.getColumns().add(tehtySarake);

        TableColumn<Tehtava, String> tekstiSarake = new TableColumn<>("Tehtävä");
        tekstiSarake.setCellValueFactory(cd -> cd.getValue().otsikkoProperty());
        tehtavaTaulu.getColumns().add(tekstiSarake);

        // Asetetaan taulukolle rakentaja, jolla uudet rivit luodaan
        tehtavaTaulu.setRowFactory(tv -> {
            // Luodaan TableRow-olio riville​
            TableRow<Tehtava> row = new TableRow<>();

            // Lisätään uudelle riville tapahtumakäsittelijä klikkauksille
            row.setOnMouseClicked(event -> {
                // Jos oli hiiren ykkösnapin tuplaklikkaus,
                // eikä tyhjän rivialueen klikkaus, niin käsitellään tapahtuma
                if (event.getButton().equals(MouseButton.PRIMARY) &&
                event.getClickCount() == 2 && !row.isEmpty()) {
                    // Haetaan riviä vastaava Tehtava-olio
                    Tehtava tehtava = row.getItem();
                    // Avataan muokkausdialogi
                    avaaTehtavanMuokkaus(tehtava);
                }
            });

            return row;
        });

        tehtavakokoelma.lataa();
        uusiTehtavaNimi.setOnAction(event -> lisaaTehtava());
        lisaaUusiTehtavaPainike.setOnAction(event -> lisaaTehtava());
        poistaValittuPainike.setOnAction(event -> poistaValittu());
    }

    private List<Tehtava> haeTehtavat(VBox sailio) {
        return sailio.getChildren().stream()
                .map(n -> (CheckBox) n)
                .map(cb -> new Tehtava(cb.getText(), cb.isSelected()))
                .toList();
    }

    private void poistaValittu() {
        Tehtava valittuTehtava = tehtavaTaulu.getSelectionModel().getSelectedItem();
        tehtavakokoelma.poistaTehtava(valittuTehtava);
    }

    private void lisaaTehtava() {
        tehtavakokoelma.lisaaTehtava(uusiTehtavaNimi.getText());
        uusiTehtavaNimi.clear();
        uusiTehtavaNimi.requestFocus();
    }
    private void avaaTehtavanMuokkaus(Tehtava tehtava) {
        try {
            /* 1 */ FXMLLoader loader = new FXMLLoader(App.class.getResource("tehtava-edit.fxml"));
            /* 1 */ Parent root = loader.load();
            /* 1 */ Scene scene = new Scene(root);

            // Haetaan näkymälle luotu kontrolleriolio
            TehtavaEditController controller = loader.getController();
            // Välitetään kontrollerille muokattava tehtävä
            controller.setTehtava(tehtava);

            /* 2 */ Stage dialogi = new Stage();
            /* 2 */ dialogi.setScene(scene);

            /* 3 */ dialogi.setTitle("Tehtävän muokkaus: " + tehtava.getOtsikko());
            /* 3 */ dialogi.setMinWidth(400);
            /* 3 */ dialogi.setMinHeight(300);
            /* 3 */ dialogi.initModality(Modality.APPLICATION_MODAL);

            /* 4 */ dialogi.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}


