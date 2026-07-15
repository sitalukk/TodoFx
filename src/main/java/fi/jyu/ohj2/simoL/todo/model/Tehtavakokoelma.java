package fi.jyu.ohj2.simoL.todo.model;

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
    private final ObservableList<Tehtava> tehtavat = FXCollections.observableArrayList(
            tehtava -> new Observable[]{tehtava.tehtyProperty()}
    );

    private final Path tiedostoPolku = Path.of("tehtavat.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public Tehtavakokoelma() {
        tehtavat.addListener((ListChangeListener<Tehtava>) change -> {
            tallenna();
        });
    }

    public ObservableList<Tehtava> getTehtavat() {
        return tehtavat;
    }

    public void tallenna() {
        mapper.writeValue(tiedostoPolku, tehtavat);
    }

    public void lataa() {
        if (Files.notExists(tiedostoPolku)) {
            return;
        }
        try {
            List<Tehtava> kaikkiTehtavat = mapper.readValue(tiedostoPolku, new TypeReference<>() {});
            tehtavat.addAll(kaikkiTehtavat);
        } catch (JacksonException je) {
            System.out.println("JSONin lukeminen epäonnistui: " + je.getMessage());
        }
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
