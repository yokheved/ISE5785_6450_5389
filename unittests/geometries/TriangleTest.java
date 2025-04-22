package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

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
}