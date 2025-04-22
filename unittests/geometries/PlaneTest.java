package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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

    /**
     * Test method for {@link Plane#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        // ======= Setup plane for testing =======
        Plane plane = new Plane(new Point(0, 0, 0), new Vector(0, 0, 1));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the plane
        Ray ray1 = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        List<Point> result1 = plane.findIntersections(ray1);
        assertNotNull(result1, "Ray should intersect the plane");
        assertEquals(1, result1.size(), "Should be exactly one intersection point");
        assertEquals(new Point(0, 0, 0), result1.get(0), "Wrong intersection point");

        // TC02: Ray starts after the plane and directed away - no intersection
        Ray ray9 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray9), "Ray starts after the plane and directed away - should return null");

        // =============== Boundary Values Tests ==================
        // TC11: Ray is parallel and included in the plane
        Ray ray2 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray2), "Ray lies inside the plane - should return null (no intersection)");

        // TC12: Ray is parallel and not included in the plane
        Ray ray3 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray3), "Ray parallel outside the plane - should return null");

        // TC13: Ray is orthogonal and starts before the plane
        Ray ray4 = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        List<Point> result4 = plane.findIntersections(ray4);
        assertNotNull(result4, "Orthogonal ray before the plane should intersect");
        assertEquals(1, result4.size(), "Should be exactly one intersection point");
        assertEquals(new Point(0, 0, 0), result4.get(0), "Wrong intersection point for orthogonal ray");

        // TC14: Ray is orthogonal and starts in the plane
        Ray ray5 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray5), "Orthogonal ray starting in the plane - should return null");

        // TC15: Ray is orthogonal and starts after the plane
        Ray ray6 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray6), "Orthogonal ray starting after the plane - should return null");


        // TC16: Ray neither orthogonal nor parallel, starts in the plane
        Ray ray7 = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray7), "Ray starts at the plane but moves away - should return null");

        // TC17: Ray begins exactly at the same reference point
        Ray ray8 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 1));
        assertNull(plane.findIntersections(ray8), "Ray starts exactly at the reference point - should return null");
    }
}