package projet.program.models;

import java.awt.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Graph extends JPanel {

    private final List<Color> colors;
    private Solution currentSol;

    public Graph(Solution solution) {
        this.colors = Arrays.asList(
                new Color(0,180,255),   // Very Light Blue
                new Color(0,0,230),     // Dark Blue
                new Color(0,152,0),     // Dark Green
                new Color(255,180,0),   // Gold
                new Color(255,0,0),     // Orange
                new Color(200, 0, 208), // Violet
                new Color(135,136,136)  // Red,
        );
        this.currentSol = solution;
        setPreferredSize(new Dimension(1000, 1000));
    }

    public void initialise(){
        // Creation of the graph using JFrame
        JFrame frame = new JFrame("Graph with paths");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void changeSolution(Solution solution, boolean playSound) {
        // Changing the graph whenever we find a new solution
        this.currentSol = solution;
        this.initialise();
        if (playSound) {
            this.play("Data/Duck.wav");
        }
        repaint(); // Redraws the graph using the new paths
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int scale = 12;
        int j = 0;

        // Drawing of each path using a different color
        for(Route currentPath : currentSol.routes){

            // Choosing the color representing the path
            Color color = colors.get(j% colors.size());
            currentPath.calculateDistance();

            // We're drawing each vertex and its edges

            for(int i = 0; i < currentPath.route.size() - 1; i++){
                // Retrieving currentClient's coords
                int x1 = currentPath.route.get(i).x * scale;
                int y1 = currentPath.route.get(i).y * scale;
                // As long as it isn't the last client of the route, we connect it to the next client
                int x2 = currentPath.route.get(i+1).x * scale;
                int y2 = currentPath.route.get(i+1).y * scale;
                // When we're in the middle of the route, we display the name of the path
                if(i == (currentPath.route.size() - 1) / 2){
                    g.setColor(Color.BLACK);
                    g.drawString("Route : " + currentSol.routes.indexOf(currentPath) + "\n\n Tt Dist : " + (int)(currentPath.totalRouteDistance), (x1 + x2) / 2, (y1 + y2) / 2);
                }
                g.setColor(color);
                g.drawLine(x1, y1, x2, y2);
                g.setColor(Color.BLACK);
                if(i==0){g.setColor(Color.RED);}
                g.drawString("" + i,x1 - 10, y1 - 10);
                g.fillOval(x1 - 6, y1 - 6, 12, 12);
            }
            j+=3;
        }
    }

    public void play(String filePath) {
        // #4Fun, play a sound whenever a new solution is drawn
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing audio file: " + e.getMessage());
        }
    }
}
