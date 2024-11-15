import java.awt.*;
import java.util.*;


import javax.swing.*;

public class DrawChords extends JPanel{
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Define circle parameters (550, 550)
        int centerX = 550;
        int centerY = 550;
        int radius = 200;

        // Draw the circle
        g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // Define points on the circumference
        ArrayList<Segment> segments = new ArrayList<>();
        Random rand = new Random();

        // Draw random points on the circumference (originally indicating 10000 lines)
        for (int n = 0; n < 20000; n+=2) {
            // Generate a random angle between 0 and 2*PI
            double angle = rand.nextDouble() * 2 * Math.PI;

            // from the random angle, find the biggest and smallest x and y values
            int highX = centerX + (int) (radius * Math.cos(angle));
            int lowX = centerX - (int) (radius * Math.cos(angle));

            int highY = centerY + (int) (radius * Math.sin(angle));
            int lowY = centerY - (int) (radius * Math.sin(angle));
            // Calculate the x and y coordinates of the point by picking a random x and y within the boundaries
            if (highX == lowX) {
                lowX += 1;
            }

            if (highY == lowY) {
                lowY += 1;
            }

            int x = rand.nextInt(Math.abs(highX - lowX)) + lowX;
            int y = rand.nextInt(Math.abs(highY - lowY)) + lowY;

            Point point = new Point(x, y);
            segments.add(new Segment(point, angle));


        }

        // Draw chords between points
        for (int i = 0; i < segments.size() - 1; i++) {
            Segment current = segments.get(i);
            Point start = current.drawSegment().get(0);
            Point end = current.drawSegment().get(1);
            g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());

        }
    }
}
