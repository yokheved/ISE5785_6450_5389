package primitives;

import java.util.Objects;

/**
 * The {@code Point} class represents a point in 3D space.
 * It is defined by a {@link Double3} object which contains the 3D coordinates.
 *
 * @author Yokheved and Chaya
 */
public class Point {

    /** The origin point (0, 0, 0) */
    public final static Point ZERO = new Point(Double3.ZERO);

    /** The coordinates of the point */
    protected final Double3 xyz;

    /**
     * Constructs a {@code Point} with the given 3D coordinates.
     *
     * @param xyz the coordinates of the point
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Constructs a {@code Point} with the given x, y, and z coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(xyz);
    }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }

    /**
     * Adds a vector to the point and returns a new point.
     *
     * @param other the vector to add
     * @return a new point after the addition
     */
    public Point add(Vector other) {
        return new Point(xyz.add(other.xyz));
    }

    /**
     * Subtracts another point from this point and returns the resulting vector.
     *
     * @param other the point to subtract
     * @return a vector representing the difference between the two points
     * @throws IllegalArgumentException if the points are identical
     */
    public Vector subtract(Point other) {
        if (this.equals(other)) {
            throw new IllegalArgumentException("Other point equals self is illegal");
        }
        return new Vector(xyz.subtract(other.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param other the point to calculate the distance to
     * @return the squared distance between the two points
     */
    public double distanceSquared(Point other) {
        return (this.xyz.d1() - other.xyz.d1()) * (this.xyz.d1() - other.xyz.d1()) +
                (this.xyz.d2() - other.xyz.d2()) * (this.xyz.d2() - other.xyz.d2()) +
                (this.xyz.d3() - other.xyz.d3()) * (this.xyz.d3() - other.xyz.d3());
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param other the point to calculate the distance to
     * @return the distance between the two points
     */
    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }
}
