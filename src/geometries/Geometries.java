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
public class Geometries implements Intersectable {

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

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;
        for (Intersectable geometry : geometries) {
            List<Point> gResult = geometry.findIntersections(ray);
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
