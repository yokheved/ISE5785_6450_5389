package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracing algorithms.
 * Responsible for tracing a single ray through a scene and computing its color.
 */
public abstract class RayTracerBase {
    /**
     * The scene to be rendered.
     */
    protected final Scene scene;

    /**
     * Constructs a ray tracer for a given scene.
     *
     * @param scene the scene to trace rays in
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray and computes the color at the intersection point.
     *
     * @param ray the ray to trace
     * @return the computed color
     */
    public abstract Color traceRay(Ray ray);
}
