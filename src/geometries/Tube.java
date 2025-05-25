package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Tube} class represents an infinite cylindrical tube in 3D space.
 * It is defined by a central axis (a ray) and a fixed radius.
 * The tube extends infinitely along the axis in both directions.
 *
 * @author Your Name
 */
public class Tube extends RadianGeometry {

    /** The central axis ray of the tube */
    protected final Ray axis;

    /**
     * Constructs a {@code Tube} with the specified axis ray and radius.
     *
     * @param axis   the central axis ray of the tube
     * @param radius the radius of the tube; must be positive
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube surface at the given point.
     * The normal is calculated as the normalized vector from the closest point
     * on the tube's axis to the given point.
     *
     * @param p the point on the tube surface to calculate the normal at
     * @return the normalized normal vector at point {@code p}
     */
    @Override
    public Vector getNormal(Point p) {
        Point p0 = axis.getHead();
        Vector dir = axis.getDirection();

        Vector p0ToP = p.subtract(p0);
        double t = dir.dotProduct(p0ToP);
        if(Util.isZero(t)){
            return new Vector(p).normalize();
        }
        Vector dirToP = dir.scale(t);
        Point o = p0.add(dirToP);
        return p.subtract(o).normalize();
    }

    /**
     * Calculates intersection points between the given ray and the tube.
     * Returns a list of intersections that are in front of the ray origin,
     * or {@code null} if there are no intersections.
     *
     * @param ray the ray to intersect with the tube
     * @return list of intersection points with the tube, or {@code null} if none exist
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();
        Point pa = axis.getHead();
        Vector va = axis.getDirection();

        // Calculate coefficients for the quadratic equation
        if(va.equals(v) || va.equals(v.scale(-1))) return null;
        if(p0.equals(pa)){
            double t = radius / Math.sqrt(1 - Math.pow(v.dotProduct(va), 2));
            List<Intersection> result = new LinkedList<>();
            result.add(new Intersection( this, p0.add(v.scale(t))) );
            return result;
        }
        Vector deltaP = p0.subtract(pa);

        boolean isOnAxis = false;

        double t = deltaP.dotProduct(va);
        Point closestAxisPoint = Util.isZero(t) ? pa.add(va.scale(deltaP.length())) : pa.add(va.scale(t));

        if (Util.isZero(p0.distance(closestAxisPoint))) {
            isOnAxis = true;
        }

        double a = v.lengthSquared() - Math.pow(v.dotProduct(va), 2);

        if (Util.isZero(a)) {
            return null;
        }

        double b = 2 * (v.dotProduct(deltaP) - (v.dotProduct(va) * deltaP.dotProduct(va)));
        double c = deltaP.lengthSquared() - Math.pow(deltaP.dotProduct(va), 2) - radius * radius;

        if (isOnAxis) {
            double t_intersection = Math.sqrt(radius * radius / a);
            Point intersectionPoint = p0.add(v.scale(t_intersection));
            List<Intersection> result = new LinkedList<>();
            result.add(new Intersection( this, intersectionPoint));
            return result;
        }

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null;
        }

        discriminant = Math.sqrt(discriminant);

        if (Util.isZero(c)) {
            if (discriminant > 0) {
                double t2 = -b / a;
                if (t2 > 0) {
                    List<Intersection> result = new LinkedList<>();
                    result.add(new Intersection( this, p0.add(v.scale(t2))));
                    return result;
                }
            }
            return null;
        }
        double t1 = (-b + discriminant) / (2 * a);
        double t2 = (-b - discriminant) / (2 * a);

        if (t1 <= 0 && t2 <= 0) {
            return null;
        }

        List<Intersection> result = new LinkedList<>();

        if (t1 > 0 && Util.alignZero(t1-maxDistance) <= 0) {
            result.add(new Intersection( this, p0.add(v.scale(t1))));
        }

        if (t2 > 0 && Util.alignZero(t2-maxDistance) <= 0) {
            result.add(new Intersection( this, p0.add(v.scale(t2))));
        }

        return result.isEmpty() ? null : result;
    }
}
