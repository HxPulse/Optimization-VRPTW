package projet.program.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Permutation {
    public List<Client> editedClients;
    public String operatorUsed;
    public int nbIteration;

    public Permutation(List<Client> editedClients, String operatorUsed, int nbIteration) {
        this.editedClients = editedClients;
        this.operatorUsed = operatorUsed;
        this.nbIteration = nbIteration;
    }

    public Permutation() {
        this.editedClients = new ArrayList<>();
        this.operatorUsed = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permutation that = (Permutation) o;
        return Objects.equals(editedClients, that.editedClients) && Objects.equals(operatorUsed, that.operatorUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(editedClients, operatorUsed);
    }
}
