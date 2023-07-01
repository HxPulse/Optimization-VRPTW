package projet.program.models;

import java.util.Objects;

public class Client {

    public String id;
    public int x;
    public int y;
    public double readyTime;

    public double servedTime;
    public double dueTime;
    public int demand;
    public int service;
    public boolean visited;

    public Client(String id, int x, int y,  double readyTime, double servedTime, double dueTime, int demand, int service, boolean visited){
        this.id = id;
        this.x = x;
        this.y = y;
        this.readyTime = readyTime;
        this.servedTime = servedTime;
        this.dueTime = dueTime;
        this.demand = demand;
        this.service = service;
        this.visited = visited;
    }

    public Client(){
        this("test", 0, 0, 0, 0, 0, 0, 0, false);
    }

    public double getDistance(Client c) {
        // Computes the distance between between the instance of the client and another client
        int deltaX = Math.abs(this.x - c.x);
        int deltaY = Math.abs(this.y - c.y);
        int xSquared = deltaX * deltaX;             // Distance computing using Pythagoras
        int ySquared = deltaY * deltaY;
        return Math.sqrt(xSquared + ySquared);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return x == client.x && y == client.y && Double.compare(client.readyTime, readyTime) == 0 && Double.compare(client.servedTime, servedTime) == 0 && Double.compare(client.dueTime, dueTime) == 0 && demand == client.demand && service == client.service && visited == client.visited && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, readyTime, servedTime, dueTime, demand, service, visited);
    }
}
