package fi.jyu.ohj2.simoL.todo.controller;

import fi.jyu.ohj2.simoL.todo.model.Tehtava;
import fi.jyu.ohj2.simoL.todo.model.Tehtavakokoelma;
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
        tekstiSarake.setCellValueFactory(cd -> cd.getValue().tekstiProperty());
        tehtavaTaulu.getColumns().add(tekstiSarake);

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

}


