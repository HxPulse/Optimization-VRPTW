package projet.program.models;

import projet.program.operators.*;
import java.util.*;

public class Neighbor {

    public Solution solution;
    public Permutation permutationDone;

    public Neighbor(Solution solution, Permutation permutationDone) {
        this.solution = solution;
        this.permutationDone = permutationDone;
    }

    public Neighbor() {
        this(new Solution(), new Permutation());
        solution.fitness = Double.POSITIVE_INFINITY;
    }

    public ArrayList<Neighbor> getNeighbours(Solution sol) {
        Solution solution = new Solution(sol);
        ArrayList<Neighbor> neighbours = new ArrayList<>();

        neighbours.add(new CrossExchange(new Solution(solution)).getNeighbor());
        neighbours.add(new Relocate(new Solution(solution)).getNeighbor());
        neighbours.add(new Reverse(new Solution(solution)).getNeighbor());
        neighbours.add(new Exchange(new Solution(solution)).getNeighbor());
        //neighbours.add(new Move(new Solution(solution)).getNeighbor());
        //neighbours.add(new DeuxOpt(new Solution(solution)).getNeighbor());

        return neighbours;
    }

    public Solution getSolution() {
        return solution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neighbor neighbor = (Neighbor) o;
        return Objects.equals(solution, neighbor.solution) && Objects.equals(permutationDone, neighbor.permutationDone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solution, permutationDone);
    }
}
