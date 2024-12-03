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

                // g2.setColor(OBJECT_COLOR);
                Ellipse2D innerObject = new Ellipse2D.Double(xObject - objectRadius, yObject - objectRadius, objectRadius * 2, objectRadius * 2);
                objectsToDraw.add(innerObject);
                //g2.draw(new Ellipse2D.Double(xObject - objectRadius, yObject - objectRadius, objectRadius * 2, objectRadius * 2));
                objects.add(new Point2D.Double(xObject,yObject));
                // to update the objects, each is itterated through and updated
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
            //TODO remove this
            System.out.println("When is painted " + rays.size());


        }
        else{
            findIntersections();
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
            for(Point2D.Double object : objects){


            }



            //TODO, the circles can appear in any area in the defined region off drawn lines, therefore,
            // establis a way to determine the lines near a point
            // make this into a polygon of some kind
            // make a way to update this polygon
            // make a way to draw a circle in this polygon such that the configuration is valid

            // find the nearest lines, get the intersections nearest the circle, and then draw a polygon using them
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

    private boolean IsIntersecting(ArrayList<Point> circles, RayPaths currentRay){
        for (Point circle : circles){


        }
        return Boolean.TRUE;

    }

    private ArrayList<RayPaths> split(RayPaths currentRay){
        int firstIndex = -1;
        Point2D.Double firstPoint = new Point2D.Double();
        Line2D.Double currentLine = new Line2D.Double(currentRay.getStartPoint(), currentRay.getEndPoint());
        for(int i = 0; i < rays.size(); i++){
            Line2D.Double evaluatedLine = new Line2D.Double(rays.get(i).startPoint, rays.get(i).endPoint);
            Point2D.Double point = intersection(currentLine, evaluatedLine);
            if(point != null){
                if(firstIndex == -1){
                    firstIndex = i;
                    firstPoint = point;
                }
                else{
                    ArrayList<RayPaths> segments = new ArrayList<>();
                    // this adds all the existing lines up to the split, then adds the split line and then the rest
                    for(int j = 0; j < firstIndex; j++){
                        RayPaths addedLine = new RayPaths(rays.get(j).startPoint, rays.get(j).endPoint, rays.get(j).isDrawn);
                        segments.add(addedLine);
                    }
                    segments.add(new RayPaths(rays.get(firstIndex).startPoint, firstPoint, rays.get(firstIndex).isDrawn));
                    segments.add(new RayPaths(firstPoint, point, rays.get(firstIndex).isDrawn));
                    segments.add(new RayPaths(point, rays.get(firstIndex).endPoint, rays.get(firstIndex).isDrawn));
                    for(int j = i +1; j < rays.size(); j++){
                        RayPaths addedLine = new RayPaths(rays.get(j).startPoint, rays.get(j).endPoint, rays.get(j).isDrawn);
                        segments.add(addedLine);
                    }

                    segments.removeIf(RayPaths::isEmpty);
                    ArrayList<RayPaths> polygon = new ArrayList<>(segments);
                    segments.clear();

                    segments.add(new RayPaths(firstPoint, rays.get(firstIndex).endPoint, rays.get(firstIndex).isDrawn));
                    for(int j = firstIndex + 1; j < i; j++){
                        segments.add(rays.get(j));
                    }
                    segments.add(new RayPaths(rays.get(i).startPoint, point, rays.get(i).isDrawn));
                    segments.add(new RayPaths(point, firstPoint, rays.get(i).isDrawn));

                    segments.removeIf(RayPaths::isEmpty);
                    polygon.addAll(segments);

                    // generate the path from the points
                    return polygon;
                }

            }
        }
        return null;

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













}