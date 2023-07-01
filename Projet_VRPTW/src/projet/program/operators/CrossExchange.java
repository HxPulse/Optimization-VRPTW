package projet.program.operators;

import projet.program.models.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CrossExchange {

    public Solution solution;

    public CrossExchange(Solution solution) {
        this.solution = solution;
    }
    // A cross exchange takes two different sets of clients and exchanges them within the solution
    public Neighbor getNeighbor(){
        Solution bestSol = new Solution(solution);
        bestSol.fitness = Double.POSITIVE_INFINITY;
        Solution currentSol = new Solution(solution);
        currentSol.recalculate();
        List<Client> modifiedRoutes = new ArrayList<>();
        int nbIteration = 0;

        for(int i = 0; i < currentSol.routes.size(); i++){
            for(int j = i+1; j < currentSol.routes.size(); j++){
                Route route1 = new Route(currentSol.routes.get(i));
                Route route2 = new Route(currentSol.routes.get(j));

                Random random = new Random();
                int indexStart = random.nextInt(Math.min(route1.route.size()-1, route2.route.size()-1))+1;

                LinkedList<Client> currentClients1 = new LinkedList<>(route1.route);
                LinkedList<Client> crossedClients1 = new LinkedList<>();
                LinkedList<Client> currentClients2 = new LinkedList<>(route2.route);
                LinkedList<Client> crossedClients2 = new LinkedList<>();

                // Creating the new array used for the exchange
                Client currentClient = new Client();
                for(int k = indexStart; k < route1.route.size(); k++){
                    currentClient = route1.route.get(k);
                    crossedClients1.add(currentClient);
                    currentClients1.remove(currentClient);
                }
                for(int k = indexStart; k < route2.route.size(); k++){
                    currentClient = route2.route.get(k);
                    crossedClients2.add(currentClient);
                    currentClients2.remove(currentClient);
                }
                // Adding to the array each exchanged element from the other one
                currentClients1.addAll(indexStart, crossedClients2);
                currentClients2.addAll(indexStart, crossedClients1);
                route1.route = new ArrayList<>(currentClients1);
                route2.route = new ArrayList<>(currentClients2);

                if(route1.isValid() && route2.isValid()){
                    currentSol.routes.set(i, route1);
                    currentSol.routes.set(j, route2);
                    currentSol.recalculate();
                    nbIteration++;
                    if(currentSol.fitness < bestSol.fitness){
                        bestSol = new Solution(currentSol);
                        modifiedRoutes.clear();
                        modifiedRoutes.addAll(crossedClients1);
                        modifiedRoutes.addAll(crossedClients2);
                    }
                }
                currentSol = new Solution(solution);
            }
        }
        Permutation permutation = new Permutation(modifiedRoutes, "crossExchange", nbIteration);
        Neighbor n = new Neighbor(bestSol, permutation);
        return n;
    }
}
