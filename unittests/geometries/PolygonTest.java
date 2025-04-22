package geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import primitives.*;

/**
 * Testing Polygons
 * @author Dan
 */
class PolygonTest {
   /**
    * Delta value for accuracy when comparing the numbers of type 'double' in
    * assertEquals
    */
   private static final double DELTA = 0.000001;

   /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
   @Test
   void testConstructor() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Correct concave quadrangular with vertices in correct order
      assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                                           new Point(1, 0, 0),
                                           new Point(0, 1, 0),
                                           new Point(-1, 1, 1)),
                         "Failed constructing a correct polygon");

      // TC02: Wrong vertices order
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1),
                           new Point(0, 1, 0),
                           new Point(1, 0, 0),
                           new Point(-1, 1, 1)), //
                   "Constructed a polygon with wrong order of vertices");

      // TC03: Not in the same plane
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1),
                           new Point(1, 0, 0),
                           new Point(0, 1, 0),
                           new Point(0, 2, 2)), //
                   "Constructed a polygon with vertices that are not in the same plane");

      // TC04: Concave quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1),
                           new Point(1, 0, 0),
                           new Point(0, 1, 0),
                           new Point(0.5, 0.25, 0.5)), //
                   "Constructed a concave polygon");

      // =============== Boundary Values Tests ==================

      // TC10: Vertex on a side of a quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1),
                           new Point(1, 0, 0),
                           new Point(0, 1, 0),
                           new Point(0, 0.5, 0.5)),
                   "Constructed a polygon with vertix on a side");

      // TC11: Last point = first point
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1),
                           new Point(1, 0, 0),
                           new Point(0, 1, 0),
                           new Point(0, 0, 1)),
                   "Constructed a polygon with vertice on a side");

      // TC12: Co-located points
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1),
                           new Point(1, 0, 0),
                           new Point(0, 1, 0),
                           new Point(0, 1, 0)),
                   "Constructed a polygon with vertice on a side");

   }

   /** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
   @Test
   void testGetNormal() {
      // ============ Equivalence Partitions Tests ==============
      // TC01: There is a simple single test here - using a quad
      Point[] pts =
         {
                 new Point(0, 0, 1),
                 new Point(1, 0, 0),
                 new Point(0, 1, 0),
                 new Point(-1, 1, 1) };
      Polygon pol = new Polygon(pts);
      // ensure there are no exceptions
      assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
      // generate the test result
      Vector result = pol.getNormal(new Point(0, 0, 1));
      // ensure |result| = 1
      assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
      // ensure the result is orthogonal to all the edges
      for (int i = 0; i < 3; ++i)
         assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                      "Polygon's normal is not orthogonal to one of the edges");
   }

   /**
    * Test method for {@link Polygon#findIntersections(Ray)}
    */
   @Test
   void testFindIntersections() {
      // ======= Setup - polygon for testing =======
      Polygon polygon = new Polygon(
              new Point(0, 0, 0),
              new Point(2, 0, 0),
              new Point(2, 2, 0),
              new Point(0, 2, 0)
      );

      // ============ Equivalence Partitions Tests ==============

      // TC01: Ray does not intersect with polygon's plane
      Ray ray1 = new Ray(new Point(1, 1, 1), new Vector(1, 0, 1));
      assertNull(polygon.findIntersections(ray1),
              "Ray does not intersect polygon's plane - should return null");

      // TC02: Ray intersects inside the polygon
      Ray ray2 = new Ray(new Point(1, 1, 1), new Vector(0, 0, -1));
      List<Point> result2 = polygon.findIntersections(ray2);
      assertNotNull(result2, "Ray should intersect inside the polygon");
      assertEquals(1, result2.size(), "Should be exactly one intersection point");
      assertEquals(new Point(1, 1, 0), result2.get(0), "Wrong intersection point inside the polygon");

      // TC03: Ray intersects the plane but outside against a side
      Ray ray3 = new Ray(new Point(3, 1, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray3),
              "Ray hits plane outside polygon against side - should return null");

      // TC04: Ray intersects the plane but outside against a vertex
      Ray ray4 = new Ray(new Point(3, 3, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray4),
              "Ray hits plane outside polygon against vertex - should return null");

      // =============== Boundary Values Tests ==================

      // TC11: Ray intersects exactly on the side of the polygon
      Ray ray11 = new Ray(new Point(1, 0, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray11), "Ray hits exactly on polygon's side - should return null");

      // TC12: Ray intersects exactly on the vertex of the polygon
      Ray ray12 = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray12), "Ray hits exactly on polygon's vertex - should return null");

      // TC13: Ray intersects along the extension of a side
      Ray ray13 = new Ray(new Point(3, 0, 1), new Vector(0, 0, -1));
      assertNull(polygon.findIntersections(ray13), "Ray hits extension of side - should return null");
   }
}
