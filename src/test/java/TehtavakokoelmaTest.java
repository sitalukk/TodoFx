import fi.jyu.ohj2.simoL.todo.model.Prioriteetti;
import fi.jyu.ohj2.simoL.todo.model.Tehtava;
import fi.jyu.ohj2.simoL.todo.model.Tehtavakokoelma;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TehtavakokoelmaTest {

    @Test
    void lisaaTehtava_lisaaTehtavanJaTallentaaSen() {
        // 1. Arrange: Valmistellaan testidata. SYÖTETÄÄN VALE-säiliö!
        MockTehtavaRepository mockRepo = new MockTehtavaRepository();
        Tehtavakokoelma malli = new Tehtavakokoelma(mockRepo);

        // 2. Act: Kutsutaan metodia
        malli.lisaaTehtava("Käy kaupassa");

        // 3. Assert: Tarkistetaan tulos oikeassa data-domainissa
        assertEquals(1, malli.getTehtavat().size(), "Listassa pitäisi olla 1 tehtävä.");
        assertEquals("Käy kaupassa", malli.getTehtavat().get(0).getOtsikko(), "Otsikon pitäisi täsmätä");

        // 4. Assert 2: Varmistetaan mock-luokan avulla, että kokoelma laukaisi tallennuksen tapahtuman yhteydessä
        assertEquals(1, mockRepo.getTallennetutTehtavat().size(), "Data olisi pitänyt tallentaa rajapinnan läpi!");
    }

    @Test
    void lisaaTehtava_eiLisaaTyhjaaOtsikkoa() {
        MockTehtavaRepository mockRepo = new MockTehtavaRepository();
        Tehtavakokoelma malli = new Tehtavakokoelma(mockRepo);

        malli.lisaaTehtava("   "); // Tyhjä syöte

        assertEquals(0, malli.getTehtavat().size(), "Tyhjiä tehtäviä ei saa lisätä listaan.");
    }
    @Test
    void tehtavakokoelmaTallennusToimiiLisayksestaJaPoistosta() {
        // Act ja Assert -vaiheita voi toistaa samassa testausyksikössä
        MockTehtavaRepository repo = new MockTehtavaRepository();
        Tehtavakokoelma kokoelma = new Tehtavakokoelma(repo);
        // Act 1: Lisätään tehtävä
        kokoelma.lisaaTehtava("Käy kaupassa");
        // Assert 1: Tallennus onnistui
        assertEquals(1, repo.getTallennetutTehtavat().size(), "Tehtävät tallentuvat, kun uusi tehtävä lisätään");

        // Act 2: Poistetaan tehtävä
        Tehtava tehtava = kokoelma.getTehtavat().getFirst();
        kokoelma.poistaTehtava(tehtava);
        // Assert 2: Tallennus onnistui
        assertEquals(0, repo.getTallennetutTehtavat().size(), "Tehtävät tallentuvat, kun tehtävä poistetaan");
    }

    @Test
    void tehtavakokoelmaTallennusToimiiAttribuuttienMuutoksesta() {
        MockTehtavaRepository repo = new MockTehtavaRepository();
        Tehtavakokoelma kokoelma = new Tehtavakokoelma(repo);

        kokoelma.lisaaTehtava("Käy kaupassa");
        Tehtava tehtava = kokoelma.getTehtavat().getFirst();
        tehtava.setTehty(true);

        Tehtava tallennettuTehtava = repo.getTallennetutTehtavat().getFirst();
        assertEquals(tehtava.getTehty(), tallennettuTehtava.getTehty(), "Tehtävän tehty-tila tallentuu, kun se muutetaan");

        tehtava.setOtsikko("Mene nukkumaan");
        tallennettuTehtava = repo.getTallennetutTehtavat().getFirst();
        assertEquals(tehtava.getOtsikko(), tallennettuTehtava.getOtsikko(), "Tehtävän otsikko tallentuu, kun se muutetaan");

        tehtava.setPrioriteetti(Prioriteetti.KORKEA);
        tallennettuTehtava = repo.getTallennetutTehtavat().getFirst();
        assertEquals(tehtava.getPrioriteetti(), tallennettuTehtava.getPrioriteetti(), "Tehtävän prioriteetti tallentuu, kun se muutetaan");

        tehtava.setKuvaus("Nukkuminen on kivaa");
        tallennettuTehtava = repo.getTallennetutTehtavat().getFirst();
        assertEquals(tehtava.getKuvaus(), tallennettuTehtava.getKuvaus(), "Tehtävän kuvaus tallentuu, kun se muutetaan");
    }

}
