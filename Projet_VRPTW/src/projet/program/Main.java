package projet.program;


import projet.program.models.Client;
import projet.program.models.Graph;
import projet.program.models.Route;
import projet.program.models.Solution;
import projet.program.solver.VRPTW;

import java.util.ArrayList;

public class Main {

    // The data set to work must be given as the parameter of the getFile function (getFile("dataXXX"))

    // To try a code part, uncomment it

    // The .output() function inputs a Solution in the "Data/ouput.txt" file, watch out as this function overwrites in the file.
    // It is advised to empty it beforewise.

    public static void main(String[] args) {

//      //***************************************************
//      //      COMMENT 1 - Creating an optimized initial solution
//      //***************************************************

      /*VRPTW vrptw = new VRPTW();              // Algorithm initialization
      vrptw.getFile("data101");                 // File reading
      Solution s0 = new Solution();

      while(!vrptw.allClientsVisited()){        // While all clients aren't visited
          ArrayList<Client> clients = vrptw.constructRoute();
          Route r = new Route(clients);
          s0.routes.add(r);
      }
      s0.recalculate();
      s0.output();       */                     // Results found in the output.txt file


//      //***************************************************
//      //      COMMENT 2 - Creating a random initial solution
//      //***************************************************

      /*VRPTW vrptw = new VRPTW();              // Algorithm initialization
      vrptw.getFile("data101");                 // File reading
      Solution sol = vrptw.createRandomSolution();
      sol.recalculate();
      sol.output(); */                          // Results found in the output.txt file

//      //***************************************************
//      //      COMMENT 3 - Display of an algorithm process
//      //***************************************************

      VRPTW vrptw = new VRPTW();              // Algorithm initialization
      vrptw.getFile("data101");                 // File reading
      Solution s0 = new Solution();
      boolean surprise = true;                 // True for a surprise

      while(!vrptw.allClientsVisited()) {       // While all clients aren't visited
          ArrayList<Client> clients = vrptw.constructRoute();
          Route r = new Route(clients);
          s0.routes.add(r);
      }
      Graph p = new Graph(s0);
      p.initialise();
      s0.recalculate();
      vrptw.bestSol = s0;
      vrptw.exchange(p, surprise);
      vrptw.relocate(p, surprise);
      vrptw.exchange(p, surprise);
      vrptw.relocate(p, surprise);


//      //***************************************************
//      //      COMMENT 4 - Computing operators improvement rates
//      //***************************************************

      /*ArrayList<Double> initialDistances = new ArrayList<>();
      ArrayList<Double> distances1iteration = new ArrayList<>();
      ArrayList<Double> distances2iteration = new ArrayList<>();
      ArrayList<Double> distances3iteration = new ArrayList<>();

      for (int i = 0; i < 200; i++) {
        System.out.println("Computing... " + i/2 +"% done.");
        VRPTW vrptw = new VRPTW();                       // Algorithm initialization
        vrptw.getFile("data101");                        // File reading
        Solution sol = vrptw.createRandomSolution();
        sol.recalculate();
        double initialDistance = sol.getTotSolDst();
        sol = new Relocate(sol).getNeighbor().getSolution();
        double distance1iteration = sol.getTotSolDst();
        sol = new Exchange(sol).getNeighbor().getSolution();
        double distance2iteration = sol.getTotSolDst();
        sol = new Relocate(sol).getNeighbor().getSolution();
        double distance3iteration = sol.getTotSolDst();
        initialDistances.add(initialDistance);
        distances1iteration.add(1 - (distance1iteration / initialDistance));
        distances2iteration.add(1 - (distance2iteration / distance1iteration));
        distances3iteration.add(1 - (distance3iteration / distance2iteration));
      }

      double sum1 = 0;
      double sum2 = 0;
      double sum3 = 0;
      for (double j : distances1iteration) {sum1 += j;}
      for (double k : distances2iteration) {sum2 += k;}
      for (double l : distances3iteration) {sum3 += l;}
      sum1 /= 2;
      sum2 /= 2;
      sum3 /= 2;

      System.out.println(sum1);
      System.out.println(sum2);
      System.out.println(sum3); */


//      //***************************************************
//      //      COMMENT 5 - Computation of the amount of drivers needed
//      //***************************************************

      /*VRPTW vrptw = new VRPTW();              // Algorithm initialization
      vrptw.getFile("data101");                 // File reading
      Solution s0 = new Solution();
      while(!vrptw.allClientsVisited()){        // While all clients aren't visited
          ArrayList<Client> clients = vrptw.constructRoute();
          Route r = new Route(clients);
          s0.routes.add(r);
      }
      s0.recalculate();

      int solOpti = s0.routes.size();

      Relocate r = new Relocate(s0);
      s0 = r.getNeighbor().getSolution();
      Exchange e = new Exchange(s0);
      s0 = e.getNeighbor().getSolution();
      Relocate r2 = new Relocate(s0);
      s0 = r2.getNeighbor().getSolution();
      Exchange e2 = new Exchange(s0);
      s0 = e2.getNeighbor().getSolution();

      Double random = 0.0;
      Double randomRER = 0.0;
      Double randomERE = 0.0;

      for (int i = 0; i < 100; i++) {
          System.out.println(i);
          VRPTW vrptw2 = new VRPTW();
          vrptw2.getFile("data101");
          Solution sol = vrptw2.createRandomSolution();
          sol.recalculate();
          random += sol.routes.size();
          Solution sol2 = new Relocate(sol).getNeighbor().getSolution();
          sol2 = new Exchange(sol2).getNeighbor().getSolution();
          sol2 = new Relocate(sol2).getNeighbor().getSolution();
          randomRER += sol2.routes.size();

          VRPTW vrptw3 = new VRPTW();
          vrptw3.getFile("data101");
          Solution solBis = vrptw3.createRandomSolution();
          solBis.recalculate();
          Solution sol3 = new Relocate(solBis).getNeighbor().getSolution();
          sol3 = new Exchange(sol3).getNeighbor().getSolution();
          sol3 = new Relocate(sol3).getNeighbor().getSolution();
          randomERE += sol3.routes.size();
      }
      System.out.println("Smart solution : " + solOpti);
      System.out.println("Smart & optimized solution : " + s0.routes.size());
      System.out.println("Random solution : " + random/100);
      System.out.println("RER random solution : " + randomRER/100);
      System.out.println("ERE random solution : " + randomERE/100); */


//      //***************************************************
//      //      COMMENT 6 - Execution time computing
//      //***************************************************

      /*long startTime = System.nanoTime();
      for (int i = 0; i < 200; i++) {
          VRPTW vrptw = new VRPTW();              // Algorithm initialization
          vrptw.getFile("data101");               // File reading
          Solution s0 = vrptw.createRandomSolution();
          s0.recalculate();
          s0 = new Exchange(s0).getNeighbor().getSolution();        // We can replace exchange by relocate or add other operators
      }
      long endTime = System.nanoTime();
      long executionTime = (endTime - startTime) / 200000000;
      System.out.println("Execution time : " + executionTime + " ms"); */


//      //***************************************************
//      //      COMMENT 7 - Improvement of the solution through time
//      //***************************************************
        /*ArrayList<Long> time1  = new ArrayList<>();
        ArrayList<Double> dist1 = new ArrayList<>();
        ArrayList<Long> time2  = new ArrayList<>();
        ArrayList<Double> dist2 = new ArrayList<>();

        for (int i = 0; i < 200; i++){
            System.out.println("Computing..." + i/2 + "%");
            VRPTW vrptw = new VRPTW();                      // Algorithm initialization
            vrptw.getFile("data101");                       // File reading
            Solution sol = vrptw.createRandomSolution();
            sol.recalculate();
            Relocate r = new Relocate(sol);
            Exchange e = new Exchange(sol);
            double initialDistance = sol.fitness;

            long startTime = System.nanoTime();

            sol = r.getNeighbor().getSolution();
            long checkTime1 = System.nanoTime();
            time1.add(checkTime1 - startTime);
            dist1.add(sol.fitness / initialDistance * 100);

            sol = e.getNeighbor().getSolution();
            long checkTime2 = System.nanoTime();
            time2.add(checkTime2 - startTime);
            dist2.add(sol.fitness / initialDistance * 100);
        }

        long sum = 0;
        for (long value : time1) {sum += value;}
        double average = (double) sum / time1.size();
        double sumBis = 0;
        for (double valueBis : dist1) {sumBis += valueBis;}
        double averageBis = 100 - (sumBis / dist1.size());

        long sum2 = 0;
        for (long value2 : time2) {sum2 += value2;}
        double average2 = (double) sum2 / time2.size();
        double sumBis2 = 0;
        for (double valueBis2 : dist2) {sumBis2 += valueBis2;}
        double averageBis2 = 100 - (sumBis2 / dist2.size());

        System.out.println("Iteration 1 Average : " + average + " % to initial " + averageBis);
        System.out.println("Iteration 2 Average : " + average2 + " % to initial " + averageBis2);*/

//      //***************************************************
//      //      COMMENT 8 - Tabu method testing over all data sets
//      //      We will be using threads to lower the execution time
//      //***************************************************

        /*ArrayList<String> files = new ArrayList<>();
        files.add("data101");
        files.add("data102");
        files.add("data111");
        files.add("data112");
        files.add("data201");
        files.add("data202");
        files.add("data1101");
        files.add("data1102");
        files.add("data1201");
        files.add("data1202");
        for(String file : files) {
                long startTime = System.currentTimeMillis();
                VRPTW vrptw = new VRPTW();  // Initialisation de l'algo
                vrptw.getFile("data1202");        // Lecture du fichier
                int nbrIterations = 300;
                AtomicReference<Double> bestFitness = new AtomicReference<>(Double.POSITIVE_INFINITY);
                AtomicReference<Double> avrgFitness = new AtomicReference<>(0.0);
                AtomicReference<Solution> bestSolOverall = new AtomicReference<>(null);
                AtomicReference<Integer> iteration = new AtomicReference<>(0);

                // Creating a thread array to store them all
                Thread[] threads = new Thread[nbrIterations];

                for (int i = 0; i < nbrIterations; i++) {
                    threads[i] = new Thread(() -> {
                        Solution sol = vrptw.createRandomSolution();
                        sol.recalculate();
                        Tabu tabu = new Tabu(130, 45, sol);
                        Object[] res = tabu.solve();
                        Solution bestSol = (Solution) res[0];
                        int nbiteration = (int) res[1];

                        //System.out.println("Initial solution (iteration " + iteration + "): " + sol.fitness);
                        //System.out.println("Final solution (iteration " + iteration + "): " + bestSol.fitness);

                        synchronized (vrptw) {
                            avrgFitness.set(avrgFitness.get() + bestSol.fitness);
                            iteration.set(iteration.get() + nbiteration);
                            if (bestSol.fitness < bestFitness.get()) {
                                bestFitness.set(bestSol.fitness);
                                bestSolOverall.set(bestSol);
                            }
                        }
                    });
                    threads[i].start();
                }

                // Waiting for all threads execution

                for (int i = 0; i < nbrIterations; i++) {
                    try {
                        threads[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                long endTime = System.currentTimeMillis();
                long executionTime = (endTime - startTime)/1000;
                System.out.println("Execution time : " + executionTime + " ms");
                System.out.println("Best fitness: " + bestFitness.get());
                System.out.println("Average fitness: " + avrgFitness.get() / nbrIterations);
                System.out.println("Average solution created: " + iteration.get() / nbrIterations);
                bestSolOverall.get().output();
        } */

//      //***************************************************
//      //      COMMENT 9 - Counting the solutions we've created
//      //***************************************************

        /*int attempts = 140;
        int totalHC_2O = 0;
        int totalHC_3O = 0;
        int totalHC_4O = 0;
        int totalTabu = 0;

        for (int i = 0; i < attempts; i++) {
            System.out.println(i);
            VRPTW vrptw = new VRPTW();
            vrptw.getFile("data1202");
            int nbrSolHC_2Operators = 0;
            int nbrSolHC_3Operators = 0;
            int nbrSolHC_4Operatos = 0;
            Solution solHC = vrptw.createRandomSolution();
            solHC.recalculate();
            Neighbor n = new Relocate(solHC).getNeighbor();
            nbrSolHC_2Operators += n.permutationDone.nbIteration;
            nbrSolHC_3Operators += n.permutationDone.nbIteration;
            nbrSolHC_4Operators += n.permutationDone.nbIteration;
            solHC = n.getSolution();
            Neighbor n2 = new Exchange(solHC).getNeighbor();
            nbrSolHC_2Operators += n2.permutationDone.nbIteration;
            nbrSolHC_3Operators += n2.permutationDone.nbIteration;
            nbrSolHC_4Operators += n2.permutationDone.nbIteration;
            solHC = n2.getSolution();
            Neighbor n3 = new Relocate(solHC).getNeighbor();
            nbrSolHC_3Operators += n3.permutationDone.nbIteration;
            nbrSolHC_4Operators += n3.permutationDone.nbIteration;
            solHC = n3.getSolution();
            Neighbor n4 = new Exchange(solHC).getNeighbor();
            nbrSolHC_4Operators += n4.permutationDone.nbIteration;

            totalHC_2O += nbrSolHC_2Operators;
            totalHC_3O += nbrSolHC_3Operators;
            totalHC_4O += nbrSolHC_4Operators;

            Solution solTabu = vrptw.createRandomSolution();
            solTabu.recalculate();
            Tabu tabu = new Tabu(130, 10, solTabu);
            Object[] res = tabu.solve();
            totalTabu += (int) res[1];
        }
        totalHC_2O /= attempts;
        totalHC_3O /= attempts;
        totalHC_4O /= attempts;
        totalTabu /= attempts;
        System.out.println("HC 2O : " + totalHC_2O);
        System.out.println("HC 3O : " + totalHC_3O);
        System.out.println("HC 4O : " + totalHC_4O);
        System.out.println("Tabu : " + totalTabu);*/

//      //***************************************************

        System.out.println("Program completed");
    }
}
