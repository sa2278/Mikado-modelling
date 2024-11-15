import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class Segment extends Point {
    double angle;
    Point point;


    public Segment(Point point, double angle){
        super(point.x, point.y);
        this.point = point;
        this.angle = angle;
    }

    public double getAngle(){
        return angle;
    }

    public ArrayList<Point> drawSegment(){
        // in this case, the 200 is used to represent the current radius
        double perpendicularDistance = Math.sqrt((point.getX() - 550) * (point.getX() - 550) + (point.getY() - 550) * (point.getY() - 550));
        double chordLength = (2 * Math.sqrt(200 * 200 - perpendicularDistance * perpendicularDistance));
        int halfChordLength = (int)(chordLength / 2);

        int highX = point.getX() + (int) (halfChordLength * Math.cos(angle));
        int lowX = point.getX() - (int) (halfChordLength * Math.cos(angle));

        int highY = point.getY() + (int) (halfChordLength * Math.sin(angle));
        int lowY = point.getY() - (int) (halfChordLength * Math.sin(angle));

        ArrayList<Point> pointPair = new ArrayList<>();
        pointPair.add(new Point(highX, highY));
        pointPair.add(new Point(lowX,lowY));

        return pointPair;






    }


}
