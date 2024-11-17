import java.util.*;

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
        double halfChordLength = (int)(chordLength / 2);

        //a new random angle is selected for the chord
        Random rand = new Random();
        double angle = rand.nextDouble() * 2 * Math.PI;

        return calculateIntercepts(this.point, angle);

    }

    public ArrayList<Point> calculateIntercepts(Point point, Double angle){
        double radius = 200;
        double x = point.getX();
        double y = point.getY();
        double tanTheta = Math.tan(angle);
        double a = 1 + tanTheta * tanTheta;
        double b = -2 * (x+ tanTheta * (y - tanTheta * x));
        double c = x * x + (y - tanTheta * x) * (y - tanTheta * x)- radius * radius;


        double discriminant = b * b - (4 * a * c);

        // here it is assumed all lines intercept the circle at a point
        double sqrtDiscriminant = Math.sqrt(discriminant);
        double x1 = ((-b + sqrtDiscriminant) / (2 * a));
        double x2 = ((-b - sqrtDiscriminant) / (2 * a));

        double y1 = (int)(tanTheta * (x1 - x) + y);
        double y2 = (int)(tanTheta * (x2 - x) + y);

        ArrayList<Point> returns = new ArrayList<>();
        returns.add(new Point(x1, y1));
        returns.add(new Point(x2, y2));

        System.out.println("x1 y1, x2 y2");
        System.out.println(x1);
        System.out.println(y1);
        System.out.println(x2);
        System.out.println(y2);

        return returns;

    }


}
