package fi.jyu.ohj2.simoL.todo;

public class Tehtava {
    @SuppressWarnings("FieldMayBeFinal")
    private String teksti;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean tehty;

    @SuppressWarnings("unused")
    public Tehtava() {
        this.teksti = teksti;
        this.tehty = tehty;/* Jätä tyhjäksi tai tee oletustoteutus */ }

    public Tehtava(String teksti, boolean tehty) {
        this.teksti = teksti;
        this.tehty = tehty; /* Lisää toteutus */ }

    public boolean getTehty() { /* Lisää toteutus */
    return tehty;}

    public String getTeksti() { /* Lisää toteutus */
    return teksti;}
}