package projet.program.solver;

import projet.program.models.Client;
import projet.program.models.Graph;
import projet.program.models.Route;
import projet.program.models.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Thread.sleep;

public class VRPTW {

    public int nbDeposits = 0;
    public int nbClients = 0;
    public int maxQuantity = 0;
    public double currentTime = 0;
    public ArrayList<Client> deposits = new ArrayList<>();            // Variables initialization
    public ArrayList<Client> clients = new ArrayList<>();
    public Solution bestSol = new Solution();

    public void getFile(String data) {
        // Retrieving the data from the file
        String fileName = "Data/" + data + ".vrp";           // Data set to work on

        try {
            Scanner scanner = new Scanner(new File(fileName));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.startsWith("NB_DEPOTS")) {
                    this.nbDeposits = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("NB_CLIENTS")) {
                    this.nbClients = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("MAX_QUANTITY")) {
                    this.maxQuantity = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("DATA_DEPOTS")) {

                    line = scanner.nextLine();
                    String[] values = line.split(" ");
                    String idName = values[0];
                    int x = Integer.parseInt(values[1]);                // File reading + variable storage
                    int y = Integer.parseInt(values[2]);
                    Double readyTime = Double.parseDouble(values[3]);
                    Double dueTime = Double.parseDouble(values[4]);
                    Client deposit1 = new Client(idName, x, y, readyTime, 0, 0, 0, 0, false);
                    Client deposit2 = new Client(idName, x, y, readyTime, 0, dueTime, 0, 0, false);
                    this.deposits.add(deposit1);
                    this.deposits.add(deposit2);

                } else if (line.startsWith("DATA_CLIENTS")) {

                    for (int i = 0; i < this.nbClients; i++) {
                        line = scanner.nextLine();
                        String[] values = line.split(" ");

                        String idName = values[0];
                        int x = Integer.parseInt(values[1]);
                        int y = Integer.parseInt(values[2]);
                        Double readyTime = Double.parseDouble(values[3]);
                        Double dueTime = Double.parseDouble(values[4]);
                        int demand = Integer.parseInt(values[5]);
                        int service = Integer.parseInt(values[6]);

                        Client c = new Client(idName, x, y, readyTime, 0, dueTime, demand, service, false);
                        this.clients.add(c);
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }

    public ArrayList<Client> constructRoute() {
        // Creating a slightly optimized and valid route
        ArrayList<Client> route = new ArrayList<>();
        route.add(deposits.get(0));
        int demand = 0;

        while(demand <= this.maxQuantity){                                                   // While the driver's capacity isn't full
            Client lastDriverStop = route.get(route.size() - 1);                             // Retrieving the current client
            currentTime = lastDriverStop.servedTime + lastDriverStop.service;
            Client nextOne = findClosestClient(this.clients, lastDriverStop);                // Searching for the best potential client

            if (nextOne == lastDriverStop){     // If findClosestClient returns the closest client as being itself, then there isn't any client left to visit
                route.add(deposits.get(1));
                return route;
            }
            if(nextOne.demand + demand <= maxQuantity){      // If the driver's capacity allows the client's demand, then we're visiting it
                nextOne.visited = true;
                demand += nextOne.demand;
                route.add(nextOne);
                double distance = lastDriverStop.getDistance(nextOne);
                double driverAtNext = lastDriverStop.servedTime + lastDriverStop.service + distance;
                nextOne.servedTime = Math.max(driverAtNext, nextOne.readyTime);
            } else {
                break;
            }
        }
        route.add(deposits.get(1));
        return route;
    }

    public Client findClosestClient(ArrayList<Client> clients, Client currentPosition) {
        // Returns the closest client to the given client distance-wise
        if (allClientsVisited()) {
            return currentPosition;             // If we don't find any unvisited client, then we return the given client
        }
        Client bestClient = null;
        double bestDistance = Double.POSITIVE_INFINITY;     // bestDistance = infinite indicates wether we found a client or not

        for(Client client : clients){
            double distance = currentPosition.getDistance(client);      // Computing the distance between the current and the following client
            double driverReady = currentPosition.servedTime + currentPosition.service;
            if(!client.visited && driverReady + distance <= client.dueTime && distance <= bestDistance) {   // If we find a client and it isn't too late
                bestDistance = distance;                    // If we can visit the client in the timing interval and with a better delay than the current one.
                bestClient = client;                        // We keep track of this client
            }
        }
        if(bestDistance == Double.POSITIVE_INFINITY){
            return currentPosition;
        }
        return bestClient;
    }

    public boolean allClientsVisited(){
        // Returns the list of all the visited client
        for(Client i : this.clients) {
            if(!i.visited){
                return false;
            }
        }
        return true;
    }

    public void exchange(Graph currentGraph, boolean playSound){
        // Computes an instance of the exchange operator
        ArrayList<Client> clients = this.bestSol.allClients();

        int i = 0;
        while(i <= 98){
            Client ci = clients.get(i);
            int j = i + 1;
            while(j <= 99){
                Client cj = clients.get(j);

                Solution exchangeTry = new Solution();
                ArrayList<Route> routes = new ArrayList<>(this.bestSol.routes.size());
                for (Route r : this.bestSol.routes) {
                    Route newRoute = new Route();
                    newRoute.route.addAll(r.route);
                    routes.add(newRoute);
                }
                exchangeTry.routes = routes;

                double tSD = this.bestSol.fitness;
                exchangeTry.fitness = tSD;
                exchangeTry.exchange(ci, cj);
                if(exchangeTry.isValid()){
                    exchangeTry.recalculate();
                    if(exchangeTry.fitness < this.bestSol.fitness){
                        ArrayList<Route> routes2 = new ArrayList<>(exchangeTry.routes.size());
                        for (Route rE : exchangeTry.routes) {
                            Route newRoute2 = new Route();
                            newRoute2.route.addAll(rE.route);
                            routes2.add(newRoute2);
                        }
                        this.bestSol.routes = routes2;
                        this.bestSol.fitness = exchangeTry.fitness;
                        // System.out.println("EXCHANGE NEW SOLUTION ! New Total Distance of " + this.bestSol.totalSolutionDistance);
                        i = 0;

                        // Updating the graph
                        currentGraph.changeSolution(this.bestSol, playSound);
                        try {
                            sleep(50);      // This can be decreased to 0ms
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        // this.bestSol.output();       // Uncomment to recover the solution in the output.txt file
                    }
                }
                j++;
            }
            i++;
        }
    }

    public void relocate(Graph currentGraph, boolean playSound){
        // Computes an instance of the relocate operator
        clients = this.bestSol.allClients();
        int k = 0;
        while(k <= 99){
            Client ck = clients.get(k);
            int l = 0;
            while(l < 99){
                if(k == l){
                    l++;
                    continue;
                }
                Client cl = clients.get(l);

                Solution relocateTry = new Solution();
                ArrayList<Route> routes3 = new ArrayList<>(this.bestSol.routes.size());
                for (Route r : this.bestSol.routes){
                    Route newRoute3 = new Route();
                    newRoute3.route.addAll(r.route);
                    routes3.add(newRoute3);
                }
                relocateTry.routes = routes3;
                double tSD2 = this.bestSol.fitness;
                relocateTry.fitness = tSD2;
                relocateTry.relocate(ck, cl);
                if(relocateTry.isValid()){
                    relocateTry.recalculate();
                    // System.out.println("Valid Solution ! Distance calculated " + relocateTry.totalSolutionDistance);
                    if(relocateTry.fitness < this.bestSol.fitness){
                        ArrayList<Route> routes4 = new ArrayList<>(relocateTry.routes.size());
                        for (Route rT : relocateTry.routes){
                            Route newRoute4 = new Route();
                            newRoute4.route.addAll(rT.route);
                            routes4.add(newRoute4);
                        }
                        this.bestSol.routes = routes4;
                        this.bestSol.fitness = relocateTry.fitness;
                        // System.out.println("RELOCATE NEW SOLUTION ! New Total Distance of " + this.bestSol.totalSolutionDistance);
                        k = 0;

                        // Updating the graph
                        currentGraph.changeSolution(this.bestSol, playSound);
                        try {
                            sleep(50); // This can be decreased to 0ms
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        // this.bestSol.output();       // Uncomment to recover the solution in the output.txt file
                    }
                }
                l++;
            }
            k++;
        }
    }

    public Solution createRandomSolution() {
        // Returns a randomly generated but valid solution
        Solution result = new Solution();
        Route r = new Route();
        r.route.add(deposits.get(0));
        r.route.add(deposits.get(1));
        r.route.get(1).servedTime = r.route.get(1).dueTime;
        result.routes.add(r);

        ArrayList<Client> clientsLeft = new ArrayList<>();
        for (Client c : this.clients) {
            Client cli = new Client();
            cli.id = c.id;
            cli.x = c.x;
            cli.y = c.y;
            cli.readyTime = c.readyTime;
            cli.servedTime = c.dueTime;
            cli.dueTime = c.dueTime;
            cli.demand = c.demand;
            cli.service = c.service;
            clientsLeft.add(cli);
        }

        while(!clientsLeft.isEmpty()){
            Client chosenOne = randomClient(clientsLeft);
            result.addClient(chosenOne, this.maxQuantity);
            clientsLeft.remove(chosenOne);
        }
        return result;
    }

    public Client randomClient(ArrayList<Client> clientsLeft) {
        // Returns a random client within the given array
        Random rand = new Random();
        int index = rand.nextInt(clientsLeft.size());
        return clientsLeft.get(index);
    }

    public int driversMin() {
        // Returns the minimal amount of drivers to use by dividing the sum of all demands by the capacity of a driver
        int totalDemand = 0;
        for(Client c : this.clients){
            totalDemand += c.demand;
        }
        int nbDrivers = totalDemand / this.maxQuantity;
        int reste = totalDemand % this.maxQuantity;
        if(reste > 0){
            nbDrivers++;
        }
        return nbDrivers;
    }
}

