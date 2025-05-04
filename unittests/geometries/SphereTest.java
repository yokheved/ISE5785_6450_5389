package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    /**
     * test method for{@link Sphere#getNormal(Point)} }
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: all point sphere
        Sphere sphere = new Sphere( new Point(0, 0, 0),1.0);
        Point p = new Point(0, 0, 1); // נקודה על פני הכדור בציר Z
        Vector normal = sphere.getNormal(p);
        assertEquals(new Vector(0, 0, 1), normal, "Bad normal to sphere surface point");

        // =============== Boundary Values Tests ==================
        // none
    }

    /** A point used in some tests */
    private final Point p001 = new Point(0, 0, 1);
    /** A point used in some tests */
    private final Point p100 = new Point(1, 0, 0);
    /** A vector used in some tests */
    private final Vector v001 = new Vector(0, 0, 1);

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        Point inside = new Point(1.5, 0, 0); // Point inside the sphere
        Vector vOut = new Vector(1, 0, 0);   // Direction vector pointing outside
        final var result2 = sphere.findIntersections(new Ray(inside, vOut));
        assertNotNull(result2, "Can't be empty list");
        assertEquals(1, result2.size(), "Wrong number of points");

        // TC04: Ray starts after the sphere (0 points)
        Point after = new Point(3, 0, 0);    // Point outside and after the sphere
        assertNull(sphere.findIntersections(new Ray(after, vOut)), "Ray starts after sphere");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        Point onSphere1 = new Point(0, 0, 0); // Point on the sphere boundary
        Vector vInside = new Vector(1, 1, 0); // Direction vector pointing inside
        final var result3 = sphere.findIntersections(new Ray(onSphere1, vInside));
        assertNotNull(result3, "Can't be empty list");
        assertEquals(1, result3.size(), "Wrong number of points");

        // TC12: Ray starts at sphere and goes outside (0 points)
        Vector vOutside = new Vector(-1, -1, 0); // Direction vector pointing outside
        assertNull(sphere.findIntersections(new Ray(onSphere1, vOutside)), "Ray starts at sphere and goes outside");

        // **** Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        Point beforeCenter = new Point(-1, 0, 0); // Point before the sphere
        Vector vCenter = new Vector(1, 0, 0);    // Direction vector through center
        final var result4 = sphere.findIntersections(new Ray(beforeCenter, vCenter));
        assertNotNull(result4, "Can't be empty list");
        assertEquals(2, result4.size(), "Wrong number of points");

        // TC22: Ray starts at sphere and goes inside (1 points)
        Point onSphere2 = new Point(0, 0, 0);    // Point on the sphere boundary
        final var result5 = sphere.findIntersections(new Ray(onSphere2, vCenter));
        assertNotNull(result5, "Can't be empty list");
        assertEquals(1, result5.size(), "Wrong number of points");

        // TC23: Ray starts inside (1 points)
        Point insideCenter = new Point(0.5, 0, 0); // Point inside the sphere
        final var result6 = sphere.findIntersections(new Ray(insideCenter, vCenter));
        assertNotNull(result6, "Can't be empty list");
        assertEquals(1, result6.size(), "Wrong number of points");

        // TC24: Ray starts at the center (1 points)
        Point center = new Point(1, 0, 0);    // The center of the sphere
        Vector vFromCenter = new Vector(1, 0, 0);  // Any direction from center
        final var result7 = sphere.findIntersections(new Ray(center, vFromCenter));
        assertNotNull(result7, "Can't be empty list");
        assertEquals(1, result7.size(), "Wrong number of points");

        // TC25: Ray starts at sphere and goes outside (0 points)
        Point onSphere3 = new Point(2, 0, 0);    // Point on the sphere boundary
        Vector vOutFromCenter = new Vector(1, 0, 0); // Direction vector away from center
        assertNull(sphere.findIntersections(new Ray(onSphere3, vOutFromCenter)), "Ray starts at sphere and goes outside");

        // TC26: Ray starts after sphere (0 points)
        Point afterSphere = new Point(3, 0, 0);    // Point after the sphere
        assertNull(sphere.findIntersections(new Ray(afterSphere, vCenter)), "Ray starts after sphere");

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // TC31: Ray starts before the tangent point
        Point beforeTangent = new Point(2, 1, 0);   // Point before tangent
        Vector vTangent = new Vector(-1, 0, 0);      // Direction vector tangent to sphere
        assertNull(sphere.findIntersections(new Ray(beforeTangent, vTangent)), "Ray is tangent to sphere");

        // TC32: Ray starts at the tangent point
        Point tangentPoint = new Point(1, -1, 0);    // The tangent point
        assertNull(sphere.findIntersections(new Ray(tangentPoint, vTangent)), "Ray starts at tangent point");

        // TC33: Ray starts after the tangent point
        Point afterTangent = new Point(0, 0, 0);     // Point after tangent
        assertNull(sphere.findIntersections(new Ray(afterTangent, vTangent)), "Ray starts after tangent point");

        // **** Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        Point outsideOrthogonal = new Point(3, 0, 0);   // Point outside sphere
        Vector vOrthogonal = new Vector(0, 1, 0);      // Direction vector orthogonal to ray start-center line
        assertNull(sphere.findIntersections(new Ray(outsideOrthogonal, vOrthogonal)),
                "Ray orthogonal to ray start-center line");

        // TC42: Ray starts inside, ray is orthogonal to ray start to sphere's center line
        Point insideOrthogonal = new Point(1.5, 0, 0);   // Point inside sphere
        Vector vInsideOrthogonal = new Vector(0, 1, 0);  // Direction vector orthogonal to ray start-center line
        final var result8 = sphere.findIntersections(new Ray(insideOrthogonal, vInsideOrthogonal));
        assertNotNull(result8, "Can't be empty list");
        assertEquals(1, result8.size(), "Wrong number of points");
    }

}