package projet.program.operators;

import projet.program.models.*;

import java.util.*;

public class TwoOpt {
    public Solution initialSolution;

    public TwoOpt(Solution solution) {
        this.initialSolution = solution;
    }
    // 2-Opt works as a double exchange between 2 sets of 2 clients
    public Neighbor getNeighbor() {
        Random random = new Random();
        Solution bestSol = new Solution(initialSolution);
        double bestFitness = Double.POSITIVE_INFINITY;
        int nbIteration = 0;

        Solution currentSol = new Solution(initialSolution);
        List<Client> modifiedClients = new ArrayList<>();

        for(Route currentRoute : currentSol.routes) {
            if(currentRoute.route.size() <=3) {continue;}
            int indexStart = random.nextInt(1,currentRoute.route.size()-2);
            int indexEnd = random.nextInt(indexStart+1,currentRoute.route.size()-1);

            // Reversing the clients between two indexes
            Route testRoute = new Route(currentRoute.route);
            while(indexStart < indexEnd) {
                Client temp = testRoute.route.get(indexStart);
                modifiedClients.add(temp);
                testRoute.route.set(indexStart, testRoute.route.get(indexEnd));
                testRoute.route.set(indexEnd, temp);
                indexStart++;
                indexEnd--;
            }
            testRoute.calculateDistance();

            // If the new route is valid, we replace it within the solution
            if(testRoute.isValid()){
                nbIteration++;
                currentSol.routes.set(currentSol.routes.indexOf(currentRoute), testRoute);
                currentSol.recalculate();
                if(currentSol.fitness < bestFitness) {
                    bestFitness = currentSol.fitness;
                    bestSol = new Solution(currentSol);
                    modifiedClients.clear();
                }
            }
            // Since we're testing a modified route, we reset the solution we've just edited using the initial solution
            currentSol = new Solution(initialSolution);
        }
        Neighbor neighbor = new Neighbor(bestSol, new Permutation(modifiedClients, "2opt", nbIteration));
        return neighbor;
    }
}
