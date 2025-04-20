package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        //TC1: all points
        Point p1 = new Point(1, 0, 5);
        Vector normal1 = tube.getNormal(p1);
        assertEquals(new Vector(1, 0, 0), normal1, "Bad normal to tube at regular point");

        // =============== Boundary Values Tests ==================
        //TC2: when point is on the circle on P0
        Point p2 = new Point(0, 1, 0);
        Vector normal2 = tube.getNormal(p2);
        assertEquals(new Vector(0, 1, 0), normal2, "Bad normal to tube at point on base circle (P0)");

    }
}