package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
        //check if point is on one of the bases
        if(p.equals(axis.getHead()) ||
                p.subtract(axis.getHead()).dotProduct(axis.getDirection())==0){
            return axis.getDirection().scale(-1);
        }
        if(p.subtract(axis.getHead()).dotProduct(axis.getDirection())==height){
            return axis.getDirection();
        }
        //return like a cylinder
        return super.getNormal(p);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Create a list to store all valid intersections
        List<Point> intersections = new java.util.LinkedList<>();

        // Get the base and top points of the cylinder
        Point basePoint = axis.getHead();
        Vector axisDir = axis.getDirection();
        Point topPoint = basePoint.add(axisDir.scale(height));

        // First, check for intersections with the cylindrical surface
        List<Point> tubeIntersections = super.findIntersections(ray);
        if (tubeIntersections != null) {
            // Filter tube intersections to only those within the cylinder height
            for (Point p : tubeIntersections) {
                // Calculate projection along axis to determine position
                Vector v = p.subtract(basePoint);
                double projection = v.dotProduct(axisDir);

                // Keep points between the bases
                if (projection >= 0 && projection <= height) {
                    intersections.add(p);
                }
            }
        }

        // Check for intersection with bottom base (at basePoint)
        checkCapIntersection(ray, basePoint, axisDir.scale(-1), intersections);

        // Check for intersection with top base (at topPoint)
        checkCapIntersection(ray, topPoint, axisDir, intersections);

        // Return null if no valid intersections
        return intersections.isEmpty() ? null : intersections;
    }

    /**
     * Helper method to check for intersection with a cylinder cap (base or top)
     *
     * @param ray the ray to check for intersection
     * @param capCenter the center point of the cap
     * @param capNormal the normal to the cap (pointing outward)
     * @param intersections list to add any valid intersections to
     */
    private void checkCapIntersection(Ray ray, Point capCenter, Vector capNormal, List<Point> intersections) {
        // Get ray origin and direction
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // Calculate the denominator of the intersection formula
        double denominator = v.dotProduct(capNormal);

        // If ray is parallel to the cap (denominator ≈ 0), no intersection
        if (primitives.Util.isZero(denominator)) {
            return;
        }

        // Calculate parameter t for plane intersection:
        // t = ((capCenter - p0) · capNormal) / (v · capNormal)
        Vector u = capCenter.subtract(p0);
        double t = u.dotProduct(capNormal) / denominator;

        // If t ≤ 0, intersection is behind the ray origin
        if (t <= 0) {
            return;
        }

        // Calculate the intersection point with the plane
        Point intersectionPoint = p0.add(v.scale(t));

        // Check if the intersection point is within the cap's circle
        // Measure distance from intersection to the cap center
        double distanceSquared = intersectionPoint.distanceSquared(capCenter);

        // If distance ≤ radius, the point is within the cap
        if (distanceSquared <= radius * radius) {
            intersections.add(intersectionPoint);
        }
    }
}
