package geometries;

/**
 * The {@code RadialGeometry} class represents geometric objects that have a radius.
 * It serves as a base class for shapes like spheres, cylinders, and tubes.
 * This class is immutable.
 *
 * @author Your Name
 */
public abstract class RadianGeometry extends Geometry {

    /** The radius of the geometric object */
    protected final double radius;

    /**
     * Constructs a {@code RadialGeometry} object with the specified radius.
     *
     * @param radius the radius of the geometric object
     * @throws IllegalArgumentException if the radius is negative
     */
    public RadianGeometry(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative.");
        }
        this.radius = radius;
    }

}
