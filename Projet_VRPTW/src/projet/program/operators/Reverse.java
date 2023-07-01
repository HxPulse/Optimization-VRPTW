package projet.program.operators;

import projet.program.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reverse {
    public Solution solution;
    public Reverse(Solution solution) {
        this.solution = solution;
    }
    // A reverse takes a route and reverses all the clients within this route
    public Neighbor getNeighbor() {
        Solution bestSol = new Solution(solution);
        Solution currentSol = new Solution(solution);
        List<Client> modifiedRoutes = new ArrayList<>();
        bestSol.fitness = Double.POSITIVE_INFINITY;
        int nbIteration = 0;
        Route modifiedRoute = new Route();

        for(int i = 0; i < currentSol.routes.size(); i++){
            List<Client> clients = currentSol.routes.get(i).route;
            List<Client> newClients = new ArrayList<>(clients);

            newClients.remove(0);
            newClients.remove(newClients.size()-1);

            Collections.reverse(newClients);
            newClients.add(0, clients.get(0));
            newClients.add(clients.get(clients.size()-1));

            Route reversedRoute = new Route((ArrayList<Client>) newClients);

            reversedRoute.calculateDistance();
            if(!reversedRoute.isValid()){
                continue;
            }
            currentSol.routes.set(i, reversedRoute);
            currentSol.recalculate();
            if(currentSol.fitness < bestSol.fitness){
                bestSol = new Solution(currentSol);
                modifiedRoutes.clear();
                modifiedRoutes.addAll(modifiedRoute.route);
            }
        }
        Permutation permutation = new Permutation(modifiedRoutes,"reverse", nbIteration);
        Neighbor n  = new Neighbor(bestSol, permutation);

        return  n;
    }
}
