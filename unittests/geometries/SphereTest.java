package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
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

    /**
     * test method for {@link Sphere#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1d);
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
        assertEquals(
                List.of(gp2),
                sphere.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(1, 1, 0))),
                "Ray from inside sphere"
        );

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(2, 2, 0), new Vector(1, 1, 0))),
                "Ray starts after sphere");

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray's line crosses the sphere (but not the center)

        // TC11: Ray starts at sphere and goes inside (1 point)
        assertEquals(
                List.of(gp2),
                sphere.findIntersections(new Ray(gp1, new Vector(3, 1, 0))),
                "Ray from surface into sphere"
        );

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(gp1, new Vector(-3, -1, 0))),
                "Ray from surface outward");

        // **** Group 2: Ray's line goes through the center

        // TC21: Ray starts before the sphere (2 points)
        assertEquals(
                List.of(new Point(-1, 0, 0), new Point(1, 0, 0)),
                sphere.findIntersections(new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0))),
                "Ray through center from before"
        );

        // TC22: Ray starts at sphere and goes inside (1 point)
        assertEquals(
                List.of(new Point(1, 0, 0)),
                sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))),
                "Ray from center outward"
        );

        // TC23: Ray starts inside (1 point)
        assertEquals(
                List.of(new Point(1, 0, 0)),
                sphere.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0))),
                "Ray from inside to outside"
        );

        // TC24: Ray starts at the center (1 point)
        assertEquals(
                List.of(new Point(1, 0, 0)),
                sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))),
                "Ray from center to surface"
        );

        // TC25: Ray starts at sphere and goes outside (0 points)
        assertNull(
                sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0))),
                "Ray from surface outward through center direction"
        );

        // TC26: Ray starts after sphere (0 points)
        assertNull(
                sphere.findIntersections(new Ray(new Point(2, 0, 0), new Vector(1, 0, 0))),
                "Ray from outside through center direction"
        );

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)

        // TC31: Ray starts before the tangent point
        assertNull(
                sphere.findIntersections(new Ray(new Point(0, -1, -1), new Vector(0, 0, 1))),
                "Ray before tangent point"
        );

        // TC32: Ray starts at the tangent point
        assertNull(
                sphere.findIntersections(new Ray(new Point(0, -1, 0), new Vector(0, 0, 1))),
                "Ray at tangent point"
        );

        // TC33: Ray starts after the tangent point
        assertNull(
                sphere.findIntersections(new Ray(new Point(0, -1, 1), new Vector(0, 0, 1))),
                "Ray after tangent point"
        );

        // **** Group 4: Special cases

        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        assertNull(
                sphere.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 1, 0))),
                "Orthogonal ray from outside"
        );

        // TC42: Ray's starts inside, ray is orthogonal to ray star
        assertEquals(
                List.of(new Point(0, 1, 0)),
                sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 1, 0))),
                "Orthogonal ray from center"
        );
    }
}