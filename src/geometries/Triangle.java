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
            Point intersectionsPlane = plane.findIntersections(ray).getFirst();

            Vector v1 = vertices.get(0).subtract(ray.getHead());
            Vector v2 = vertices.get(1).subtract(ray.getHead());
            Vector v3 = vertices.get(2).subtract(ray.getHead());

            Vector n1 = v1.crossProduct(v2).normalize();
            Vector n2 = v2.crossProduct(v3).normalize();
            Vector n3 = v3.crossProduct(v1).normalize();

            Vector[] vectors = {v1, v2, v3};
            Vector[] normals = {n1, n2, n3};

            Double firstSign = null;

            for (Vector v : vectors) {
                for (Vector n : normals) {
                    double dot = v.dotProduct(n);

                    if (Util.isZero(dot)) {
                        return null; // Edge case: exactly on the boundary
                    }

                    if (firstSign == null) {
                        firstSign = Math.signum(dot);
                    } else {
                        if (Math.signum(dot) != firstSign) {
                            return null; // Different signs → outside
                        }
                    }
                }
            }

           List<Point> result = new LinkedList<>();
            result.add(intersectionsPlane);
            return result;

        }catch(NoSuchElementException e){
            return null;
        }
    }
}
