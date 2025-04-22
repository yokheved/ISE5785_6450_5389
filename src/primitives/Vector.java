package primitives;

import static java.lang.System.out;

/**
 * The {@code Vector} class represents a vector in 3D space.
 * A vector is essentially a point with direction and is used to represent directions and magnitudes in space.
 *
 * It extends {@link Point} but ensures that the zero vector is not allowed.
 *
 * @author @author Yokheved and Chaya
 */
public class Vector extends Point {

    /**
     * Constructs a {@code Vector} with the given 3D coordinates.
     * Throws an exception if the vector is a zero vector.
     *
     * @param xyz the coordinates of the vector
     * @throws IllegalArgumentException if the vector is a zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);

        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector zero is illegal");
        }
    }

    /**
     * Constructs a {@code Vector} with the given x, y, and z coordinates.
     * Throws an exception if the vector is a zero vector.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @throws IllegalArgumentException if the vector is a zero vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector zero is illegal");
        }
    }

    /**
     * Constructs a {@code Vector} with the given 3D coordinates.
     * Throws an exception if the vector is a zero vector.
     *
     * @param p the coordinates of the vector
     * @throws IllegalArgumentException if the vector is a zero vector
     */
    public Vector(Point p) {
        super(p.xyz);

        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector zero is illegal");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other) &&
                this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "xyz=" + xyz +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Returns the squared length (magnitude) of the vector.
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        return this.distanceSquared(Point.ZERO);
    }

    /**
     * Returns the length (magnitude) of the vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Adds this vector with another vector and returns the result.
     *
     * @param other the vector to add
     * @return the result of the addition
     * @throws IllegalArgumentException if the vectors are opposite (i.e., one is the negative of the other)
     */
    public Vector add(Vector other) {
        return new Vector(this.xyz.add(other.xyz));
    }

    /**
     * Scales the vector by a given scalar value.
     *
     * @param scale the scalar value to scale the vector by
     * @return the scaled vector
     */
    public Vector scale(double scale) {
        return new Vector(this.xyz.scale(scale));
    }

    /**
     * Computes the dot product of this vector and another vector.
     *
     * @param other the other vector
     * @return the dot product
     */
    public double dotProduct(Vector other) {
        return this.xyz.d1() * other.xyz.d1() +
                this.xyz.d2() * other.xyz.d2() +
                this.xyz.d3() * other.xyz.d3();
    }

    /**
     * Computes the cross product of this vector and another vector.
     *
     * @param other the other vector
     * @return the cross product as a new vector
     */
    public Vector crossProduct(Vector other) {
        return new Vector(
                this.xyz.d2() * other.xyz.d3() - this.xyz.d3() * other.xyz.d2(),
                this.xyz.d3() * other.xyz.d1() - this.xyz.d1() * other.xyz.d3(),
                this.xyz.d1() * other.xyz.d2() - this.xyz.d2() * other.xyz.d1()
        );
    }

    /**
     * Normalizes the vector to unit length.
     *
     * @return the normalized vector
     */
    public Vector normalize() {
        double len = this.length();
        return this.scale(1 / len);
    }
}
