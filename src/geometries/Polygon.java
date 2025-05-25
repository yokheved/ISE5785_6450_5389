package geometries;

import static java.lang.Double.*;
import java.util.List;
import static primitives.Util.*;
import primitives.*;

/**
 * The {@code Polygon} class represents a convex polygon in 3D space.
 * A polygon is defined by a list of vertices ordered along its edge path.
 * The polygon must be convex, and all vertices must lie in the same plane.
 * <p>
 * Internally, the polygon is associated with a {@link Plane} for geometric calculations.
 * </p>
 *
 * @author Dan
 */
public class Polygon extends Geometry {
   /** List of the polygon's vertices, ordered along its edges */
   protected final List<Point> vertices;

   /** The plane in which the polygon lies */
   protected final Plane plane;

   /** The number of vertices in the polygon */
   private final int size;

   /**
    * Constructs a convex polygon from a list of ordered vertices.
    * The polygon must be:
    * <ul>
    *   <li>Defined by at least 3 vertices</li>
    *   <li>Convex</li>
    *   <li>Non-collinear (no three consecutive vertices on the same line)</li>
    *   <li>Planar (all vertices in the same plane)</li>
    * </ul>
    *
    * @param vertices ordered list of vertices along the polygon's edges
    * @throws IllegalArgumentException if any of the polygon construction conditions are violated
    */
   public Polygon(Point... vertices) {
      if (vertices.length < 3)
         throw new IllegalArgumentException("A polygon can't have less than 3 vertices");

      this.vertices = List.of(vertices);
      size = vertices.length;

      // Define the plane from the first three vertices
      plane = new Plane(vertices[0], vertices[1], vertices[2]);

      if (size == 3) return; // No need for further checks for a triangle

      Vector n = plane.getNormal(vertices[0]);

      // Initial edges for convexity and orientation check
      Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
      Vector edge2 = vertices[0].subtract(vertices[size - 1]);

      boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;

      for (int i = 1; i < size; ++i) {
         // Check all vertices lie in the same plane
         if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
            throw new IllegalArgumentException("All vertices of a polygon must lie in the same plane");

         // Check convexity and ordering
         edge1 = edge2;
         edge2 = vertices[i].subtract(vertices[i - 1]);
         if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
            throw new IllegalArgumentException("Vertices must be ordered and form a convex polygon");
      }
   }

   @Override
   public Vector getNormal(Point point) {
      return plane.getNormal(point);
   }

   /**
    * Finds intersections of a ray with the polygon.
    * The method checks if the intersection point with the polygon's plane lies inside the polygon.
    *
    * @param ray the ray to intersect with the polygon
    * @return a list containing a single intersection point if it lies inside the polygon, or {@code null} if not
    */
   @Override
   public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
      List<Point> planeIntersections = plane.findIntersections(ray);
      if (planeIntersections == null) return null;

      Point p0 = ray.getHead();
      Vector dir = ray.getDirection();
      Point p = planeIntersections.get(0);

      Vector v1 = vertices.get(0).subtract(p0);
      Vector v2 = vertices.get(1).subtract(p0);
      Vector n = v1.crossProduct(v2).normalize();
      double sign = alignZero(dir.dotProduct(n));
      if (isZero(sign)) return null;
      boolean positive = sign > 0;

      for (int i = 1; i < size; ++i) {
         v1 = vertices.get(i).subtract(p0);
         v2 = vertices.get((i + 1) % size).subtract(p0);
         n = v1.crossProduct(v2).normalize();
         sign = alignZero(dir.dotProduct(n));
         if (isZero(sign)) return null;
         if ((sign > 0) != positive) return null;
      }

      return List.of(new Intersection(this, p)); // The point lies inside the polygon
   }
}
