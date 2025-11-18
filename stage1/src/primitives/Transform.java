package primitives;

import java.util.ArrayList;
import java.util.List;

import primitives.Double3;

import static primitives.Util.isZero;

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
     * @param delta    the direction and distance to move the points
     * @param points    the points or vectors to move
     * @return a new list containing the moved points, preserving the runtime
     *         type of each element
     */
    public static List<Point> movePoints(Vector delta, Point... points) {
        List<Point> result = new ArrayList<>(points.length);
        for (Point p : points) {
            Point movedPoint = p.add(delta);
            result.add(movedPoint);
        }
        return result;
    }

    /**
     * Rotates the given points (or vectors) around the Z-axis clockwise by the
     * specified angle measured in degrees.
     *
     * @param degrees rotation angle in degrees (clockwise)
     * @param axis    the axis of rotation, which defines the origin and direction
     * @param points  the points or vectors to rotate
     * @return a new list containing the rotated points, preserving the runtime
     *         type of each element
     */
    public static List<Point> rotatePointsClockwise(double degrees, Ray axis, Point... points) {
        Point origin = axis.getHead();
        List<Point> result = new ArrayList<>(points.length);

        for (Point p : points) {
            Vector v = p.subtract(origin);
            Vector rotated = rotateVectorsClockwise(degrees, axis, v).get(0); // Rotate the vector
            Point rotatedPoint = origin.add(rotated);
            result.add(rotatedPoint);
        }
        return result;
    }

    /**
     * Rotates the given vectors around the specified axis clockwise by the
     * specified angle measured in degrees.
     *
     * @param degrees rotation angle in degrees (clockwise)
     * @param axis    the axis of rotation, which defines the origin and direction
     * @param vectors the vectors to rotate
     * @return a new list containing the rotated vectors, preserving the runtime
     *         type of each element
     */
    public static List<Vector> rotateVectorsClockwise(double degrees, Ray axis, Vector... vectors) {
        double radians = Math.toRadians(-degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        Vector k = axis.getDirection();
        List<Vector> result = new ArrayList<>(vectors.length);

        for (Vector v : vectors) {

            // General case: Rodrigues' rotation formula
            Point term1 = isZero(cos) ?  new Point(0, 0, 0) : v.scale(cos);
            Point term2 = isZero(sin)? new Point(0, 0, 0) : k.crossProduct(v).scale(sin);
            Point term3 = isZero(k.dotProduct(v)) ? new Point(0, 0, 0) : k.scale(k.dotProduct(v) * (1 - cos));

            // Ensure precision by aligning values to zero where applicable
            Vector rotated = new Vector(
                Util.alignZero(term1.xyz.d1() + term2.xyz.d1() + term3.xyz.d1()),
                Util.alignZero(term1.xyz.d2() + term2.xyz.d2() + term3.xyz.d2()),
                Util.alignZero(term1.xyz.d3() + term2.xyz.d3() + term3.xyz.d3())
            );

            result.add(rotated);
        }
        return result;
    }


}
