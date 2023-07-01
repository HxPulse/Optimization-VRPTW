package projet.program.operators;

import projet.program.models.*;

import java.util.ArrayList;
import java.util.List;

public class Exchange {

    public Solution solution;
    public Exchange(Solution solution) {
        this.solution = solution;
    }
    // An exchange takes two different clients within a solution and exchanges them
    public Neighbor getNeighbor(){
        ArrayList<Client> clients = (ArrayList<Client>) solution.allClients().clone();
        double bestFitness = Double.POSITIVE_INFINITY;
        List<Client> permutations = new ArrayList<>();
        Solution bestSol = solution;
        int nbIteration = 0;
        int i = 0;
        while (i < 98) {
            Client ci = clients.get(i);
            int j = i + 1;
            while (j < 99) {
                Client cj = clients.get(j);

                Solution exchangeTry = new Solution();
                ArrayList<Route> routes = new ArrayList<>(solution.routes.size());
                for (Route r : solution.routes) {
                    Route newRoute = new Route();
                    newRoute.route.addAll(r.route);
                    routes.add(newRoute);
                }
                exchangeTry.routes = routes;

                double tSD = solution.fitness;
                exchangeTry.fitness = tSD;
                exchangeTry.exchange(ci, cj);
                if (exchangeTry.isValid()) {
                    nbIteration++;
                    exchangeTry.recalculate();
                    if (exchangeTry.fitness < bestFitness) {
                        bestFitness = exchangeTry.fitness;
                        bestSol = exchangeTry;
                        permutations.add(ci);
                        permutations.add(cj);
                        //System.out.println("EXCHANGE NEW SOLUTION ! New Total Distance of " + this.bestSol.totalSolutionDistance);
                    }
                }
                j++;
            }
            i++;
        }
        Permutation permutationDone = new Permutation(permutations, "exchange", nbIteration);
        Neighbor n = new Neighbor(bestSol, permutationDone );

        return n;
    }
}
