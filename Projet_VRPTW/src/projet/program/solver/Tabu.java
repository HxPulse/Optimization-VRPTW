package projet.program.solver;

import projet.program.models.Neighbor;
import projet.program.models.Permutation;
import projet.program.models.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Tabu {
    // More about this here : https://en.wikipedia.org/wiki/Tabu_search
    public ArrayList<Permutation> forbiddenPermutations = new ArrayList<>();
    public HashMap<Solution, ArrayList<Permutation>> visitedStates = new HashMap<>();
    public Solution initialSolution;
    public final int maxIterations;
    public final int maxTabuSize;

    // Constructor
    public Tabu(int maxIterations, int maxTabuSize, Solution solution) {
        this.maxIterations = maxIterations;
        this.maxTabuSize = maxTabuSize;
        this.initialSolution = solution;
    }

    public Object[] solve(){
        Solution currentSol = new Solution(initialSolution);
        Solution bestSol = new Solution(initialSolution);
        bestSol.recalculate();
        int nbIterations = 0;

        for(int i = 0; i < maxIterations; i++){
            Neighbor bestNeighbour = new Neighbor();
            ArrayList<Neighbor> neighbourhood = bestNeighbour.getNeighbours(currentSol);
            if(neighbourhood.size() == 0){
                break;
            }
            // Removing the solutions being in the Tabu list
            for(int k = 0; k < neighbourhood.size(); k++){
                Neighbor currentNeighbor = neighbourhood.get(k);
                nbIterations += currentNeighbor.permutationDone.nbIteration;
                if(forbiddenPermutations.contains(currentNeighbor.permutationDone)){
                    // System.out.println("element deleted");
                    neighbourhood.remove(currentNeighbor);
                }
            }
            // Finding the best neighbor
            for(Neighbor currentNeighbor : neighbourhood){
                if(currentNeighbor.solution.fitness < bestNeighbour.solution.fitness){
                    bestNeighbour = currentNeighbor;
                }
            }

            // If we worsen the fitness
            if(bestNeighbour.solution.fitness >= currentSol.fitness){
                // Adding the forbidden permutation
                forbiddenPermutations.add(bestNeighbour.permutationDone);

                // If the Tabu list is full, we delete the first element
                if(forbiddenPermutations.size() > maxTabuSize){
                    forbiddenPermutations.remove(0);
                }
            }
            if(bestNeighbour.solution.fitness < bestSol.fitness){
                bestSol = new Solution(bestNeighbour.solution);
            }
            currentSol = bestNeighbour.solution;
            // System.out.println("Iteration " + i + " : " + currentSol.fitness+ " operator used : " + bestNeighbour.permutationDone.operatorUsed);

        }
        Object[] result = new Object[2];
        result[0] = bestSol;
        result[1] = nbIterations;
        // System.out.println(nbIterations);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tabu tabu = (Tabu) o;
        return maxIterations == tabu.maxIterations && maxTabuSize == tabu.maxTabuSize && Objects.equals(forbiddenPermutations, tabu.forbiddenPermutations) && Objects.equals(visitedStates, tabu.visitedStates) && Objects.equals(initialSolution, tabu.initialSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forbiddenPermutations, visitedStates, initialSolution, maxIterations, maxTabuSize);
    }
}
