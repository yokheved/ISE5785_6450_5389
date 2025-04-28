package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        // build Geometries collection with 4 Triangles and 1 plane for testing
        Geometries geometries = new Geometries(
                new Triangle(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1)),
                new Triangle(new Point(1, 0, 2), new Point(2, 0, 2), new Point(1, 1, 2)),
                new Triangle(new Point(2, 0, 3), new Point(3, 0, 3), new Point(2, 1, 3)),
                new Triangle(new Point(3, 0, 4), new Point(4, 0, 4), new Point(3, 1, 4)),
                new Plane(new Point(0, 0, 5), new Vector(0, 0, 1))
        );

        // ============ Equivalence Partitions Tests ==============
        // TC01: some geometries are intersected but not all
        Ray ray1 = new Ray(new Point(0, 0.25, 0.5), new Vector(1, 0.1, 0.5));
        assertEquals(1, geometries.findIntersections(ray1).size(), "TC01 failed");

        // =============== Boundary Values Tests ==================
        // TC11: collection empty
        Geometries emptyGeometries = new Geometries();
        assertNull(emptyGeometries.findIntersections(ray1), "TC11 failed");

        // TC12: no geometry is intersected
        Ray ray2 = new Ray(new Point(-1, -1, 0), new Vector(0, 0, -1));
        assertNull(geometries.findIntersections(ray2), "TC12 failed");

        // TC13: only one geometry is intersected
        Ray ray3 = new Ray(new Point(1.5, 0.5, 0), new Vector(0, 0, 1));
        assertEquals(1, geometries.findIntersections(ray3).size(), "TC13 failed");

        // TC14: all geometries are intersected
        Ray ray4 = new Ray(new Point(-0.7, 0.3, 0), new Vector(1, 0, 1));
        assertEquals(5, geometries.findIntersections(ray4).size(), "TC14 failed");
    }

}