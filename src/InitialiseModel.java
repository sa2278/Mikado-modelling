import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class InitialiseModel extends JPanel {
    public static final int RAYS_NUM = 200;
    public static final Color POINT_COLOR = Color.RED;
    public static final Color LINE_COLOR_DRAWN = Color.MAGENTA;
    public static final Color LINE_COLOR_UNDRAWN = Color.GREEN;
    public static final Color OBJECT_COLOR = Color.BLACK;
    public static final int OBJECT_NUM = 4;
    int objectRadius = 50;
    ArrayList<RayPaths> rays = new ArrayList<>();
    ArrayList<Point> objects = new ArrayList<>();

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        double padding = 10;
        ArrayList<Point> points = new ArrayList<>();
        double radius = (double) Math.min(this.getWidth(), this.getHeight()) / 2 - padding * 2;
        g2.draw(new Ellipse2D.Double(padding, padding, radius * 2, radius * 2));
        g2.setColor(POINT_COLOR);



        double currentSect = 0;

        for (int i = 0; i < OBJECT_NUM; i++) {
            double xObject = 0;
            double yObject = 0;

            // the new objects must not exist outside of the defined area, so the object diameter is subtracted
            double angle = getRandomNumber(currentSect, currentSect + 0.25) * 2 * Math.PI;
            double r = (radius - objectRadius - objectRadius) * Math.sqrt(Math.random());
            xObject = r * Math.cos(angle) + radius + padding;
            yObject = r * Math.sin(angle) + radius + padding;
            currentSect += 0.25;

            g2.setColor(OBJECT_COLOR);
            g2.draw(new Ellipse2D.Double(xObject - objectRadius, yObject - objectRadius, objectRadius * 2, objectRadius * 2));
            objects.add(new Point(xObject,yObject));
            // to update the objects, each is itterated through and updated
        }

        for (int i = 0; i < RAYS_NUM; i++) {
            Random rand = new Random();
            double angle = Math.random() * 2 * Math.PI ;
            double r = radius * Math.sqrt(Math.random());
            double x = r * Math.cos(angle) + radius + padding;
            double y = r * Math.sin(angle) + radius + padding;




            double angle2 = Math.random() * 2 * Math.PI ;
            Point start = new Point(x + 1000 * Math.cos(angle2), y + 1000 * Math.sin(angle2));
            Point end = new Point((x - 1000 * Math.cos(angle2)), (y - 1000* Math.sin(angle2)));
            // TODO remove this as it is for debug purposes
            // g2.setColor(POINT_COLOR);
            // g2.draw(new Ellipse2D.Double(x, y, 1, 1));
            // g2.setColor(LINE_COLOR_DRAWN);
            // g2.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
            Line2D temp = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
            RayPaths currentRay = (new RayPaths(start, end, Boolean.FALSE));
            for (int j = 0; j < OBJECT_NUM; j++){
                if(Math.abs(temp.ptLineDist(objects.get(j).getX(), objects.get(j).getY())) < objectRadius ){
                    currentRay.setDrawn(Boolean.FALSE);
                    break;
                }
                currentRay.setDrawn(Boolean.TRUE);
            }
            rays.add(currentRay);
            // if the ray overlaps a circle is drawn is false, change the colour.
            // TODO add a function that at the press of a key picks a random chord and flips it, and a way to determine step size

        }
        for (int k = 0; k < RAYS_NUM; k++) {
            RayPaths ray = rays.get(k);
            Line2D temp = new Line2D.Double(ray.startPoint.getX(), ray.getStartPoint().getY(), ray.getEndPoint().getX(), ray.getEndPoint().getY());
            if (ray.isDrawn == Boolean.TRUE){
                g2.setColor(LINE_COLOR_DRAWN);
                g2.draw(temp);
            }
            else{
                g2.setColor(LINE_COLOR_UNDRAWN);
                g2.draw(temp);
            }


        }


    }

    private boolean IsIntersecting(ArrayList<Point> circles, RayPaths ray){
        for (Point circle : circles){


        }
        return Boolean.TRUE;

    }

    private double getRandomNumber(double min, double max) {
        return (double) ((Math.random() * (max - min)) + min);
    }








}