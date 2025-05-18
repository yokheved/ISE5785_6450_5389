package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The {@code Cylinder} class represents a finite cylinder in 3D space, extending {@link Tube}.
 * A cylinder is defined by a central axis (a {@link Ray}), a radius, and a height.
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
     * Returns the normal vector to the surface of the cylinder at the given point.
     * The point can lie on the lateral surface or on one of the bases.
     *
     * @param p the point on the cylinder's surface
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        // implementation unchanged
        if(p.equals(axis.getHead()) ||
                p.subtract(axis.getHead()).dotProduct(axis.getDirection())==0){
            return axis.getDirection().scale(-1);
        }
        if(p.subtract(axis.getHead()).dotProduct(axis.getDirection())==height){
            return axis.getDirection();
        }
        return super.getNormal(p);
    }

    /**
     * Computes all intersection points between a given ray and the finite cylinder.
     * This includes intersections with the side surface as well as the top and bottom bases.
     *
     * @param ray the ray to intersect with the cylinder
     * @return a list of intersection points with the cylinder, or {@code null} if there are none
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // implementation unchanged
        List<Intersection> intersections = new java.util.LinkedList<>();

        Point basePoint = axis.getHead();
        Vector axisDir = axis.getDirection();
        Point topPoint = basePoint.add(axisDir.scale(height));

        List<Intersection> tubeIntersections = super.calculateIntersectionsHelper(ray);
        if (tubeIntersections != null) {
            for (Intersection p : tubeIntersections) {
                Vector v = p.point.subtract(basePoint);
                double projection = v.dotProduct(axisDir);

                if (projection >= 0 && projection <= height) {
                    intersections.add(p);
                }
            }
        }

        checkCapIntersection(ray, basePoint, axisDir.scale(-1), intersections);
        checkCapIntersection(ray, topPoint, axisDir, intersections);

        return intersections.isEmpty() ? null : intersections;
    }

    /**
     * Checks for an intersection between the given ray and one of the cylinder's caps
     * (either the top or bottom base). If an intersection occurs within the cap's radius,
     * it is added to the list of intersections.
     *
     * @param ray the ray to check for intersection
     * @param capCenter the center point of the cap
     * @param capNormal the normal vector to the cap's plane
     * @param intersections list to collect valid intersection points
     */
    private void checkCapIntersection(Ray ray, Point capCenter, Vector capNormal, List<Intersection> intersections) {
        // implementation unchanged
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        double denominator = v.dotProduct(capNormal);
        if (primitives.Util.isZero(denominator)) {
            return;
        }

        Vector u = capCenter.subtract(p0);
        double t = u.dotProduct(capNormal) / denominator;

        if (t <= 0) {
            return;
        }

        Point intersectionPoint = p0.add(v.scale(t));
        double distanceSquared = intersectionPoint.distanceSquared(capCenter);

        if (distanceSquared <= radius * radius) {
            intersections.add(new Intersection(this, intersectionPoint));
        }
    }
}
