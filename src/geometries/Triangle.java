package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The {@code Triangle} class represents a triangle in 3D space.
 * It extends {@link Polygon} and is always defined by exactly three points.
 *
 * @author Your Name
 */
public class Triangle extends Polygon {

    /**
     * Constructs a {@code Triangle} with three given points.
     *
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     *
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3); // Calls the Polygon constructor

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        try {
            Point intersectionPlane = plane.findIntersections(ray).getFirst();

            Vector v1 = vertices.get(0).subtract(ray.getHead());
            Vector v2 = vertices.get(1).subtract(ray.getHead());
            Vector v3 = vertices.get(2).subtract(ray.getHead());

            Vector n1 = v1.crossProduct(v2).normalize();
            Vector n2 = v2.crossProduct(v3).normalize();
            Vector n3 = v3.crossProduct(v1).normalize();

            double dot1 = intersectionPlane.subtract(ray.getHead()).dotProduct(n1);
            double dot2 = intersectionPlane.subtract(ray.getHead()).dotProduct(n2);
            double dot3 = intersectionPlane.subtract(ray.getHead()).dotProduct(n3);

            if (Util.isZero(dot1) || Util.isZero(dot2) || Util.isZero(dot3)) {
                return null; // Ray hits exactly on edge or vertex
            }

            boolean positive = dot1 > 0;

            if ((dot2 > 0) != positive || (dot3 > 0) != positive) {
                return null; // Signs not all same → outside
            }

            List<Point> result = new LinkedList<>();
            result.add(intersectionPlane);
            return result;

        } catch (Exception e) {
            return null;
        }
    }

}

