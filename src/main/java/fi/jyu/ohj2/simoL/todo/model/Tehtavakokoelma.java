package fi.jyu.ohj2.simoL.todo.model;

import fi.jyu.ohj2.simoL.todo.persistence.RepositoryException;
import fi.jyu.ohj2.simoL.todo.persistence.TehtavaRepository;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Tehtavakokoelma {
    // Riippuvuus tallennusmekanismista on nyt rajapinnan takana
    private final TehtavaRepository repository;

    private final ObservableList<Tehtava> tehtavat = FXCollections.observableArrayList(
            tehtava -> new Observable[]{(Observable) tehtava.tehtyProperty()}
    );



    public Tehtavakokoelma(TehtavaRepository repository) {
        this.repository = repository;

        this.tehtavat.addListener((ListChangeListener<Tehtava>) change -> {
            tallenna();
        });
    }

    public void lataa() {
        try {
            List<Tehtava> kaikkiTehtavat = repository.lataa();
            tehtavat.addAll(kaikkiTehtavat);
        } catch (RepositoryException e) {
            System.out.print(e.getMessage());
        }
    }

    public void tallenna() {
        try {
            repository.tallenna(tehtavat);
        } catch (RepositoryException e) {
            System.out.print(e.getMessage());
        }
    }

    // ... kaikki muut lisaaTehtava yms. samat kuin aiemmin!


    public ObservableList<Tehtava> getTehtavat() {
        return tehtavat;
    }

    public void lisaaTehtava(String teksti) {
        if (teksti == null || teksti.isBlank()) {
            return;
        }
        teksti = teksti.trim();
        tehtavat.add(new Tehtava(teksti, false));
    }

    public void poistaTehtava(Tehtava tehtava) {
        if (tehtava == null) {
            return;
        }
        tehtavat.remove(tehtava);
    }
}
