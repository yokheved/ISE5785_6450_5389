package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.Intersection;

import java.util.List;

/**
 * A basic ray tracer implementation that computes the color of a ray
 * based solely on the ambient light and the emission color of the intersected geometry.
 * <p>
 * This tracer does not support shading, shadows, reflection, or refraction.
 * It is useful for initial testing and visualization of geometry and camera setup.
 * </p>
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a simple ray tracer for a given scene.
     *
     * @param scene the scene in which the rays will be traced
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a given ray through the scene to find its closest intersection.
     * If an intersection is found, returns the ambient light and emission color at that point.
     * Otherwise, returns the scene's background color.
     *
     * @param ray the ray to trace
     * @return the computed color for the ray
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersectionPoints = scene.geometries.calculateIntersections(ray);
        if (intersectionPoints == null) {
            return scene.background;
        }

        Intersection closest = ray.findClosestIntersection(intersectionPoints);
        return calcColor(closest);
    }

    /**
     * Calculates the color at the given intersection point.
     * The color is composed of the scene's ambient light and the geometry's emission.
     *
     * @param intersection the intersection point with geometry
     * @return the computed color at the intersection point
     */
    private Color calcColor(Intersection intersection) {
        return scene.ambientLight.getIntensity().add(intersection.geometry.getEmission());
    }
}
