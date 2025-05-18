package primitives;

import java.util.List;
import java.util.Objects;

/**
 * The {@code Ray} class represents a ray in 3D space.
 * A ray is defined by a starting point (head) and a direction vector.
 *
 * @author @author Yokheved and Chaya
 */
public class Ray {

    /**
     * The head (origin) of the ray
     */
    private Point head;

    /**
     * The direction of the ray
     */
    private Vector direction;

    public Ray(Point head, Vector direction) {
        this.head = head;
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

    public Point getHead() {
        return head;
    }

    public Vector getDirection() {
        return direction;
    }

    public Point getPoint(double t) {
        if (t < 0) throw new IllegalArgumentException("t must be positive or zero");
        if (t == 0) return head;
        return getHead().add(getDirection().scale(t));
    }

    public Point findClosestPoint(List<Point> points) {
        // אם הרשימה null או ריקה – מחזירים null
        if (points == null || points.isEmpty()) {
            return null;
        }
        Point closest = null;
        double minDist = Double.POSITIVE_INFINITY;  // מתחילים ב∞

        for (Point p : points) {
            if (p == null) {
                continue;  // מדלגים אם נתנו null ברשימה
            }
            // חשוב: קריאה ל-distanceSquared מנקודת ה-origin של הקרן
            double dist = p.distanceSquared(this.getHead());
            // ברגע ש־closest == null או מצאנו מרחק קטן יותר – מעדכנים
            if (closest == null || dist < minDist) {
                minDist = dist;
                closest = p;
            }
        }
        return closest;
    }




}
