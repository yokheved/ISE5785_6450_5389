package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    /**
     * test method for{@link Vector#add(Vector)}
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        // TC1: vectors that create an obtuse angle
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -1, 0);
        Vector result1 = v1.add(v2);
        assertEquals(new Vector(-1, 1, 3), result1,
                "vectors that create an obtuse angle failed");

        // TC2: vectors that create an acute angle
        Vector v3 = new Vector(2, 1, 0);
        Vector v4 = new Vector(1, 2, 3);
        Vector result2 = v3.add(v4);
        assertEquals(new Vector(3, 3, 3), result2,
                "vectors that create an acute angle failed");

        // =============== Boundary Values Tests ==================

        // TC3: vectors that are opposites
        Vector v5 = new Vector(1, 2, 3);
        Vector v6 = new Vector(-1, -2, -3);
        assertThrows(IllegalArgumentException.class, () -> v5.add(v6), "TC3 failed: expected exception when adding opposite vectors");

    }


    /**
     * test method for{@link Vector#subtract(Point)}
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: vectors that create an obtuse angle
        Vector v1 = new Vector(1, 2, 3);
        Point p2 = new Point(-2, -1, 0);
        Vector result1 = v1.subtract(p2);
        assertEquals(new Vector(3, 3, 3), result1,
                "vectors that create an obtuse angle failed");

        // TC02: vectors that create an acute angle
        Vector v3 = new Vector(2, 1, 0);
        Point p4 = new Point(1, 2, 3);
        Vector result2 = v3.subtract(p4);
        assertEquals(new Vector(1, -1, -3), result2,
                "vectors that create an acute angle failed");


        // =============== Boundary Values Tests ==================
        // TC11: vectors with same direction and same length
        Vector v5 = new Vector(1, 2, 3);
        Point p6 = new Point(1, 2, 3);

        assertThrows(IllegalArgumentException.class, () -> {
            Vector result = v5.subtract(p6);
        },"failure text");
    }

    /**
     * test method for{@link Vector#distanceSquared(Point)}
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: all vectors
        Vector v1 = new Vector(1, 2, 3);
        Point p2 = new Point(-2, -1, 0);
        double result1 = v1.distanceSquared(p2);
        assertEquals(27.0, result1,
                "all vectors distance squares failed");

        // =============== Boundary Values Tests ==================
        // TC11: the same vector
        Vector v3 = new Vector(1, 2, 3);
        double result2 = v3.distanceSquared(v3);
        assertEquals(0.0, result2,
                "the same vector distance squares failed");
    }

    /**
     * test method for{@link Vector#distance(Point)}
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: all vectors
        Vector v1 = new Vector(1, 2, 3);
        Point p2 = new Point(-2, -1, 0);
        double result1 = v1.distance(p2);
        assertEquals(Math.sqrt(27.0), result1,
                "all vectors distance failed");

        // =============== Boundary Values Tests ==================
        // TC11: the same vector
        Vector v3 = new Vector(1, 2, 3);
        double result2 = v3.distance(v3);
        assertEquals(0.0, result2,
                "the same vector distance failed");
    }

    /**
     * test method for{@link Vector#lengthSquared()}
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: all vectors
        Vector v1 = new Vector(1, 2, 3);
        double result1 = v1.lengthSquared();
        assertEquals(14.0, result1,
                "all vectors length squares failed");

        // =============== Boundary Values Tests ==================
        // TC11: vector with length zero
        assertThrows(IllegalArgumentException.class, () -> {
            Vector v2 = new Vector(0, 0, 0);
            v2.lengthSquared();
        },"failure text");
    }

    /**
     * test method for{@link Vector#length()}
     */
    @Test
    void testLength() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: all vectors
        Vector v1 = new Vector(1, 2, 3);
        double result1 = v1.length();
        assertEquals(Math.sqrt(14.0), result1,
                "all vectors length squares failed");

        // =============== Boundary Values Tests ==================
        // TC11: vector with length zero
        assertThrows(IllegalArgumentException.class, () -> {
            Vector v2 = new Vector(0, 0, 0);
            v2.length();
        },"failure text");
    }

    /**
     * test method for{@link Vector#scale(double)}
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: vectors that create an obtuse angle
        Vector v1 = new Vector(1, 2, 3);
        double num2 = 3;
        Vector result1 = v1.scale(num2);
        assertEquals(new Vector(3, 6, 9), result1,
                "vectors that create an obtuse angle failed");

        // TC02: vectors that create an acute angle
        Vector v3 = new Vector(2, 1, 0);
        double num4 = -5;
        Vector result2 = v3.scale(num4);
        assertEquals(new Vector(-10, -5, 0), result2,
                "vectors that create an acute angle failed");

        // =============== Boundary Values Tests ==================
        // TC11: scaling vector by zero
        Vector v5 = new Vector(2, 1, 0);
        double num6 = 0;
        assertThrows(IllegalArgumentException.class, () -> {
            v5.scale(num6);
        },"failure text");
    }

    /**
     * test method for{@link Vector#dotProduct(Vector)}
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: vectors that create an obtuse angle
        Vector v1 = new Vector(1, 1, 0);
        Vector v2 = new Vector(-1, 1, 0);
        double result1 = v1.dotProduct(v2);
        assertEquals(0.0, result1, "Dot product of vectors with an obtuse angle should be negative");

        // TC02: vectors that create an acute angle
        Vector v3 = new Vector(1, 2, 3);
        Vector v4 = new Vector(2, 3, 4);
        double result2 = v3.dotProduct(v4);
        assertEquals(20.0, result2, "Dot product of vectors with an acute angle should be positive");

        // =============== Boundary Values Tests ==================
        // TC11: vectors that create a 90 angle
        Vector v5 = new Vector(1, 0, 0);
        Vector v6 = new Vector(0, 1, 0);
        double result3 = v5.dotProduct(v6);
        assertEquals(0.0, result3, "Dot product of perpendicular vectors should be 0");

        // TC12: one of the vectors is a unit vector
        Vector v7 = new Vector(1, 2, 3);  // וקטור כללי
        Vector v8 = new Vector(1, 0, 0);  // וקטור יחידה בכיוון ה-X
        double result4 = v7.dotProduct(v8);
        assertEquals(v7.xyz.d1(), result4,
                "Dot product with unit vector should return the first vector's corresponding component");
    }

    /**
     * test method for{@link Vector#crossProduct(Vector)}
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: vectors that create an obtuse angle
        Vector v1 = new Vector(1, 1, 0);
        Vector v2 = new Vector(-1, 1, 0);
        Vector result1 = v1.crossProduct(v2);
        Vector expected1 = new Vector(0, 0, 2);
        assertEquals(expected1, result1, "Cross product of vectors with obtuse angle failed");

        // TC02: vectors that create an acute angle
        Vector v3 = new Vector(1, 0, 0);
        Vector v4 = new Vector(1, 1, 0);
        Vector result2 = v3.crossProduct(v4);
        Vector expected2 = new Vector(0, 0, 1);
        assertEquals(expected2, result2.normalize().scale(result2.length()),
                "Cross product of vectors with acute angle failed");

        // =============== Boundary Values Tests ==================
        // TC11: vectors with same direction and same length
        Vector v5 = new Vector(1, 2, 3);
        Vector v6 = new Vector(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> v5.crossProduct(v6),"failure text");

        //TC12: vectors with same direction
        Vector v7 = new Vector(1, 2, 3);
        Vector v8 = new Vector(2, 4, 6);  // פי 2
        assertThrows(IllegalArgumentException.class, () -> v7.crossProduct(v8),"failure text");

        // TC13: vectors with same length
        Vector v9 = new Vector(1, 0, 0);
        Vector v10 = new Vector(0, 1, 0);
        Vector result5 = v9.crossProduct(v10);
        Vector expected5 = new Vector(0, 0, 1);  // מאונך לשניהם
        assertEquals(expected5, result5, "Cross product of same-length perpendicular vectors failed");

        // TC14: vectors with opposite direction
        Vector v11 = new Vector(1, 2, 3);
        Vector v12 = new Vector(-1, -2, -3);  // בדיוק בכיוון ההפוך
        assertThrows(IllegalArgumentException.class,
                () -> v11.crossProduct(v12),"failure text");
    }

    /**
     * test method for{@link Vector#normalize()}
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: all vectors
        Vector v = new Vector(3, 4, 0); // אורך 5
        Vector normalized = v.normalize();
        assertEquals(1.0, normalized.length(), 1e-10, "Normalized vector should have length 1");

        // =============== Boundary Values Tests ==================
        //none

    }
}