package fi.jyu.ohj2.simoL.todo.persistence;

import fi.jyu.ohj2.simoL.todo.model.Tehtava;

import java.util.List;

public interface TehtavaRepository {
    List<Tehtava> lataa() throws RepositoryException;
    void tallenna(List<Tehtava> tehtavat) throws RepositoryException;
}