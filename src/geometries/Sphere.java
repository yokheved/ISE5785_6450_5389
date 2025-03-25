package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The {@code Sphere} class represents a sphere in 3D space.
 * A sphere is defined by a center point and a radius.
 *
 * @author Your Name
 */
public class Sphere extends RadianGeometry {

    /** The center of the sphere */
    private final Point center;

    /**
     * Constructs a {@code Sphere} with a given center and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     * @throws IllegalArgumentException if the radius is negative
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }


    @Override
    public Vector getNormal(Point p) {
        // Compute the normal by subtracting the center from p and normalizing
        return p.subtract(center).normalize();
    }

}
