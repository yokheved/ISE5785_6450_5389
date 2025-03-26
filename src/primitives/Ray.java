package primitives;

import java.util.Objects;

/**
 * The {@code Ray} class represents a ray in 3D space.
 * A ray is defined by a starting point (head) and a direction vector.
 *
 * @author @author Yokheved and Chaya
 */
public class Ray {

    /** The head (origin) of the ray */
    private Point head;

    /** The direction of the ray */
    private Vector direction;

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
}
