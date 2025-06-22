package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    /**
     * Test for {@link Transform#movePoints(Vector, double, Point...)}.
     */
    @Test
    void testMovePoints() {
        List<Point> moved = Transform.movePoints(new Vector(1, 0, 0), 2,
                new Point(0, 0, 0), new Point(1, 0, 0));
        assertEquals(List.of(new Point(2, 0, 0), new Point(3, 0, 0)), moved);
    }

    /**
     * Test for {@link Transform#rotatePointsClockwise(double, Point...)}.
     */
    @Test
    void testRotatePointsClockwise() {
        List<Point> rotated = Transform.rotatePointsClockwise(90,
                new Point(1, 0, 0), new Point(0, 1, 0));
        assertEquals(List.of(new Point(0, -1, 0), new Point(1, 0, 0)), rotated);
    }

    /**
     * Test for vector translation.
     */
    @Test
    void testMoveVectors() {
        List<Point> moved = Transform.movePoints(new Vector(0, 0, 1), 3,
                new Vector(1, 0, 0), new Vector(0, 1, 0));
        assertEquals(List.of(new Point(1, 0, 3), new Point(0, 1, 3)), moved);
    }

    /**
     * Test for vector rotation.
     */
    @Test
    void testRotateVectorsClockwise() {
        List<Point> rotated = Transform.rotatePointsClockwise(90,
                new Vector(1, 0, 0), new Vector(0, 1, 0));
        assertEquals(List.of(new Point(0, -1, 0), new Point(1, 0, 0)), rotated);
    }
}
