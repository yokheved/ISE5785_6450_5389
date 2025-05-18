package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * A simple ray tracer implementation that returns ambient light at the closest intersection point.
 * This tracer does not compute shading, reflection, or refraction.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a simple ray tracer for the given scene.
     *
     * @param scene the scene to trace rays in
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersectionPoints = scene.geometries.findIntersections(ray);
        if(intersectionPoints == null)
            return scene.background;
        Point closest = ray.findClosestPoint(intersectionPoints);
        return calcColor(closest);
    }

    /**
     * Calculates the color at the given point.
     * Currently returns only the ambient light intensity of the scene.
     *
     * @param point the point to calculate the color at
     * @return the ambient light color
     */
    private Color calcColor(Point point){
        return scene.ambientLight.getIntensity();
    }

}
