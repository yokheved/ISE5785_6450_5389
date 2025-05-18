package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Plane} class represents an infinite plane in 3D space.
 * A plane can be defined either by:
 * <ul>
 *   <li>A point on the plane and a normal vector</li>
 *   <li>Three non-collinear points lying on the plane</li>
 * </ul>
 * It is mainly used for geometric intersection calculations.
 */
public class Plane extends Geometry {

    /** A reference point on the plane */
    private final Point q;

    /** The normalized normal vector of the plane */
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
     * Constructs a plane from three non-collinear points.
     * The normal is computed as the cross product of vectors p1→p2 and p1→p3.
     *
     * @param p1 the first point on the plane
     * @param p2 the second point on the plane
     * @param p3 the third point on the plane
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
     * The vector is constant across the entire surface.
     *
     * @param p a point on the surface (not used in this implementation)
     * @return the constant normal vector
     */
    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    /**
     * Computes intersections of a ray with the plane.
     *
     * @param ray the ray to check for intersection
     * @return a list containing one intersection if the ray intersects the plane, or {@code null} otherwise
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        double t;
        try {
            t = normal.dotProduct(q.subtract(ray.getHead())) / normal.dotProduct(ray.getDirection());
            if (t < 0 || Util.isZero(t)) return null;
        } catch (Exception e) {
            return null;
        }

        List<Intersection> returnList = new LinkedList<>();
        returnList.add(new Intersection(this, ray.getPoint(t)));
        return returnList;
    }
}
