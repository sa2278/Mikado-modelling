import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class Model extends JPanel {
    public static final int RAYS_NUM = 1000;
    public static final Color POINT_COLOR = Color.RED;
    public static final Color LINE_COLOR_DRAWN = Color.BLACK;
    public static final Color LINE_COLOR_UNDRAWN = Color.GRAY;
    public static final Color OBJECT_COLOR = Color.RED;
    public static final int OBJECT_NUM = 2;
    private  JFrame frame;
    int objectRadius = 100;
    public boolean isPainted = Boolean.FALSE;
    public ArrayList<RayPaths> rays = new ArrayList<>();
    public ArrayList<Point2D.Double> objects = new ArrayList<>();
    public ArrayList<Ellipse2D> objectsToDraw = new ArrayList<>();
    public ArrayList<Point2D.Double> intersections = new ArrayList<>();

    public ArrayList<Double> distances = new ArrayList<>();
    public ArrayList<Double> entropies = new ArrayList<>();

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        double padding = 10;
        double radius = (double) Math.min(this.getWidth(), this.getHeight()) / 2 - padding * 2;

        Ellipse2D outerEdge = new Ellipse2D.Double(padding, padding, radius * 2, radius * 2);
        g2.setStroke(new BasicStroke(4));
        g2.draw(outerEdge);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(POINT_COLOR);


        if(isPainted == Boolean.FALSE){
            double currentSect = 0;
            // g2.setStroke(new BasicStroke(4));
            for (int i = 0; i < OBJECT_NUM; i++) {
                double xObject = 0;
                double yObject = 0;

                // the new objects must not exist outside of the defined area, so the object diameter is subtracted
                double angle = getRandomNumber(currentSect, currentSect + 0.25) * 2 * Math.PI;
                double r = (radius - objectRadius - objectRadius) * Math.sqrt(Math.random());
                xObject = r * Math.cos(angle) + radius + padding;
                yObject = r * Math.sin(angle) + radius + padding;

                currentSect += 0.25;
                Ellipse2D innerObject = new Ellipse2D.Double(xObject - objectRadius, yObject - objectRadius, objectRadius * 2, objectRadius * 2);
                objectsToDraw.add(innerObject);
                objects.add(new Point2D.Double(xObject,yObject));
            }
            // g2.setStroke(new BasicStroke(1));

            for (int i = 0; i < RAYS_NUM; i++) {
                Random rand = new Random();
                double angle = Math.random() * 2 * Math.PI ;
                double r = radius * Math.sqrt(Math.random());
                double x = r * Math.cos(angle) + radius + padding;
                double y = r * Math.sin(angle) + radius + padding;

                double angle2 = Math.random() * 2 * Math.PI ;
                Point2D.Double start = new Point2D.Double(x + 1000 * Math.cos(angle2), y + 1000 * Math.sin(angle2));
                Point2D.Double end = new Point2D.Double((x - 1000 * Math.cos(angle2)), (y - 1000* Math.sin(angle2)));
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

            Double distance = objects.get(0).distance(objects.get(1));
            Double entropy = calculateEntropy();
            distances.add(distance);
            entropies.add(entropy);

            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.BLACK);
            g2.draw(outerEdge);
            g2.setColor(OBJECT_COLOR);
            g2.setStroke(new BasicStroke(2));
            for(int ob = 0; ob < OBJECT_NUM; ob++){
                g2.draw(objectsToDraw.get(ob));
            }
            g2.setStroke(new BasicStroke(1));

            isPainted = Boolean.TRUE;

        }
        else{

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
            Double distance = objects.get(0).distance(objects.get(1));
            Double entropy = calculateEntropy();
            distances.add(distance);
            entropies.add(entropy);

            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.BLACK);
            g2.draw(outerEdge);
            g2.setColor(OBJECT_COLOR);
            g2.setStroke(new BasicStroke(2));
            for(int ob = 0; ob < OBJECT_NUM; ob++){
                g2.draw(objectsToDraw.get(ob));
            }
            g2.setStroke(new BasicStroke(1));
            System.out.println("after painting updated " + rays.size());

        }
        // TODO remove
        g2.setClip(new Ellipse2D.Double(padding, padding, radius * 2, radius * 2));
        System.out.println("after painting " + rays.size());

    }


    public Double calculateEntropy(){
        int nLegal = 0;
        for(RayPaths ray : rays){
            if(ray.isDrawn){
                nLegal += 1;
            }
        }
        return Math.log(nLegal);
    }



    private Point2D.Double closestPoint(Point2D.Double centrePoint){
        double distance = 1000;
        Point2D.Double closestPoint = new Point2D.Double();
        for (Point2D.Double currentPoint : intersections) {
            double currentDistance = centrePoint.distance(currentPoint);
            if (currentDistance < distance) {
                distance = currentDistance;
                closestPoint = currentPoint;
            }
        }
        return closestPoint;

    }

    public static Point2D.Double intersection(Line2D a, Line2D b) {
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(), x3 = b.getX1(), y3 = b.getY1(),
                x4 = b.getX2(), y4 = b.getY2();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point2D.Double(xi, yi);
    }

    private void findIntersections(){
        intersections.clear();
        for (int i = 0; i < rays.size(); i++) {
            for (int j = i + 1; j < rays.size(); j++) {
                RayPaths ray1 = rays.get(i);
                RayPaths ray2 = rays.get(j);
                Line2D.Double line1 = new Line2D.Double(ray1.startPoint.getX(), ray1.getStartPoint().getY(), ray1.getEndPoint().getX(), ray1.getEndPoint().getY());
                Line2D.Double line2 = new Line2D.Double(ray2.startPoint.getX(), ray2.getStartPoint().getY(), ray2.getEndPoint().getX(), ray2.getEndPoint().getY());
                if (line1.intersectsLine(line2) && ray1.isDrawn && ray2.isDrawn) {
                    Point2D.Double intersection = intersection(line1, line2);
                    if (intersection != null) {
                        intersections.add(intersection);
                    }
                }
            }
        }

    }

    private double getRandomNumber(double min, double max) {
        return (double) ((Math.random() * (max - min)) + min);
    }
    public void update(int batchSize){
        for( int iter = 0; iter < batchSize; iter++){
            // update selects a random ray from the rays and flips update if it is not intrsecting
            double index = getRandomNumber(0, RAYS_NUM - 1);
            System.out.println("in update" + rays.size());

            RayPaths currentRay = rays.get((int)index);
            Line2D temp = new Line2D.Double(currentRay.startPoint.getX(), currentRay.startPoint.getY(), currentRay.endPoint.getX(), currentRay.endPoint.getY());
            // check if there is any collision

            if(currentRay.getDrawn() == Boolean.TRUE){
                currentRay.setDrawn(Boolean.FALSE);
            }
            else{
                for (int j = 0; j < OBJECT_NUM; j++){
                    if(Math.abs(temp.ptLineDist(objects.get(j).getX(), objects.get(j).getY())) < objectRadius ){
                        currentRay.setDrawn(Boolean.FALSE);
                        break;
                    }
                    currentRay.setDrawn(Boolean.TRUE);
                }

            }

        }
    }

    public void generateGraph(){

    }













}