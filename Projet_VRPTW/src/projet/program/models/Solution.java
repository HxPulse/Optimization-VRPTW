package projet.program.models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Solution {

    public ArrayList<Route> routes;
    public double fitness;

    public Solution(ArrayList<Route> givenRoutes) {
        this.routes = new ArrayList<>(givenRoutes);
        this.fitness = 0;

        for (Route r : givenRoutes) {
            this.fitness += r.totalRouteDistance;
        }
    }

    public Solution(Solution s) {
        this.routes = new ArrayList<>(s.routes);
        this.fitness = s.fitness;
    }

    public Solution() {
        this(new ArrayList<>());
    }

    public void reset() {
        // Removes all the routes in the solution as well as the fitness of the solution
        this.routes = new ArrayList<>();
        this.fitness = Double.POSITIVE_INFINITY;
    }

    public Double getTotSolDst() {
        return this.fitness;
    }

    public Route getRandomRoute() {
        // Returns a random route within  the solution
        Random rand = new Random();
        int index = rand.nextInt(this.routes.size());
        return routes.get(index);
    }

    public void addClient(Client chosenOne, int maxQuantity) {
        // Adds a said client to a route in the solution as long as it doesn't overpass the maxQuantity of the route
        boolean hasBeenAdded = false;
        int indexOfRoute = -1;
        int indexOfClient = -1;
        for (Route r : this.routes) {
            if (hasBeenAdded) {
                break;
            }
            for (int i = 0; i < r.route.size() - 1; i++) {
                if (hasBeenAdded) {
                    break;
                }
                Client before = r.route.get(i);
                Client next = r.route.get(i + 1);
                double distanceBefore = chosenOne.getDistance(before);
                double distanceNext = chosenOne.getDistance(next);
                double timeReady = before.dueTime + before.service + distanceBefore;
                double timeLeaving = -1;
                if (timeReady <= chosenOne.readyTime || timeReady <= chosenOne.dueTime) {
                    if (timeReady <= chosenOne.readyTime) {
                        timeLeaving = chosenOne.readyTime + chosenOne.service;
                    } else {
                        timeLeaving = timeReady + chosenOne.service;
                    }
                } else {
                    continue;
                }
                if (timeLeaving + distanceNext <= next.readyTime) {
                    indexOfRoute = this.routes.indexOf(r);
                    indexOfClient = i;
                    hasBeenAdded = true;
                }
            }
        }
        if (hasBeenAdded) {
            int demand = 0;
            for (Client c : this.routes.get(indexOfRoute).route) {
                demand += c.demand;
            }
            if (demand + chosenOne.demand <= maxQuantity) {
                this.routes.get(indexOfRoute).route.add(indexOfClient + 1, chosenOne);
            } else {
                hasBeenAdded = false;
            }
        }
        if (!hasBeenAdded) {
            Route r1 = new Route();
            ArrayList<Client> deposits = this.routes.get(0).route;
            r1.route.add(deposits.get(0));
            r1.route.add(chosenOne);
            r1.route.add(deposits.get(deposits.size() - 1));
            this.routes.add(r1);
        }
    }

    public ArrayList<Client> allClients() {
        // Retrieves all the clients of the solution
        ArrayList<Client> result = new ArrayList<>();
        for (Route r : this.routes) {
            for (Client c : r.route) {
                if (c.id.equals("d1")) {
                    continue;
                }
                result.add(c);
            }
        }
        return result;
    }

    public void recalculate() {
        // Makes sure the fitness the solution is well computed
        boolean val = this.isValid();
        this.fitness = 0;
        ArrayList<Route> routesToRemove = new ArrayList<>();
        for (Route r : this.routes) {
            if (r.route.size() <= 2) {
                routesToRemove.add(r);
            } else {
                r.calculateDistance();
                this.fitness += r.totalRouteDistance;
            }
        }
        for (Route r2 : routesToRemove) {
            this.routes.remove(r2);
        }
    }

    public void exchange(Client one, Client two) {
        // Exchanges two clients from a solution
        for (Route r : this.routes) {

            if (r.route.contains(one) && r.route.contains(two)) {
                // System.out.println("Same Route : " + one.id + " " + two.id);
                int indexOne = r.route.indexOf(one);
                int indexTwo = r.route.indexOf(two);
                if (indexOne < indexTwo) {
                    r.route.remove(one);
                    r.route.remove(two);
                    r.route.add(indexOne, two);
                    r.route.add(indexTwo, one);
                }
                if (indexOne > indexTwo) {
                    r.route.remove(one);
                    r.route.remove(two);
                    r.route.add(indexTwo, one);
                    r.route.add(indexOne, two);
                }
                break;
            }
            if (r.route.contains(one)) {
                int indexOne = r.route.indexOf(one);
                r.route.remove(one);
                r.route.add(indexOne, two);
                continue;
            }
            if (r.route.contains(two)) {
                int indexTwo = r.route.indexOf(two);
                r.route.remove(two);
                r.route.add(indexTwo, one);
            }
        }
        // System.out.println("Successfully exchanged " + one.id + " " + two.id);
    }


    public void relocate(Client one, Client two) {
        // Relocates client one after client two
        boolean checked = false;
        for (Route r : this.routes) {

            if (r.route.contains(one) && r.route.contains(two)) {
                int indexTwo = r.route.indexOf(two);
                r.route.remove(one);
                r.route.add(indexTwo, one);
                break;
            }

            if (r.route.contains(one) && !checked) {
                r.route.remove(one);
                checked = true;
            }

            if (r.route.contains(two)) {
                int indexTwo = r.route.indexOf(two);
                r.route.add(indexTwo + 1, one);
                continue;
            }
        }
    }

    public void move(double dueTime) {
        // Takes a random client in the solution and puts it alone in a new route
        Random rand = new Random();
        int indexRoute = rand.nextInt(this.routes.size());
        int indexClient = rand.nextInt(1, this.routes.get(indexRoute).route.size() - 1);
        Client clientMoved = this.routes.get(indexRoute).route.get(indexClient);
        System.out.println(clientMoved.id);
        this.routes.get(indexRoute).route.remove(indexClient);
        Route newRoute = new Route();
        Client deposit1 = new Client("d1", 0, 0, 0.0, 0.0, 0.0, 0, 0, true);
        Client deposit2 = new Client("d1", 0, 0, 0.0, dueTime, 240.0, 0, 0, true);
        newRoute.route.add(deposit1);
        newRoute.route.add(clientMoved);
        newRoute.route.add(deposit2);
        for (Client i : newRoute.route) {
            System.out.println(i.id);
        }
        this.routes.add(newRoute);
        this.recalculate();
    }

    public boolean isValid() {
        // Returns a boolean indicating wether the solution is valid or not (if every route is valid)
        for (Route r : this.routes) {
            if (!r.isValid()) {
                return false;
            }
        }
        return true;
    }

    public void output() {
        // Takes a solution and displays it nicely in the output.txt file
        // This function doesn't overwrite what's already in the file
        String fileName = "Data/output.txt";
        try {
            FileWriter writer = new FileWriter(fileName, true);
            String tSD = Double.toString(this.fitness);
            writer.write("Total Solution Distance : " + tSD + "\n");
            int i = 0;
            for (Route r : this.routes) {
                i++;
                String strI = Integer.toString(i);
                writer.write("----------------------------------------------------------\n");
                writer.write("Route #" + strI + "\n");
                writer.write("----------------------------------------------------------\n");
                for (Client c : r.route) {
                    int rdyI = (int) Math.round(c.readyTime);
                    String rdy = Integer.toString(rdyI);
                    int srvI = (int) Math.round(c.servedTime);
                    String srv = Integer.toString(srvI);
                    int dueI = (int) Math.round(c.dueTime);
                    String due = Integer.toString(dueI);
                    String dmd = Integer.toString(c.demand);
                    writer.write(c.id + " " + rdy + " " + srv + " " + due + " " + dmd + "\n");
                }
            }
            writer.write("\n");
            writer.write("\n");
            writer.write("\n");
            writer.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public double getFitness() {
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return Double.compare(solution.fitness, fitness) == 0 && Objects.equals(routes, solution.routes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routes, fitness);
    }
}
