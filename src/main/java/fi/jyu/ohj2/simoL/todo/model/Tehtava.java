package fi.jyu.ohj2.simoL.todo.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Tehtava {
    private final StringProperty otsikko = new SimpleStringProperty("");
    private final StringProperty kuvaus = new SimpleStringProperty("");
    private final BooleanProperty tehty = new SimpleBooleanProperty(false);
    private final ObjectProperty<Prioriteetti> prioriteetti = new SimpleObjectProperty<>(Prioriteetti.KESKI);

    @SuppressWarnings("unused")
    public Tehtava() {}

    public Tehtava(String otsikko, boolean tehty) {
        setOtsikko(otsikko);
        setTehty(tehty);
    }

    public boolean getTehty() { return this.tehty.get(); }
    public void setTehty(boolean tehty) { this.tehty.set(tehty); }
    public BooleanProperty tehtyProperty() { return this.tehty; }

    public String getOtsikko() { return this.otsikko.get(); }
    public void setOtsikko(String otsikko) { this.otsikko.set(otsikko); }
    public StringProperty otsikkoProperty() { return this.otsikko; }

    public String getKuvaus() { return kuvaus.get(); }
    public void setKuvaus(String kuvaus) { this.kuvaus.set(kuvaus); }
    public StringProperty kuvausProperty() { return this.kuvaus; }

    public Prioriteetti getPrioriteetti() { return this.prioriteetti.get(); }
    public void setPrioriteetti(Prioriteetti prioriteetti) { this.prioriteetti.set(prioriteetti); }
    public ObjectProperty<Prioriteetti> prioriteettiProperty() { return this.prioriteetti; }

    @Override
    public String toString() {
        return getOtsikko() + ": " + (getTehty() ? "TEHTY" : "EI TEHTY");
    }
    private final ObservableList<Tehtava> tehtavat = FXCollections.observableArrayList(
            tehtava -> new Observable[] {
                    (Observable) tehtava.tehtyProperty(),
                    tehtava.otsikkoProperty(),
                    tehtava.kuvausProperty(),
                    tehtava.prioriteettiProperty()
            }
    );
}
