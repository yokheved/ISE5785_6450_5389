package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Plane} class represents a plane in 3D space.
 * A plane is defined by either:
 * <ul>
 *   <li>A point on the plane and a normal vector</li>
 *   <li>Three non-collinear points in space</li>
 * </ul>
 * The plane is infinite and is mainly used for intersection calculations.
 *
 * @author Your Name
 */
public class Plane extends Geometry {

    /** A point on the plane */
    private final Point q;

    /** The normalized normal vector to the plane */
    private final Vector normal;

    /**
     * Constructs a plane using a point and a normal vector.
     *
     * @param q      a point on the plane
     * @param normal the normal vector to the plane (will be normalized)
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Constructs a plane using three non-collinear points.
     * The normal vector is calculated using the cross-product of vectors
     * p1→p2 and p1→p3.
     *
     * @param p1 first point on the plane
     * @param p2 second point on the plane
     * @param p3 third point on the plane
     * @throws IllegalArgumentException if the points are collinear
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.q = p1;

        // Compute normal as the cross product of two vectors in the plane
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();

    }

    /**
     * Returns the normal vector to the plane.
     *
     * @return the normal vector
     */
    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        double t;
        try {
            t = normal.dotProduct(q.subtract(ray.getHead())) / normal.dotProduct(ray.getDirection());
            if(t==0) return null;
        }catch (Exception e){
            return null;
        }
        List<Point> returnList = new LinkedList<>();
        returnList.add(ray.getPoint(t)) ;
        return returnList;
    }
}
