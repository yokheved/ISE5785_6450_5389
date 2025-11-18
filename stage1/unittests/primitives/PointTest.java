package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class PointTest {

    /**
     * test method for{@link Point#add(Vector)}
     */
    @Test
    void testAdd() {
        Point p1 = new Point(1, 2, 3);
        Vector v2 = new Vector(0, 2 , 9);
        // ============ Equivalence Partitions Tests ==============
        //TC01: all rational numbers
        assertEquals(new Point(1+0, 2+2, 3+9), p1.add(v2), "adding vector to point was unexpected");
        // =============== Boundary Values Tests ==================
        //none
    }

    /**
     * test method for{@link Point#subtract(Point)}
     */
    @Test
    void testSubtract() {
        Point p1 = new Point(3,5,-7);
        Point p2 = new Point(5, 0, 2);
        // ============ Equivalence Partitions Tests ==============
        //TC01: all rational numbers
        assertEquals(new Vector(3-5, 5-0, -7-2), p1.subtract(p2), "subtracting point with point was unexpected");
        // =============== Boundary Values Tests ==================
        //--none
    }

    /**
     * test method for{@link Point#distanceSquared(Point)}
     */
    @Test
    void testDistanceSquared() {
        Point p1 = new Point(3,5,-7);
        Point p2 = new Point(5, 0, 2);
        // ============ Equivalence Partitions Tests ==============
        //TC01: all rational coordinates
        assertTrue(isZero((3-5)*(3-5)+(5-0)*(5-0)+(-7-2)*(-7-2) - p1.distanceSquared(p2)));
        // =============== Boundary Values Tests ==================
        //TC11: the points are the same
        assertTrue(isZero(p1.distanceSquared(p1)));

    }

    /**
     * test method for{@link Point#distance(Point)}
     */
    @Test
    void testDistance() {
        Point p1 = new Point(3,5,-7);
        Point p2 = new Point(5, 0, 2);
        // ============ Equivalence Partitions Tests ==============
        //TC01: all rational coordinates
        assertTrue(isZero(Math.sqrt((3-5)*(3-5)+(5-0)*(5-0)+(-7-2)*(-7-2))
                - p1.distance(p2)));
        // =============== Boundary Values Tests ==================
        //TC11: the points are the same
        assertTrue(isZero(p1.distance(p1)));
    }
}