package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of intersectable geometric objects.
 * This class allows grouping multiple {@link Intersectable} objects and
 * treating them as a single entity for intersection computations.
 */
public class Geometries extends Intersectable {

    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Constructs an empty collection of geometries.
     */
    public Geometries() {
    }

    /**
     * Constructs a collection of geometries from the given array of intersectables.
     *
     * @param geometries one or more intersectable objects to add to the collection
     */
    public Geometries(Intersectable... geometries){
        this.add(geometries);
    }

    /**
     * Adds one or more intersectable objects to the collection.
     *
     * @param geometries the intersectable objects to add
     */
    public void add(Intersectable... geometries){
        this.geometries.addAll(Arrays.asList(geometries));
    }

    /**
     * Computes the intersections of the given ray with all geometries in the collection.
     *
     * @param ray the ray to test for intersections
     * @return a list of intersection points, or {@code null} if there are no intersections
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> result = null;
        for (Intersectable geometry : geometries) {
            List<Intersection> gResult = geometry.calculateIntersectionsHelper(ray);
            if (gResult != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(gResult);
            }
        }
        return result;
    }
}
