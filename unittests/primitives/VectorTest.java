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
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testTestAdd() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }

    /**
     * test method for{@link Vector#${METHOD_NAME}(${PARAMETERS})}
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============

        // =============== Boundary Values Tests ==================
        fail("Not yet implemented");
    }
}