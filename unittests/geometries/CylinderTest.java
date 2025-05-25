package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    /**
     * test method for{@link Cylinder#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Cylinder cylinder = new Cylinder(5.0, axisRay, 1.0);


        // ============ Equivalence Partitions Tests ==============
        //TC01: on the round surface
        Point p1 = new Point(1, 0, 3);
        Vector normal1 = cylinder.getNormal(p1);
        assertEquals(new Vector(1, 0, 0), normal1, "Bad normal on the side surface of the cylinder");

        //TC02: on the base 1
        Point p2 = new Point(0, 1, 0);
        Vector normal2 = cylinder.getNormal(p2);
        assertEquals(new Vector(0, 0, -1), normal2, "Bad normal on the base 1 of the cylinder");

        //TC03: on the base 2
        Point p3 = new Point(0, 1, 5);
        Vector normal3 = cylinder.getNormal(p3);
        assertEquals(new Vector(0, 0, 1), normal3, "Bad normal on the base 2 of the cylinder");

        
        // =============== Boundary Values Tests ==================
        // TC01: in the center of base 1
        Point p4 = new Point(0, 0, 0); // במרכז הבסיס התחתון
        Vector normal4 = cylinder.getNormal(p4);
        assertEquals(new Vector(0, 0, -1), normal4, "Bad normal at the center of base 1");

        // TC02: in the center of base 2
        Point p5 = new Point(0, 0, 5); // במרכז הבסיס העליון
        Vector normal5 = cylinder.getNormal(p5);
        assertEquals(new Vector(0, 0, 1), normal5, "Bad normal at the center of base 2");

        //TC03: on the base 1 edge
        Point p6 = new Point(1, 0, 0); // על הקצה של הבסיס התחתון
        Vector normal6 = cylinder.getNormal(p6);
        assertEquals(new Vector(0, 0, -1), normal6, "Bad normal at the edge of base 1");

        //TC04: on the base 2 edge
        Point p7 = new Point(1, 0, 5); // על הקצה של הבסיס העליון
        Vector normal7 = cylinder.getNormal(p7);
        assertEquals(new Vector(0, 0, 1), normal7, "Bad normal at the edge of base 2");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        // ======= Setup - יצירת גליל סופי לבדיקה =======
        Cylinder cylinder = new Cylinder(
                2.0,
                new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),
                1.0
        );

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray crosses the cylinder's side - two intersection points expected
        Ray ray1 = new Ray(new Point(2, 0, 1), new Vector(-1, 0, 0));
        List<Point> result1 = cylinder.findIntersections(ray1);
        assertNotNull(result1, "Ray should cross the cylinder");
        assertEquals(2, result1.size(), "Should be exactly two intersection points on side");

        // TC02: Ray starts inside and crosses out through the side
        Ray ray2 = new Ray(new Point(0.5, 0, 1), new Vector(1, 0, 0));
        List<Point> result2 = cylinder.findIntersections(ray2);
        assertNotNull(result2, "Ray starts inside the cylinder - one intersection expected");
        assertEquals(1, result2.size(), "Should be exactly one intersection point");

        // TC03: Ray passes above cylinder without intersection
        Ray ray3 = new Ray(new Point(2, 0, 3), new Vector(-1, 0, 0));
        assertNull(cylinder.findIntersections(ray3), "Ray above the cylinder - should return null");

        // TC04: Ray intersects top base
//        Ray ray4 = new Ray(new Point(0.5, 0.5, 3), new Vector(0, 0, -1));
//        List<Point> result4 = cylinder.findIntersections(ray4);
//        assertNotNull(result4, "Ray should intersect top base");
//        assertEquals(1, result4.size(), "Should be exactly one intersection point (top base)");

        // TC05: Ray intersects bottom base
//        Ray ray5 = new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, 1));
//        List<Point> result5 = cylinder.findIntersections(ray5);
//        assertNotNull(result5, "Ray should intersect bottom base");
//        assertEquals(1, result5.size(), "Should be exactly one intersection point (bottom base)");

        // =============== Boundary Values Tests ==================

        // TC11: Ray hits exactly on the cylinder's side edge
        Ray ray11 = new Ray(new Point(1, 0, 1), new Vector(0, 1, 0));
        List<Point> result11 = cylinder.findIntersections(ray11);
        assertNull(result11, "Ray exactly on cylinder's side edge - no intersection expected");

        // TC12: Ray hits exactly at the center of top base
//        Ray ray12 = new Ray(new Point(0, 0, 3), new Vector(0, 0, -1));
//        List<Point> result12 = cylinder.findIntersections(ray12);
//        assertNotNull(result12, "Ray hitting center of top base - should intersect");
//        assertEquals(1, result12.size(), "Should be exactly one intersection point");

        // TC13: Ray outside cylinder and parallel to axis
        Ray ray13 = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        assertNull(cylinder.findIntersections(ray13), "Ray outside cylinder and parallel - should return null");

        // TC14: Ray tangent to cylinder and hits upper base edge
        Ray ray14 = new Ray(new Point(1, 1, 3), new Vector(0, 0, -1));
        List<Point> result14 = cylinder.findIntersections(ray14);
        assertNull(result14, "Ray tangent to side and hitting upper edge - should return null");
    }
    @Test
    void testCalculateIntersectionsHelper_Cylinder() {
        Cylinder cyl = new Cylinder(5.0, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1.0);
        Ray ray1 = new Ray(new Point(2, 0, 2), new Vector(-1, 0, 0)); // TC01: two side intersections
        assertEquals(2, cyl.calculateIntersectionsHelper(ray1, 5).size());

        Ray ray2 = new Ray(new Point(0, 0, 6), new Vector(0, 0, -1)); // TC02: intersects top base
        assertEquals(1, cyl.calculateIntersectionsHelper(ray2, 5).size());

        Ray ray3 = new Ray(new Point(0, 0, -1), new Vector(0, 0, -1)); // TC03: no intersection
        assertNull(cyl.calculateIntersectionsHelper(ray3, 5));
    }

}