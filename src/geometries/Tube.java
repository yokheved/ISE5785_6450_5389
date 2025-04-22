package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.rmi.server.UID;
import java.util.List;

/**
 * The {@code Tube} class represents an infinite cylindrical tube in 3D space.
 * It is defined by a central axis and a fixed radius.
 *
 * @author Your Name
 */
public class Tube extends RadianGeometry {

    /** The axis ray of the tube */
    protected final Ray axis;

    /**
     * Constructs a {@code Tube} with a given axis and radius.
     *
     * @param axis   the central axis of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point p) {
        Point p0 = axis.getHead();
        Vector dir = axis.getDirection();

        Vector p0ToP = p.subtract(p0);
        double t = dir.dotProduct(p0ToP);
        if(Util.isZero(t)){
            return new Vector(p).normalize();
        }
        Vector dirToP = dir.scale(t);
        Point o = p0.add(dirToP);
        return p.subtract(o).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
