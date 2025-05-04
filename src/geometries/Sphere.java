package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Sphere} class represents a sphere in 3D space.
 * A sphere is defined by a center point and a radius.
 *
 * @author Your Name
 */
public class Sphere extends RadianGeometry {

    /** The center of the sphere */
    private final Point center;

    /**
     * Constructs a {@code Sphere} with a given center and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     * @throws IllegalArgumentException if the radius is negative
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }


    @Override
    public Vector getNormal(Point p) {

        // Compute the normal by subtracting the center from p and normalizing
        return p.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        double d;
        double tm;
        try {
            Vector u = center.subtract(ray.getHead());
            tm = ray.getDirection().dotProduct(u);
            d = Math.sqrt(u.lengthSquared()-tm*tm);
        }catch (IllegalArgumentException e){
            tm = 0;
            d = 0;
        }
        if( d > radius ||  Util.isZero(radius - d) )
            return null;
        double th = Math.sqrt(radius*radius-d*d);
        double t1 = tm - th;
        double t2 = tm + th;
        if((t1 < 0 || Util.isZero(t1)) && (t2 < 0 || Util.isZero(t2)))
            return null;
        List<Point> result = new LinkedList<>();
        if(t1 > 0 && !Util.isZero(t1))
            result.add(ray.getHead().add(ray.getDirection().scale(t1)));
        if(t2 > 0 && !Util.isZero(t2))
            result.add(ray.getHead().add(ray.getDirection().scale(t2)));
        return result;
    }
}
