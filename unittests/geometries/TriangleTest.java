package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    /**
     * test method for{@link Triangle#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC10: all triangles
        Triangle triangle = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );

        Vector normal = triangle.getNormal(new Point(0.5, 0.25, 0));

        // check orthogonality to the triangle's edges
        Vector edge1 = new Vector(1, 0, 0); // from p1 to p2
        Vector edge2 = new Vector(0, 1, 0); // from p1 to p3
        assertEquals(0, normal.dotProduct(edge1), 1e-10, "Normal not orthogonal to edge1");
        assertEquals(0, normal.dotProduct(edge2), 1e-10, "Normal not orthogonal to edge2");

        // check normalization
        assertEquals(1, normal.length(), 1e-10, "Normal is not a unit vector");

        // =============== Boundary Values Tests ==================
        //none
    }

    /**
     * Test method for {@link Triangle#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        // ======= Setup - יצירת משולש קבוע לבדיקה =======
        Triangle triangle = new Triangle(
                new Point(1, 1, 1),
                new Point(2, 1, 1),
                new Point(1, 2, 1)
        );

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray does not intersect the triangle's plane
        Ray ray1 = new Ray(new Point(1, 1, 2), new Vector(0, 1, 0));
        assertNull(triangle.findIntersections(ray1),
                "Ray is parallel to triangle's plane - should return null");

        // TC02: Ray intersects inside the triangle
        Ray ray2 = new Ray(new Point(1.25, 1.25, 2), new Vector(0, 0, -1));
        List<Point> result2 = triangle.findIntersections(ray2);
        assertNotNull(result2, "Ray should intersect inside the triangle");
        assertEquals(1, result2.size(), "Should be exactly one intersection point");
        assertEquals(new Point(1.25, 1.25, 1), result2.get(0),
                "Wrong intersection point inside the triangle");

        // TC03: Ray intersects the plane but outside against a side
        Ray ray3 = new Ray(new Point(0.5, 1.5, 2), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray3),
                "Ray hits plane outside triangle against side - should return null");

        // TC04: Ray intersects the plane but outside against a vertex
        Ray ray4 = new Ray(new Point(2, 2, 2), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray4),
                "Ray hits plane outside triangle against vertex - should return null");

        // =============== Boundary Values Tests ==================

        // TC11: Ray intersects exactly on the side of the triangle
        Ray ray11 = new Ray(new Point(1.5, 1, 2), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray11),
                "Ray hits exactly on triangle's side - should return null");

        // TC12: Ray intersects exactly on the vertex of the triangle
        Ray ray12 = new Ray(new Point(1, 1, 2), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray12),
                "Ray hits exactly on triangle's vertex - should return null");

        // TC13: Ray intersects along the extension of a side
        Ray ray13 = new Ray(new Point(2.5, 1, 2), new Vector(0, 0, -1));
        assertNull(triangle.findIntersections(ray13), "Ray hits extension of side - should return null");
    }

}