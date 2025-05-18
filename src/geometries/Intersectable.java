package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all geometric objects that can be intersected with rays.
 * Provides the interface for computing intersections and encapsulates the intersection result.
 */
public abstract class Intersectable {

    /**
     * Represents an intersection between a {@link Ray} and a {@link Geometry}.
     * Contains the point of intersection and the geometry it belongs to.
     */
    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        /**
         * Constructs a new Intersection object.
         *
         * @param geometry the geometry that the ray intersected
         * @param point the intersection point on the geometry
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
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
     * Finds all intersections between a given ray and this geometry.
     * Returns a list of {@link Intersection} objects that include the intersected geometry.
     *
     * @param ray the ray to intersect with
     * @return list of intersections, or {@code null} if there are none
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Protected abstract method that subclasses must implement to calculate intersections.
     * This method is called by {@link #calculateIntersections(Ray)}.
     *
     * @param ray the ray to intersect with
     * @return list of intersections including geometry and point, or {@code null} if none
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    /**
     * Finds intersection points (without geometry references) between a given ray and this geometry.
     *
     * @param ray the ray that intersects with the geometry
     * @return a list of points where the ray intersects the geometry, or {@code null} if none
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }
}
