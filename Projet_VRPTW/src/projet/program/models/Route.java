package projet.program.models;

import java.util.ArrayList;
import java.util.Objects;

public class Route {

    public ArrayList<Client> route;
    public double totalRouteDistance;

    public Route(ArrayList<Client> clients){
        this.route = clients;
        this.totalRouteDistance = 0;
    }

    public Route(){
        this(new ArrayList<>());
    }

    public Route(Route r){
        this.route = new ArrayList<Client>(r.route);
        this.totalRouteDistance = r.totalRouteDistance;
    }

    public boolean isValid() {
        // Returns a boolean telling wether a Route is valid or not (we can go through all clients in the right order, with possible time delays)
        for(int i = 0; i < this.route.size() - 1; i++){
            Client current = this.route.get(i);
            Client next = this.route.get(i+1);
            double distance = current.getDistance(next);
            double driverReady = current.servedTime + current.service + distance;
            if(next.id.equals("d1")){
                if(this.route.indexOf(next) != this.route.size() - 1){
                    return false;
                }
                return (driverReady <= next.dueTime);
            }
            if(driverReady <= next.readyTime){
                next.servedTime = next.readyTime;
            } else if (driverReady <= next.dueTime) {
                next.servedTime = driverReady;
            } else {
                // System.out.println("Impossible route : " + current.id + " served at " + current.servedTime + " distance of " + distance + " from " + next.id + " ready from " + next.readyTime + " to " + next.dueTime);
                return false;
            }
        }
        return true;
    }

    public void calculateDistance() {
        // Returns the total distance of the route, i.e. the sum of all distances between two adjacent clients.
        this.totalRouteDistance = 0;
        for(int i = 0; i < route.size() - 1; i++){
            this.totalRouteDistance += route.get(i).getDistance(route.get(i+1));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route1 = (Route) o;
        return Double.compare(route1.totalRouteDistance, totalRouteDistance) == 0 && Objects.equals(route, route1.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, totalRouteDistance);
    }
}
