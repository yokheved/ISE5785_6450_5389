package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * The {@code Cylinder} class represents a finite cylinder in 3D space, extending {@link Tube}.
 * A cylinder is defined by a central axis (a {@link Ray}), a radius, and a height.
 *
 * @author Your Name
 */
public class Cylinder extends Tube {

    /** The height of the cylinder */
    private final double height;

    /**
     * Constructs a new {@code Cylinder} with the specified height, axis, and radius.
     *
     * @param height the height of the cylinder
     * @param axis the central axis of the cylinder represented as a {@link Ray}
     * @param radius the radius of the circular base of the cylinder
     */
    public Cylinder(double height, Ray axis, double radius) {
        super(axis, radius);
        this.height = height;
    }

    /**
     * Returns the normal vector to the cylinder at a given point on its surface.
     *
     * @param p the point on the cylinder's surface
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return null; // To be implemented
    }
}
