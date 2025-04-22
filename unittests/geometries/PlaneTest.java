package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    /**
     * test method for{@link Plane#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: all points
        Plane plane = new Plane(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );
        Vector normal = plane.getNormal(new Point(1, 1, 0));

        // check that the normal is orthogonal to both vectors in the plane
        Vector v1 = new Vector(1, 0, 0);
        Vector v2 = new Vector(0, 1, 0);
        assertEquals(0, normal.dotProduct(v1), 1e-10, "Normal not orthogonal to vector v1");
        assertEquals(0, normal.dotProduct(v2), 1e-10, "Normal not orthogonal to vector v2");

        // check that the normal is normalized
        assertEquals(1, normal.length(), 1e-10, "Normal is not a unit vector");

        // =============== Boundary Values Tests ==================
        //none
    }

    /**
     * test method for{@link Plane#Plane(Point, Point, Point)}
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: all points
        //need to check the normal is orthogonal to two vectors(between the points)
        //and normal.length() is one
        //(make sure the cross product between two vectors between two points won't be 1
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);
        Vector normal = plane.getNormal(p1);

        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        assertEquals(0, normal.dotProduct(v1), 1e-10, "Normal not orthogonal to v1");
        assertEquals(0, normal.dotProduct(v2), 1e-10, "Normal not orthogonal to v2");
        assertEquals(1, normal.length(), 1e-10, "Normal is not a unit vector");

        // =============== Boundary Values Tests ==================
        //all Boundary Values TCs check for illegal argument exception

        //TC11: p1 and p2 are same
        assertThrows(IllegalArgumentException.class, () ->
                new Plane(new Point(1, 1, 1), new Point(1, 1, 1), new Point(0, 1, 0))
                ,"failure text");

        //TC12: p1 and p3 are same
        assertThrows(IllegalArgumentException.class, () ->
                new Plane(new Point(1, 1, 1), new Point(0, 1, 0), new Point(1, 1, 1))
                ,"failure text");

        //TC13: p2 and p3 are same
        assertThrows(IllegalArgumentException.class, () ->
                new Plane(new Point(0, 1, 0), new Point(1, 1, 1), new Point(1, 1, 1))
                ,"failure text");

        //TC14: p1 p2 and p3 are same
        assertThrows(IllegalArgumentException.class, () ->
                new Plane(new Point(1, 1, 1), new Point(1, 1, 1), new Point(1, 1, 1))
                ,"failure text");

        //TC15: all points are on the same line
        assertThrows(IllegalArgumentException.class, () ->
                new Plane(new Point(0, 0, 0), new Point(1, 1, 1), new Point(2, 2, 2))
                ,"failure text");
    }
}