package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    /**
     * test method for{@link Ray#getPoint(double)}
     */
    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        // ============ Equivalence Partitions Tests ==============
        //TC01: t is positive
        Point result1 = ray.getPoint(1);
        assertEquals(new Point(0, 0, 2), result1, "TC01 failed");
        //TC02: t is negative
        assertThrows(IllegalArgumentException.class, () -> {
            ray.getPoint(-1);
        }, "TC02 failed");
        // =============== Boundary Values Tests ==================
        //TC11: t equals 0
        Point result2 = ray.getPoint(0);
        assertEquals(new Point(0, 0, 1), result2, "TC11 failed");
    }

    /**
     * test method for{@link Ray#findClosestPoint(List)}
     */
    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============

        //TC01: the closest point in the middle of the list
        List<Point> points1 = List.of(
                new Point(5, 1, 0),
                new Point(2, 0.5, 0),
                new Point(3, 0, 0) // קרובה ביותר ל-(0,0,0)
        );
        Point result1 = ray.findClosestPoint(points1);
        assertEquals(new Point(2, 0.5, 0), result1, "TC01 failed");

        // =============== Boundary Values Tests ==================

        //TC11: empty list return null
        List<Point> emptyList = List.of();
        Point result2 = ray.findClosestPoint(emptyList);
        assertNull(result2, "TC11 failed");

        //TC12: the closest point in the beginning of the list
        List<Point> points2 = List.of(
                new Point(1, 0, 0), // קרובה ביותר ל-(0,0,0)
                new Point(3, 0, 0),
                new Point(5, 0, 0)
        );
        Point result3 = ray.findClosestPoint(points2);
        assertEquals(new Point(1, 0, 0), result3, "TC12 failed");

        //TC13: the closest point in the end of the list
        List<Point> points3 = List.of(
                new Point(5, 0, 0),
                new Point(4, 0, 0),
                new Point(1, 0, 0) // קרובה ביותר ל-(0,0,0)
        );
        Point result4 = ray.findClosestPoint(points3);
        assertEquals(new Point(1, 0, 0), result4, "TC13 failed");
    }
}