import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class Model extends JPanel {
    public static final int RAYS_NUM = 100000;
    public static final Color POINT_COLOR = Color.RED;
    public static final Color LINE_COLOR_DRAWN = Color.BLACK;
    public static final Color LINE_COLOR_UNDRAWN = Color.GRAY;
    public static final Color OBJECT_COLOR = Color.RED;
    public static final int OBJECT_NUM = 2;
    private  JFrame frame;
    int objectRadius = 75;
    public boolean isPainted = Boolean.FALSE;
    public ArrayList<RayPaths> rays = new ArrayList<>();
    public ArrayList<Point2D.Double> objects = new ArrayList<>();
    public ArrayList<Ellipse2D> objectsToDraw = new ArrayList<>();
    public ArrayList<Point2D.Double> intersections = new ArrayList<>();

    public ArrayList<Double> distances = new ArrayList<>();
    public ArrayList<Double> entropies = new ArrayList<>();
    private double radius = 0;
    private boolean movingTowards = Boolean.TRUE;
    private double seperationFromPoint = 0;

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
        radius = (double) Math.min(this.getWidth(), this.getHeight()) / 2 - padding * 2;

        Ellipse2D outerEdge = new Ellipse2D.Double(padding, padding, radius * 2, radius * 2);
        g2.setStroke(new BasicStroke(4));
        g2.draw(outerEdge);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(POINT_COLOR);


        if(isPainted == Boolean.FALSE){
            double currentSect = 0;
            // g2.setStroke(new BasicStroke(4));

            // the new objects must not exist outside of the defined area, so the object diameter is subtracted

            double r = (radius - objectRadius - 4);
            seperationFromPoint = r;

            double xObject1 = r * Math.cos(0) + radius + padding;
            double yObject1 = r * Math.sin(0) + radius + padding;
            Ellipse2D innerObject = new Ellipse2D.Double(xObject1 - objectRadius, yObject1 - objectRadius, objectRadius * 2, objectRadius * 2);
            objectsToDraw.add(innerObject);
            objects.add(new Point2D.Double(xObject1,yObject1));


            double xObject2 = r * Math.cos(Math.PI) + radius + padding;
            innerObject = new Ellipse2D.Double(xObject2 - objectRadius, yObject1 - objectRadius, objectRadius * 2, objectRadius * 2);
            objectsToDraw.add(innerObject);
            objects.add(new Point2D.Double(xObject2,yObject1));

            System.out.println(xObject1 + " , " + yObject1 + " and " + xObject2 + " , " + yObject1);
            // g2.setStroke(new BasicStroke(1));

            for (int i = 0; i < RAYS_NUM; i++) {
                Random rand = new Random();
                double angle = Math.random() * 2 * Math.PI ;
                r = radius * Math.sqrt(Math.random());
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
            // this is limited to 1000 for processing purposes
            for (int k = 0; k < 1000; k++) {
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
            rays.clear();
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

            for (int k = 0; k < 1000; k++) {
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


    private double getRandomNumber(double min, double max) {
        return (double) ((Math.random() * (max - min)) + min);
    }
    public void update(int batchSize){
        for( int iter = 0; iter < batchSize; iter++){
            // update selects a random ray from the rays and flips update if it is not intersecting
            updateObject();
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
            for (int j = 0; j < OBJECT_NUM; j++){
                for(RayPaths checkRays : rays){
                    Line2D checkLine = new Line2D.Double(checkRays.startPoint.getX(), checkRays.startPoint.getY(), checkRays.endPoint.getX(), checkRays.endPoint.getY());
                    if(Math.abs(checkLine.ptLineDist(objects.get(j).getX(), objects.get(j).getY())) < objectRadius ){
                        checkRays.setDrawn(Boolean.FALSE);
                    }
                }
            }


        }
    }

    public void updateObject(){
        if(movingTowards){
            seperationFromPoint -= 0.1;
            if( seperationFromPoint < (0)){
                movingTowards = Boolean.FALSE;
            }
        }
        else{
            seperationFromPoint += 0.1;
            if( seperationFromPoint > (radius - objectRadius - 4)){
                movingTowards = Boolean.TRUE;
            }

        }

        double xObject1 = seperationFromPoint * Math.cos(0) + radius + 10;
        double yObject1 = seperationFromPoint * Math.sin(0) + radius + 10;
        Ellipse2D innerObject = new Ellipse2D.Double(xObject1 - objectRadius, yObject1 - objectRadius, objectRadius * 2, objectRadius * 2);
        objectsToDraw.set(0, innerObject);
        objects.set(0, new Point2D.Double(xObject1,yObject1));


        double xObject2 = seperationFromPoint * Math.cos(Math.PI) + radius + 10;
        innerObject = new Ellipse2D.Double(xObject2 - objectRadius, yObject1 - objectRadius, objectRadius * 2, objectRadius * 2);
        objectsToDraw.set(1, innerObject);
        objects.set(1, new Point2D.Double(xObject2,yObject1));


    }

















}