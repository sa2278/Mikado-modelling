import java.awt.geom.Point2D;

public class RayPaths {
    Point startPoint;
    Point endPoint;
    Boolean isDrawn;

    public RayPaths(Point startPoint, Point endPoint, Boolean isDrawn){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.isDrawn = isDrawn;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setDrawn(Boolean drawn) {
        isDrawn = drawn;
    }

    public Boolean getDrawn() {
        return isDrawn;
    }
}
