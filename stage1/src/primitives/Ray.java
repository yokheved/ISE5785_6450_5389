package primitives;

import geometries.Intersectable.Intersection;

import java.util.List;
import java.util.Objects;

/**
 * The {@code Ray} class represents a ray in 3D space.
 * A ray is defined by a starting point (head) and a direction vector.
 * The direction is always normalized.
 * <p>
 * Rays are commonly used in geometric computations such as ray tracing.
 * </p>
 *
 * @author Yokheved and Chaya
 */
public class Ray {
    private static final double DELTA = 0.1;

    /** The origin point of the ray */
    private Point head;

    /** The normalized direction vector of the ray */
    private Vector direction;

    /**
     * Constructs a ray from a given point and direction.
     * The direction is normalized upon construction.
     *
     * @param head the origin point of the ray
     * @param direction the direction vector (will be normalized)
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Constructs a ray from a given point and direction,
     * moving the head delta in the direction of the normal.
     * The direction is normalized upon construction.
     *
     * @param head the origin point of the ray
     * @param direction the direction vector (will be normalized)
     * @param normal the normal in which direction to move the ray
     */
    public Ray(Point head, Vector direction, Vector normal){
        double epsSign =
                Util.alignZero(direction.dotProduct(normal) * -1) < 0 ? 1 : -1;
        Vector eps = normal.scale(DELTA * epsSign);
        this.head = head.add(eps);
        this.direction = direction.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    /**
     * Returns the origin point (head) of the ray.
     *
     * @return the head point of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the normalized direction vector of the ray.
     *
     * @return the direction vector
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Calculates a point along the ray at a distance {@code t} from the origin.
     *
     * @param t the distance from the origin (must be non-negative)
     * @return the computed point on the ray
     * @throws IllegalArgumentException if {@code t} is negative
     */
    public Point getPoint(double t) {
        if (t < 0) throw new IllegalArgumentException("t must be positive or zero");
        if (t == 0) return head;
        return getHead().add(getDirection().scale(t));
    }

    /**
     * Finds the closest point to the ray's origin from a list of points.
     *
     * @param points the list of points to search
     * @return the closest point to the ray's origin,
     *         or {@code null} if the list is {@code null}, empty, or contains only {@code null} entries
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestIntersection(points.stream()
                .map(p -> new Intersection(null, p))
                .toList()
        ).point;
    }

    /**
     * Finds the closest {@link Intersection} point to the ray's origin.
     *
     * @param intersections the list of intersection objects to check
     * @return the closest intersection to the ray's origin,
     *         or {@code null} if the list is {@code null}, empty, or only contains {@code null} entries
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null || intersections.isEmpty()) return null;

        Intersection closest = null;
        double minDist = Double.POSITIVE_INFINITY;

        for (Intersection p : intersections) {
            if (p == null) continue;
            double dist = p.point.distanceSquared(this.getHead());
            if (closest == null || dist < minDist) {
                minDist = dist;
                closest = p;
            }
        }

        return closest;
    }

    public Ray(Point head, Point to){
        this.head = head;
        this.direction = to.subtract(head).normalize();
    }
}
