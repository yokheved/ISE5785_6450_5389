package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all geometric objects that can be intersected with rays.
 * <p>
 * Provides the interface for computing intersections and encapsulates the intersection result.
 * Subclasses must implement the intersection calculation logic.
 */
public abstract class Intersectable {

    /**
     * Represents a detailed intersection between a {@link Ray} and a {@link Geometry}.
     * <p>
     * Contains the intersection point, the geometry it intersects, and optional lighting-related information.
     */
    public static class Intersection {
        /** The geometry object that was intersected */
        public final Geometry geometry;

        /** The point of intersection */
        public final Point point;

        /** The material at the intersection point */
        public final Material material;

        /** The direction of the intersecting ray (optional) */
        public Vector rayDirection;

        /** The normal vector of the geometry at the intersection point (optional) */
        public Vector geometryNormal;

        /** The dot product of the ray direction and the normal (optional) */
        public double directionDotNormal;

        /** The light source considered at this intersection (optional) */
        public LightSource lightSource;

        /** The direction from the point to the light source (optional) */
        public Vector lightDirection;

        /** The dot product of the light direction and the geometry normal (optional) */
        public double lightDirectionDotNormal;

        /**
         * Constructs a new {@code Intersection} object with the given geometry and point.
         * If the geometry is {@code null}, a default material is assigned.
         *
         * @param geometry the geometry that was intersected
         * @param point    the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? new Material() : geometry.getMaterial();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Intersection that = (Intersection) o;
            return this.geometry == that.geometry && this.point.equals(that.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(geometry, point);
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }

    /**
     * Calculates all intersections between the specified ray and this geometric object.
     * <p>
     * Each intersection includes the intersected geometry and the intersection point.
     *
     * @param ray the ray to intersect with
     * @return a list of intersection objects, or {@code null} if no intersections are found
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Calculates all intersections between the specified ray and this geometry,
     * limiting the results to those within the given maximum distance.
     *
     * @param ray         the ray to intersect with
     * @param maxDistance the maximum allowed distance from the ray origin to an intersection point
     * @return a list of intersection objects, or {@code null} if no intersections are found
     */
    public final List<Intersection> calculateIntersections(Ray ray, double maxDistance) {
        return calculateIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Protected abstract helper method that subclasses must implement to perform the actual intersection computation.
     * <p>
     * Called internally by {@link #calculateIntersections(Ray)} and {@link #calculateIntersections(Ray, double)}.
     *
     * @param ray         the ray to intersect with
     * @param maxDistance the maximum allowed distance from the ray origin to an intersection point
     * @return a list of intersection results including geometry and point, or {@code null} if none are found
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * Returns only the intersection points (without geometry information) between the given ray and this geometry.
     *
     * @param ray the ray to intersect with
     * @return a list of intersection points, or {@code null} if no intersections are found
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }
}
