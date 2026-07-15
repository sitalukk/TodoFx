package fi.jyu.ohj2.simoL.todo.model;

import javafx.beans.property.*;

public class Tehtava {
    // Alkuperäiset attribuutit on korvattu Property-kääreillä
    private final StringProperty teksti = new SimpleStringProperty("");
    private final BooleanProperty tehty = new SimpleBooleanProperty(false);

    @SuppressWarnings("unused")
    public Tehtava() {}

    public Tehtava(String teksti, boolean tehty) {
        setTeksti(teksti);
        setTehty(tehty);
    }

    // --- Property-setterit ja getterit ---
    // Huomaa, että JavaFX-tyylissä on tapana tarjota kolme metodia per property:
    // 1. Tavallinen get-metodi (palauttaa esim. boolean)
    // 2. Tavallinen set-metodi (ottaa esim. boolean)
    // 3. property-metodi (palauttaa itse Property-olion, esim. BooleanProperty)

    public boolean getTehty() { return this.tehty.get(); }
    public void setTehty(boolean tehty) { this.tehty.set(tehty); }
    public BooleanProperty tehtyProperty() { return this.tehty; }

    public String getTeksti() { return this.teksti.get(); }
    public void setTeksti(String teksti) { this.teksti.set(teksti); }
    public StringProperty tekstiProperty() { return this.teksti; }

    @Override
    public String toString() {
        return getTeksti() + ": " + (getTehty() ? "TEHTY" : "EI TEHTY");
    }
}
