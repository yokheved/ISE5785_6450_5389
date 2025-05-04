package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.rmi.server.UID;
import java.util.LinkedList;
import java.util.List;

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
        Point p0 = axis.getHead();
        Vector dir = axis.getDirection();

        Vector p0ToP = p.subtract(p0);
        double t = dir.dotProduct(p0ToP);
        if(Util.isZero(t)){
            return new Vector(p).normalize();
        }
        Vector dirToP = dir.scale(t);
        Point o = p0.add(dirToP);
        return p.subtract(o).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();
        Point pa = axis.getHead();
        Vector va = axis.getDirection();

        // Calculate coefficients for the quadratic equation
        // Using the formula for distance between a point and a line
        if(va.equals(v) || va.equals(v.scale(-1))) return null;
        if(p0.equals(pa)){
            // In this case, ray will always intersect the tube unless it's parallel
            // to the axis (which we already handled above)
            double t = radius / Math.sqrt(1 - Math.pow(v.dotProduct(va), 2));
            List<Point> result = new LinkedList<>();
            result.add(p0.add(v.scale(t)));
            return result;
        }
        Vector deltaP = p0.subtract(pa);

        // Check if ray starts on or very close to the axis
        boolean isOnAxis = false;

        // Project deltaP onto the axis direction to find the closest point on axis
        double t = deltaP.dotProduct(va);
        Point closestAxisPoint = Util.isZero(t) ? pa.add(va.scale(deltaP.length())) : pa.add(va.scale(t));

        // If distance from ray origin to axis is very small, ray starts on or very close to axis
        if (Util.isZero(p0.distance(closestAxisPoint))) {
            isOnAxis = true;
        }

        // Calculate components for quadratic formula
        double a = v.lengthSquared() - Math.pow(v.dotProduct(va), 2);

        // If ray is parallel to tube axis, no intersections
        if (Util.isZero(a)) {
            return null;
        }

        double b = 2 * (v.dotProduct(deltaP) - (v.dotProduct(va) * deltaP.dotProduct(va)));
        double c = deltaP.lengthSquared() - Math.pow(deltaP.dotProduct(va), 2) - radius * radius;


        // Special case: ray starts on axis
        if (isOnAxis) {
            // Ray from axis will always intersect tube once in any direction (except parallel)
            double t_intersection = Math.sqrt(radius * radius / a);
            Point intersectionPoint = p0.add(v.scale(t_intersection));
            List<Point> result = new LinkedList<>();
            result.add(intersectionPoint);
            return result;
        }

        // Solve the quadratic equation
        double discriminant = b * b - 4 * a * c;

        // No solutions - no intersections
        if (discriminant < 0) {
            return null;
        }

        // Calculate intersection points for non-tangent case
        discriminant = Math.sqrt(discriminant);

        // Check for zero distance from ray origin to tube surface
        if (Util.isZero(c)) {
            // Ray starts on tube surface
            // If ray points inward, it will hit the other side
            if (discriminant > 0) {
                double t2 = -b / a; // Simplified from (-b + 0)/(2*a)
                if (t2 > 0) {
                    List<Point> result = new LinkedList<>();
                    result.add(p0.add(v.scale(t2)));
                    return result;
                }
            }
            return null;
        }
        double t1 = (-b + discriminant) / (2 * a);
        double t2 = (-b - discriminant) / (2 * a);

        // Check if both intersections are behind the ray
        if (t1 <= 0 && t2 <= 0) {
            return null;
        }

        // Now create the list since we know we have at least one valid intersection
        List<Point> result = new LinkedList<>();

        // Add valid intersections to the result
        if (t1 > 0) {
            result.add(p0.add(v.scale(t1)));
        }

        if (t2 > 0) {
            result.add(p0.add(v.scale(t2)));
        }

        return result;
    }

}
