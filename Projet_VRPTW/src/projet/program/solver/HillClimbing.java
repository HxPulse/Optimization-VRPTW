package projet.program.solver;

import projet.program.models.Neighbor;
import projet.program.models.Solution;

import java.util.ArrayList;
import java.util.Objects;

public class HillClimbing {
    public Solution initialSolution;

    public HillClimbing(Solution solution) {
        this.initialSolution = solution;
    }

    public Object[] solve(){
        Solution bestSol = new Solution(initialSolution);
        boolean upgrade = true;
        int nbIteration = 0;
        while(upgrade){
            upgrade = false;
            ArrayList<Neighbor> neighbors = new Neighbor().getNeighbours(bestSol);

            for(Neighbor neighbor : neighbors){
                nbIteration += neighbor.permutationDone.nbIteration;
                if(neighbor.solution.fitness < bestSol.fitness){
                    bestSol = neighbor.solution;
                    upgrade = true;
                }
            }
        }
        System.out.println("Valid solutions created: " + nbIteration);
        Object[] result = new Object[2];
        result[0] = bestSol;
        result[1] = nbIteration;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HillClimbing that = (HillClimbing) o;
        return Objects.equals(initialSolution, that.initialSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialSolution);
    }
}
