package projet.program.operators;

import projet.program.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Move {
    public Solution initialSolution;

    public Move(Solution solution) {
        this.initialSolution = solution;
    }
    // A move takes a random client a puts it all alone in a new route within the solution
    public Neighbor getNeighbor() {
        Solution bestSolution = new Solution(initialSolution);
        bestSolution.fitness = Double.POSITIVE_INFINITY;
        Solution currentSolution = new Solution(initialSolution);
        Client finalClientMoved = new Client();
        int nbIteration = 0;
        // We make about 30 move tests and we keep the best one, this value can be increased if needed
        for(int i = 0; i < 30; i++) {
            Random random = new Random();
            // Generating the index of the route and client to move
            int indexRoute = random.nextInt(currentSolution.routes.size());
            if (currentSolution.routes.get(indexRoute).route.size() <= 3) {
                continue;
            }
            int indexClient = random.nextInt(1, currentSolution.routes.get(indexRoute).route.size() - 1);

            /// We retrieve the client to move and remove it from its initial route
            Client clientMoved = currentSolution.routes.get(indexRoute).route.get(indexClient);
            currentSolution.routes.get(indexRoute).route.remove(indexClient);

            // Creating the new route
            Route route = new Route();
            Client deposit1 = currentSolution.routes.get(indexRoute).route.get(0);
            Client deposit2 = currentSolution.routes.get(indexRoute).route.get(currentSolution.routes.get(indexRoute).route.size() - 1);
            route.route.add(deposit1);
            route.route.add(clientMoved);
            route.route.add(deposit2);
            currentSolution.routes.add(route);
            currentSolution.recalculate();
            nbIteration++;

            // If the new route is better than the bestSolution, we keep it
            if(currentSolution.fitness < bestSolution.fitness) {
                bestSolution = new Solution(currentSolution);
                finalClientMoved = clientMoved;
            }
            // Resetting the current solution
            currentSolution = new Solution(initialSolution);
        }

        List<Client> permutations = new ArrayList<>();
        permutations.add(finalClientMoved);
        Neighbor neighbor = new Neighbor(bestSolution, new Permutation(permutations, "move", nbIteration));
        return neighbor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(initialSolution, move.initialSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialSolution);
    }
}
