package fi.jyu.ohj2.simoL.todo.persistence;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;
import fi.jyu.ohj2.simoL.todo.model.Tehtava;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonTehtavaRepository implements TehtavaRepository {
    private final Path tallennustiedosto;
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonTehtavaRepository(Path tallennustiedosto) {
        this.tallennustiedosto = tallennustiedosto;
    }

    @Override
    public List<Tehtava> lataa() throws JacksonException {
        if (Files.notExists(tallennustiedosto)) {
            return List.of();
        }
        return mapper.readValue(tallennustiedosto.toFile(), new TypeReference<>() {});
    }

    @Override
    public void tallenna(List<Tehtava> tehtavat) throws JacksonException {
        mapper.writeValue(tallennustiedosto.toFile(), tehtavat);
    }
}
