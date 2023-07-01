package projet.program.operators;

import projet.program.models.*;

import java.util.ArrayList;
import java.util.List;

public class Relocate {
    public Solution solution;

    public Relocate(Solution solution) {
        this.solution = solution;
    }
    // A relocate takes a client and places it next to another one
    public Neighbor getNeighbor(){
        ArrayList<Client> clients = (ArrayList<Client>) solution.allClients().clone();
        double bestFitness = Double.POSITIVE_INFINITY;
        List<Client> modifiedClients = new ArrayList<>();
        Solution bestSol = solution;
        int nbIteration = 0;
        int k = 0;
        while (k < 98) {
            Client ck = clients.get(k);
            int l = 0;
            while (l < 99) {
                if (k == l) {
                    l++;
                    continue;
                }
                Client cl = clients.get(l);

                Solution relocateTry = new Solution();
                ArrayList<Route> routes3 = new ArrayList<>(solution.routes.size());
                for (Route r : solution.routes) {
                    Route newRoute3 = new Route();
                    newRoute3.route.addAll(r.route);
                    routes3.add(newRoute3);
                }
                relocateTry.routes = routes3;

                double tSD2 = solution.fitness;
                relocateTry.fitness = tSD2;
                relocateTry.relocate(ck, cl);

                if (!relocateTry.isValid()){
                    l++;
                    continue;
                }

                relocateTry.recalculate();
                nbIteration++;
                if (relocateTry.fitness < bestFitness) {
                    bestFitness = relocateTry.fitness;
                    bestSol = relocateTry;
                    modifiedClients.clear();
                    modifiedClients.add(ck);
                    // System.out.println("RELOCATE NEW SOLUTION ! New Total Distance of " + this.bestSol.totalSolutionDistance);
                }
                l++;
            }
            k++;
        }
        Permutation permutationDone = new Permutation(modifiedClients,"relocate", nbIteration);
        Neighbor n  = new Neighbor(bestSol, permutationDone);

        return n;
    }
}
