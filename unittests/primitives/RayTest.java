package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    /**
     * test method for{@link Ray#getPoint(double)}
     */
    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(0,0,1), new Vector(0,0,1));
        // ============ Equivalence Partitions Tests ==============
        //TC01: t is positive
        Point result1 = ray.getPoint(1);
        assertEquals(new Point(0,0,2), result1, "TC01 failed");
        //TC02: t is negative
        assertThrows(IllegalArgumentException.class, ()-> {ray.getPoint(-1);}, "TC02 failed");
        // =============== Boundary Values Tests ==================
        //TC11: t equals 0
        Point result2 = ray.getPoint(0);
        assertEquals(new Point(0,0,1), result2, "TC11 failed");
    }
}