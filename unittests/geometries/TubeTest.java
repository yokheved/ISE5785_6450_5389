package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    /**
     * test method for{@link Tube#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube( axisRay,1.0);

        // ============ Equivalence Partitions Tests ==============
        //TC01: all points
        Point p1 = new Point(1, 0, 5);
        Vector normal1 = tube.getNormal(p1);
        assertEquals(new Vector(1, 0, 0), normal1, "Bad normal to tube at regular point");

        // =============== Boundary Values Tests ==================
        //TC11: when point is on the circle on P0
        Point p2 = new Point(0, 1, 0);
        Vector normal2 = tube.getNormal(p2);
        assertEquals(new Vector(0, 1, 0), normal2, "Bad normal to tube at point on base circle (P0)");

    }

    /**
     * Test method for {@link Tube#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        // ======= Setup - tube for testing =======
        Tube tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray crosses the tube - two intersection points expected
        Ray ray1 = new Ray(new Point(2, 0, 0), new Vector(-1, 0, 0));
        List<Point> result1 = tube.findIntersections(ray1);
        assertNotNull(result1, "Ray should cross the tube");
        assertEquals(2, result1.size(), "Should be exactly two intersection points");

        // TC02: Ray starts inside the tube and crosses outside - one intersection point
        Ray ray2 = new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0));
        List<Point> result2 = tube.findIntersections(ray2);
        assertNotNull(result2, "Ray starts inside and should have one intersection");
        assertEquals(1, result2.size(), "Should be exactly one intersection point");

        // TC03: Ray misses the tube entirely - no intersections
        Ray ray3 = new Ray(new Point(2, 2, 0), new Vector(1, 0, 0));
        assertNull(tube.findIntersections(ray3), "Ray misses the tube - should return null");

        // TC04: Ray tangent to the tube - one touching point
        Ray ray4 = new Ray(new Point(1, -1, 0), new Vector(0, 1, 0));
        List<Point> result4 = tube.findIntersections(ray4);
        assertNotNull(result4, "Ray is tangent - should have exactly one intersection");
        assertEquals(1, result4.size(), "Should be exactly one tangent point");

        // =============== Boundary Values Tests ==================

        // TC11: Ray parallel to the tube axis and outside - no intersection
        Ray ray11 = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray11), "Ray parallel to tube axis and outside - should return null");

        // TC12: Ray parallel to the tube axis and on surface - no intersection
        Ray ray12 = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray12), "Ray parallel and on surface - should return null (touching only)");

        // TC13: Ray inside the tube and parallel to axis - no intersection (infinite containment isn't considered here)
        Ray ray13 = new Ray(new Point(0.5, 0, 0), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray13), "Ray inside and parallel to axis - no intersection with sides");

        // TC14: Ray orthogonal to tube axis and passes through center
        Ray ray14 = new Ray(new Point(0, -2, 0), new Vector(0, 1, 0));
        List<Point> result14 = tube.findIntersections(ray14);
        assertNotNull(result14, "Orthogonal ray through center should intersect");
        assertEquals(2, result14.size(), "Should have exactly two intersection points");
    }
}