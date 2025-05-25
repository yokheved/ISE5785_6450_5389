package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * The {@code Triangle} class represents a triangle in 3D space.
 * It extends {@link Polygon} and is defined by exactly three vertices.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle with three given vertices.
     *
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = plane.calculateIntersectionsHelper(ray, maxDistance);
        if (intersections == null) return null;

        Point p0 = ray.getHead();
        Point p = intersections.get(0).point;
        double distance = p.distance(p0);
        if(distance > maxDistance) return null;

        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        Vector vp = p.subtract(p0);

        double dot1 = Util.alignZero(vp.dotProduct(n1));
        double dot2 = Util.alignZero(vp.dotProduct(n2));
        double dot3 = Util.alignZero(vp.dotProduct(n3));

        // If the point lies on the edge or vertex, or outside
        if (dot1 == 0 || dot2 == 0 || dot3 == 0) return null;

        boolean isPositive = dot1 > 0;

        if ((dot2 > 0) != isPositive || (dot3 > 0) != isPositive)
            return null;

        return List.of(new Intersection(this, p));
    }
}
