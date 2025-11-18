package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    /**
     * Test for {@link Transform#movePoints(Vector, Point...)}.
     */
    @Test
    void testMovePoints() {
        List<Point> moved = Transform.movePoints(new Vector(1, 0, 0),
                new Point(0, 0, 0), new Point(1, 0, 0));
        assertEquals(List.of(new Point(1, 0, 0), new Point(2, 0, 0)), moved);
    }

    /**
     * Test for {@link Transform#rotatePointsClockwise(double, Ray, Point...)}.
     */
    @Test
    void testRotatePointsClockwise() {
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Point> rotated = Transform.rotatePointsClockwise(90, axis,
                new Point(1, 0, 0), new Point(0, 1, 0));
        assertEquals(List.of(new Point(0, -1, 0), new Point(1, 0, 0)), rotated);
    }

    /**
     * Test for vector translation.
     */
    @Test
    void testMoveVectors() {

        List<Point> moved = Transform.movePoints(new Vector(0, 0, 1),
                new Vector(1, 0, 0), new Vector(0, 1, 0));
        assertEquals(List.of(new Point(1, 0, 1), new Point(0, 1, 1)), moved);

    }

    /**
     * Test for vector rotation.
     */
    @Test
    void testRotateVectorsClockwise() {
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Vector> rotated = Transform.rotateVectorsClockwise(90, axis,
                new Vector(1, 0, 0), new Vector(0, 1, 0));
        assertEquals(List.of(new Vector(0, -1, 0), new Vector(1, 0, 0)), rotated);

        // Additional test for a vector that is not aligned with the axis 30 degrees rotation
        List<Vector> rotated2 = Transform.rotateVectorsClockwise(30, axis,
                new Vector(1, 1, 0), new Vector(0, 1, 1));
        assertEquals(List.of(
                new Vector(1.3660254037844386,0.36602540378443876,0.0),
                new Vector(0.49999999999999994,0.8660254037844387,1.0)
        ), rotated2);

        //evp 180 degrees rotation
        List<Vector> rotated3 = Transform.rotateVectorsClockwise(180, axis,
                new Vector(1, 0, 0), new Vector(0, 1, 0));
        assertEquals(List.of(new Vector(-1, 0, 0), new Vector(0, -1, 0)), rotated3);

        //evp 90 degrees rotation with a vector not aligned with the axis
        List<Vector> rotated4 = Transform.rotateVectorsClockwise(90, axis,
                new Vector(1, 1, 0), new Vector(0, 1, 1));
        assertEquals(List.of(new Vector(1, -1, 0), new Vector(1, 0, 1)), rotated4);
    }
}
