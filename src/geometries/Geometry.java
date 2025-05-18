package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The {@code Geometry} class serves as an abstract base class for all geometric objects.
 * It defines common properties and an abstract method for retrieving the normal vector
 * at a given point on the surface.
 */
public abstract class Geometry extends Intersectable {
    protected Color emission = Color.BLACK;

    /**
     * Calculates and returns the normal vector to the geometry at a given point.
     *
     * @param p the point on the geometry where the normal is calculated
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point p);

    /**
     * Returns the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return the geometry object itself for method chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
}
