package renderer.rayTracer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.Intersection;

import java.util.List;

/**
 * A basic ray tracer implementation that computes the color of a ray
 * based on ambient light, emission, and local/global lighting effects.
 * <p>
 * Supports diffuse and specular reflections, shading, reflections, and transparency.
 * Global effects are computed recursively with reflection and refraction rays.
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

}
