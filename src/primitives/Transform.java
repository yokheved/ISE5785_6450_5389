package primitives;

import java.util.ArrayList;
import java.util.List;

import primitives.Double3;

/**
 * Utility class for performing basic transformations on points and vectors.
 * Provides methods for translating and rotating multiple {@link Point} or
 * {@link Vector} objects.
 */
public final class Transform {

    // Prevent instantiation
    private Transform() { }

    /**
     * Translates the given points (or vectors) by moving them in the provided
     * direction scaled to the specified distance.
     *
     * @param direction the direction of translation
     * @param distance  how far to move along the direction
     * @param points    the points or vectors to move
     * @return a new list containing the moved points, preserving the runtime
     *         type of each element
     */
    public static List<Point> movePoints(Vector direction, double distance,
                                         Point... points) {
        Vector delta = direction.normalize().scale(distance);
        List<Point> result = new ArrayList<>(points.length);
        for (Point p : points) {
            Double3 xyz = p.xyz.add(delta.xyz);
            if (p instanceof Vector) {
                result.add(new Vector(xyz));
            } else {
                result.add(new Point(xyz));
            }
        }
        return result;
    }

    /**
     * Rotates the given points (or vectors) around the Z-axis clockwise by the
     * specified angle measured in degrees.
     *
     * @param degrees rotation angle in degrees (clockwise)
     * @param points  the points or vectors to rotate
     * @return a new list containing the rotated points, preserving the runtime
     *         type of each element
     */
    public static List<Point> rotatePointsClockwise(double degrees,
                                                    Point... points) {
        double radians = Math.toRadians(-degrees); // negative for clockwise
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        List<Point> result = new ArrayList<>(points.length);
        for (Point p : points) {
            double x = p.xyz.d1() * cos - p.xyz.d2() * sin;
            double y = p.xyz.d1() * sin + p.xyz.d2() * cos;
            Double3 xyz = new Double3(x, y, p.xyz.d3());
            if (p instanceof Vector) {
                result.add(new Vector(xyz));
            } else {
                result.add(new Point(xyz));
            }
        }
        return result;
    }
}
