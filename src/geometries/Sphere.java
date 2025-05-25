package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Sphere} class represents a sphere in 3D space.
 * A sphere is defined by its center point and radius.
 * It is a type of radial geometry (i.e., has a radius).
 * Used primarily for intersection calculations in ray tracing.
 */
public class Sphere extends RadianGeometry {

    /** The center point of the sphere */
    private final Point center;

    /**
     * Constructs a {@code Sphere} with a specified center and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere (must be non-negative)
     * @throws IllegalArgumentException if the radius is negative
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Computes the normal vector to the sphere at a given point on its surface.
     *
     * @param p a point on the surface of the sphere
     * @return the normalized vector from the center to the point
     */
    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }

    /**
     * Computes the intersection points of the given ray with the sphere.
     *
     * @param ray the ray to intersect with the sphere
     * @return a list of intersection points, or {@code null} if there are none
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Vector u;
        double tm, d;

        try {
            u = center.subtract(ray.getHead());
            tm = ray.getDirection().dotProduct(u);
            d = Math.sqrt(u.lengthSquared() - tm * tm);
        } catch (IllegalArgumentException e) {
            // Ray starts at the center of the sphere
            tm = 0;
            d = 0;
        }

        // No intersection if the distance from the ray to the center is greater than the radius
        if (d > radius || Util.isZero(radius - d)) {
            return null;
        }

        double th = Math.sqrt(radius * radius - d * d);
        double t1 = tm - th;
        double t2 = tm + th;

        if ((t1 < 0 || Util.isZero(t1)) && (t2 < 0 || Util.isZero(t2))) {
            return null;
        }

        List<Intersection> result = new LinkedList<>();
        if (t1 > 0 && !Util.isZero(t1) && Util.alignZero(t1-maxDistance) <= 0) {
            result.add(new Intersection(this, ray.getPoint(t1)));
        }
        if (t2 > 0 && !Util.isZero(t2) && Util.alignZero(t2-maxDistance) <= 0) {
            result.add(new Intersection(this, ray.getPoint(t2)));
        }

        return result.isEmpty() ? null : result;
    }
}
