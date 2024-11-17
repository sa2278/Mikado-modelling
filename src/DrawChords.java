import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;


import javax.swing.*;

public class DrawChords extends JPanel{
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        // Define circle parameters (550, 550)
        int centerX = 550;
        int centerY = 550;
        int radius = 200;

        // Draw the circle
        g2d.setColor(Color.RED);
        Ellipse2D.Double circle = new Ellipse2D.Double((double) getWidth() / (2 - radius), (double) getHeight() / (2 - radius), 2 * radius, 2 * radius);
        g2d.draw(circle);

        // Define points on the circumference
        ArrayList<Segment> segments = new ArrayList<>();
        Random rand = new Random();

        // Draw random points on the inside the circle (originally indicating 10000 lines)
        for (int n = 0; n < 200; n+=2) {
            // Generate a random angle between 0 and 2*PI
            double r = radius * Math.sqrt(rand.nextDouble(1));
            double angle = rand.nextDouble(1) * 2 * Math.PI;

            int x = (int)(centerX + r * Math.cos(angle));
            int y = (int)(centerY + r * Math.cos(angle));

            System.out.println("a and y");
            System.out.println(x);
            System.out.println(y);

            Point point = new Point(x, y);
            segments.add(new Segment(point, angle));


        }

        // Draw chords between points
        g.setColor(Color.BLACK);
        for (int i = 0; i < segments.size() - 1; i++) {
            Line2D line = getLine2D(segments, i);
            g2d.draw(line);

        }
    }

    private Line2D getLine2D(ArrayList<Segment> segments, int i) {
        Segment current = segments.get(i);
        Point start = current.drawSegment().get(0);
        Point end = current.drawSegment().get(1);

        double x1 = (double) getWidth() / (2 + start.getX());
        double y1 = (double) getHeight() / (2 + start.getY());
        double x2 = (double) getWidth() / (2 + end.getX());
        double y2 = (double) getHeight() / (2 + end.getY());
        return new Line2D.Double(x1, y1, x2, y2);

    }
}
