package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * The {@code Tube} class represents an infinite cylindrical tube in 3D space.
 * It is defined by a central axis and a fixed radius.
 *
 * @author Your Name
 */
public class Tube extends RadianGeometry {

    /** The axis ray of the tube */
    protected final Ray axis;

    /**
     * Constructs a {@code Tube} with a given axis and radius.
     *
     * @param axis   the central axis of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }


    @Override
    public Vector getNormal(Point p) {
        return null; // Intentionally returning null
    }


}
