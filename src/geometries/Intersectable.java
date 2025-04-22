package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * interface for bodies that can be intersected with rays and calculate them
 */
public interface Intersectable {
    /**
     * finds intersections of the ray with the intersectable body
     * @param ray that intersects with the body
     * @return a list of points on the body where the ray intersected, if none returns null
     */
    List<Point> findIntersections(Ray ray);
}
