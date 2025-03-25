package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The {@code Geometry} class serves as an abstract base class for all geometric objects.
 * It defines a method for retrieving the normal vector at a given point on the surface.
 * Subclasses must implement this method to provide the correct normal calculation.
 *
 * @author Your Name
 */
public abstract class Geometry {

    /**
     * Calculates and returns the normal vector to the geometry at a given point.
     *
     * @param p the point on the geometric object where the normal is calculated
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point p);
}
