package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    /**
     * test method for{@link Sphere#getNormal(Point)} }
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC1: all point sphere
        Sphere sphere = new Sphere( new Point(0, 0, 0),1.0);
        Point p = new Point(0, 0, 1); // נקודה על פני הכדור בציר Z
        Vector normal = sphere.getNormal(p);
        assertEquals(new Vector(0, 0, 1), normal, "Bad normal to sphere surface point");

        // =============== Boundary Values Tests ==================
        // none
    }
}