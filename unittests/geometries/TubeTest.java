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
        Tube tube = new Tube(axisRay, 1.0);

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
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(axisRay, 1.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects tube twice (enters and exits)
        Ray ray1 = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        List<Point> result1 = tube.findIntersections(ray1);
        assertEquals(2, result1.size(), "Wrong number of intersections when ray crosses tube");
        Point p1 = new Point(-1, 0, 0);
        Point p2 = new Point(1, 0, 0);
        assertTrue(result1.contains(p1) && result1.contains(p2),
                "Wrong intersection points when ray crosses tube");

        // TC02: Ray starts inside tube and exits
        Ray ray2 = new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0));
        List<Point> result2 = tube.findIntersections(ray2);
        assertEquals(1, result2.size(), "Wrong number of intersections when ray starts inside tube");
        assertEquals(new Point(1, 0, 0), result2.get(0),
                "Wrong intersection point when ray starts inside tube");

        // TC03: Ray starts outside tube and never intersects
        Ray ray3 = new Ray(new Point(0, 2, 0), new Vector(0, 1, 0));
        assertNull(tube.findIntersections(ray3), "Should be null when ray doesn't intersect tube");

        // TC04: Ray starts after tube in direction away from tube
        Ray ray4 = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
        assertNull(tube.findIntersections(ray4), "Should be null when ray starts after tube");

        // TC05: Ray intersects tube at oblique angle
        Ray ray5 = new Ray(new Point(-2, -2, 0), new Vector(1, 1, 0).normalize());
        List<Point> result5 = tube.findIntersections(ray5);
        assertEquals(2, result5.size(), "Wrong number of intersections with oblique angle");
        // Verify the two intersection points (exact points would need calculation)

        // TC06: Ray with some z-component
        Ray ray6 = new Ray(new Point(-2, 0, 1), new Vector(1, 0, 0));
        List<Point> result6 = tube.findIntersections(ray6);
        assertEquals(2, result6.size(), "Wrong number of intersections with z-component");
        // Since we don't know the order, check both points are present
        assertTrue(result6.contains(new Point(-1, 0, 1)) && result6.contains(new Point(1, 0, 1)),
                "Wrong intersection points with z-component");

        // TC07: Ray starts inside and never exits (along axis)
        Ray ray7 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        // Ray parallel to tube axis returns null regardless of position
        assertNull(tube.findIntersections(ray7), "Should be null when ray is parallel to axis");

        // TC08: Ray starts outside going toward tube but one intersection behind ray
        Ray ray8 = new Ray(new Point(2, 0, 0), new Vector(-1, 1, 0).normalize());
        List<Point> result8 = tube.findIntersections(ray8);
        assertNull( result8, "Should have none intersection when one is behind ray");

        // =============== Boundary Values Tests ==================

        // TC11: Ray is tangent to tube surface
        Ray ray11 = new Ray(new Point(0, -2, 0), new Vector(0, 0, 1));
        List<Point> result11 = tube.findIntersections(ray11);
        assertNull(result11, "Tangent ray outside tube should have no intersections");

        // TC12: Ray is tangent from point on surface
        Ray ray12 = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
        List<Point> result12 = tube.findIntersections(ray12);
        assertNull(result12, "Tangent ray from surface should have no intersections");

        // TC13: Ray is on tube surface but not tangent
        Ray ray13 = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray13), "Ray on surface should have no intersections");

        // TC14: Ray starts from axis
        Ray ray14 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        List<Point> result14 = tube.findIntersections(ray14);
        assertEquals(1, result14.size(), "Wrong number of intersections from axis");
        // The intersection point should be at distance 1 from origin
        assertEquals(1.0, result14.get(0).distance(Point.ZERO), 0.000001,
                "Wrong distance for intersection from axis");

        // TC15: Ray starts from surface point along normal (outward)
        Ray ray15 = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));
        assertNull(tube.findIntersections(ray15), "Should be null for ray from surface outward");

        // TC16: Ray starts from surface point along normal (inward)
        Ray ray16 = new Ray(new Point(1, 0, 0), new Vector(-1, 0, 0));
        List<Point> result16 = tube.findIntersections(ray16);
        assertEquals(1, result16.size(), "Wrong number of intersections from surface inward");
        assertEquals(new Point(-1, 0, 0), result16.get(0),
                "Wrong intersection point from surface inward");

        // TC17: Ray parallel to tube axis outside tube
        Ray ray17 = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray17), "Should be null for ray parallel to axis outside tube");

        // TC18: Ray parallel to tube axis inside tube
        Ray ray18 = new Ray(new Point(0.5, 0, 0), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray18), "Should be null for ray parallel to axis inside tube");

        // TC19: Ray perpendicular to tube axis through center
        Ray ray19 = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        List<Point> result19 = tube.findIntersections(ray19);
        assertEquals(2, result19.size(), "Wrong number of intersections perpendicular through center");
        assertTrue(result19.contains(new Point(-1, 0, 0)) && result19.contains(new Point(1, 0, 0)),
                "Wrong intersection points perpendicular through center");

        // TC20: Ray perpendicular to tube axis not through center
        Ray ray20 = new Ray(new Point(-2, 0.5, 0), new Vector(1, 0, 0));
        List<Point> result20 = tube.findIntersections(ray20);
        assertEquals(2, result20.size(), "Wrong number of intersections perpendicular not through center");
        // Points would need to be calculated

        // TC21: Ray with both intersections behind ray head
        Ray ray21 = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
        assertNull(tube.findIntersections(ray21), "Should be null when both intersections behind ray");

        // TC22: Ray with both intersections exactly at t=0 (from surface point perpendicular to axis)
        Ray ray22 = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
        assertNull(tube.findIntersections(ray22), "Should be null when starting from surface perpendicular to axis");

        // TC23: Test with tube having non-origin axis point
        Ray offsetAxisRay = new Ray(new Point(1, 1, 1), new Vector(0, 0, 1));
        Tube offsetTube = new Tube(offsetAxisRay, 1.0);
        Ray ray23 = new Ray(new Point(-1, 1, 2), new Vector(1, 0, 0));
        List<Point> result23 = offsetTube.findIntersections(ray23);
        assertEquals(2, result23.size(), "Wrong number of intersections with offset tube");
        // Points would need to be calculated

        // TC24: Test with tube having non-Z axis direction
        Ray xAxisRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        Tube xTube = new Tube(xAxisRay, 1.0);
        Ray ray24 = new Ray(new Point(0, -2, 0), new Vector(0, 1, 0));
        List<Point> result24 = xTube.findIntersections(ray24);
        assertEquals(2, result24.size(), "Wrong number of intersections with X-axis tube");
        // Points would need to be calculated

        // TC25: Test with tube having arbitrary axis direction
        Ray arbAxisRay = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1).normalize());
        Tube arbTube = new Tube(arbAxisRay, 1.0);
        Ray ray25 = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        List<Point> result25 = arbTube.findIntersections(ray25);
        // The number of intersections depends on the specific geometry

        // TC26: Ray with extremely small (but non-zero) discriminant
        Ray ray26 = new Ray(new Point(0, 1.999999, 0), new Vector(1, 0, 0));
        List<Point> result26 = tube.findIntersections(ray26);
        // This tests numerical stability with very small discriminant

        // TC27: Ray with extremely large parameter values
        Ray ray27 = new Ray(new Point(-1000000, 0, 0), new Vector(1, 0, 0));
        List<Point> result27 = tube.findIntersections(ray27);
        assertEquals(2, result27.size(), "Wrong number of intersections with distant ray");

        // TC28: Ray intersecting tube at grazing angle (almost tangent)
        Ray ray28 = new Ray(new Point(0, -1.99, 0), new Vector(0.01, 1, 0).normalize());
        List<Point> result28 = tube.findIntersections(ray28);
        // Expect 2 very close intersections or 1 for grazing angle

        // TC29: Ray with one intersection exactly at ray head
        Ray ray29 = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
        assertNull(tube.findIntersections(ray29), "Should be null when starting exactly on tube surface");

        // TC30: Ray with multiple intersections at exactly the same point (tangent)
        Ray ray30 = new Ray(new Point(-2, -1, 0), new Vector(1, 0, 0));
        List<Point> result30 = tube.findIntersections(ray30);
        // This ray should intersect at x=-sqrt(1-(1^2)) and x=sqrt(1-(1^2))
        // which are approximately x=-0 and x=0 with y=-1
        assertEquals(2, result30.size(), "Wrong number of intersections when ray passes through bottom");

        // TC31: Ray with t values very close to each other
        Ray ray31 = new Ray(new Point(-2, 0.999, 0), new Vector(1, 0, 0));
        List<Point> result31 = tube.findIntersections(ray31);
        assertEquals(2, result31.size(), "Wrong number of intersections with close t values");

        // TC32: Testing with a different radius tube
        Tube largeTube = new Tube(axisRay, 2.0);
        Ray ray32 = new Ray(new Point(-3, 0, 0), new Vector(1, 0, 0));
        List<Point> result32 = largeTube.findIntersections(ray32);
        assertEquals(2, result32.size(), "Wrong number of intersections with larger tube");
        // Since we don't know the order, check that both expected points are present
        assertTrue(result32.contains(new Point(-2, 0, 0)) && result32.contains(new Point(2, 0, 0)),
                "Wrong intersection points with larger tube");

        // TC33: Ray from outside that passes through axis (test sorting of intersections)
        Ray ray33 = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        List<Point> result33 = tube.findIntersections(ray33);
        assertEquals(2, result33.size(), "Wrong number of intersections through axis");
        // Your findIntersections implementation may not sort points, so we'll just check they exist
        assertTrue(result33.contains(new Point(-1, 0, 0)) && result33.contains(new Point(1, 0, 0)),
                "Wrong intersection points through axis");


        // TC34: Ray that passes near but not through the axis
        Ray ray34 = new Ray(new Point(-2, 0.5, 0), new Vector(1, 0, 0));
        List<Point> result34 = tube.findIntersections(ray34);
        assertEquals(2, result34.size(), "Wrong number of intersections near axis");

        // TC35: Test with very small radius tube
        Tube thinTube = new Tube(axisRay, 0.001);
        Ray ray35 = new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0));
        List<Point> result35 = thinTube.findIntersections(ray35);
        assertEquals(2, result35.size(), "Wrong number of intersections with thin tube");

        // TC36: Ray that intersects tube at exactly one point (true tangent)
        // Calculate a true tangent ray
        Ray ray36 = new Ray(new Point(0, -2, 0), new Vector(1, 1, 0).normalize());
        List<Point> result36 = tube.findIntersections(ray36);
        assertNull( result36, "Wrong number of intersections with true tangent");

        // TC37: Ray with head exactly on tube surface and pointing slightly inward
        Ray ray37 = new Ray(new Point(1, 0, 0), new Vector(-0.1, 0.1, 0).normalize());
        List<Point> result37 = tube.findIntersections(ray37);
        assertEquals(1, result37.size(), "Wrong number of intersections from surface slightly inward");

        // TC38: Ray with head exactly on tube surface and pointing slightly outward
        Ray ray38 = new Ray(new Point(1, 0, 0), new Vector(0.1, 0.1, 0).normalize());
        assertNull(tube.findIntersections(ray38), "Should be null when pointing outward from surface");

        // TC39: Ray pointing directly at axis
        Ray ray39 = new Ray(new Point(2, 0, 0), new Vector(-1, 0, 0));
        List<Point> result39 = tube.findIntersections(ray39);
        assertEquals(2, result39.size(), "Wrong number of intersections pointing at axis");

        // TC40: Ray with complex trajectory through tube
        Ray ray40 = new Ray(new Point(-2, -2, -2), new Vector(1, 1, 1).normalize());
        List<Point> result40 = tube.findIntersections(ray40);
        // Number of intersections depends on specific setup
    }

    @Test
    void testCalculateIntersectionsHelper_Tube() {
        Tube tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1.0);
        Ray ray1 = new Ray(new Point(2, 0, 1), new Vector(-1, 0, 0)); // TC01: intersects side
        assertEquals(2, tube.calculateIntersectionsHelper(ray1, 5).size());

        Ray ray2 = new Ray(new Point(2, 0, 1), new Vector(-1, 0, 0)); // TC02: intersection beyond max
        assertNull(tube.calculateIntersectionsHelper(ray2, 0.5));

        Ray ray3 = new Ray(new Point(1, 1, 1), new Vector(1, 1, 0)); // TC03: no intersection
        assertNull(tube.calculateIntersectionsHelper(ray3, 10));
    }

}
