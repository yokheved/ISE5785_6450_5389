package geometries;

import primitives.Point;

/**
 * The {@code Triangle} class represents a triangle in 3D space.
 * It extends {@link Polygon} and is always defined by exactly three points.
 *
 * @author Your Name
 */
public class Triangle extends Polygon {

    /**
     * Constructs a {@code Triangle} with three given points.
     *
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     *
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3); // Calls the Polygon constructor

    }
}
