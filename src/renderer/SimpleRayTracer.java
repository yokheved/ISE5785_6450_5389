package renderer;

import lighting.LightSource;
import primitives.*;
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
        Color closestColor = calcColor(closest, ray);
        return closestColor;
    }

    /**
     * Calculates the base color at the given intersection point.
     * <p>
     * The color is composed of ambient lighting and local lighting effects
     * (diffuse and specular), if the geometry is facing the ray.
     * If the geometry is not lit from the ray direction, black is returned.
     *
     * @param intersection the intersection point
     * @param ray the incoming ray
     * @return the calculated color
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        boolean lighted = preprocessIntersection(intersection, ray.getDirection());
        if (!lighted) {
            return Color.BLACK;
        }
        return scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().kA)
                .add(calcColorLocalEffects(intersection));
    }
    /**
     * Prepares intersection data required for lighting calculations.
     * Sets ray direction, geometry normal, and dot product between them.
     *
     * @param intersection    the intersection to prepare
     * @param intersectingRay the direction of the ray that caused the intersection
     * @return true if the dot product is not zero (surface is visible), false otherwise
     */
    public Boolean preprocessIntersection(Intersection intersection, Vector intersectingRay) {
        intersection.rayDirection = intersectingRay.normalize();
        intersection.geometryNormal = intersection.geometry.getNormal(intersection.point);
        intersection.directionDotNormal = Util.alignZero(
                intersection.rayDirection.dotProduct(intersection.geometryNormal)
        );
        return intersection.directionDotNormal != 0;
    }
    /**
     * Sets the light direction and computes its dot product with the normal.
     * Returns whether the light contributes to the surface (same hemisphere as viewer).
     *
     * @param intersection the intersection being evaluated
     * @param light        the light source being considered
     * @return true if the light contributes to the shading at this point, false otherwise
     */
    public Boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.lightSource = light;
        intersection.lightDirection = light.getL(intersection.point);
        intersection.lightDirectionDotNormal = Util.alignZero(
                intersection.lightDirection.dotProduct(intersection.geometryNormal)
        );
        return intersection.lightDirectionDotNormal * intersection.directionDotNormal > 0;
    }
    /**
     * Computes the local lighting effects (diffuse and specular) at an intersection point.
     *
     * @param intersection the intersection with geometry
     * @return the combined color contribution from all light sources
     */
    Color calcColorLocalEffects(Intersection intersection){
        Color result = intersection.geometry.getEmission();
        for(LightSource light : scene.lights){
            boolean isLit = setLightSource(intersection, light);
            if(! isLit ) continue;
            result = result.add(intersection.lightSource.getIntensity(intersection.point)
                    .scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
        }
        return result;
    }
    /**
     * Calculates the specular component of the lighting at the intersection.
     * Based on the Phong reflection model.
     *
     * @param intersection the intersection data
     * @return the specular reflection as a {@link Double3}
     */
    Double3 calcSpecular(Intersection intersection) {
        Vector L = intersection.lightDirection;
        Vector N = intersection.geometryNormal;
        Vector V = intersection.rayDirection;
        Vector R = L.subtract(N.scale(2 * L.dotProduct(N))); // Reflected light vector

        double vrDot = Math.max(0, V.dotProduct(R));
        double specFactor = Math.pow(vrDot, intersection.material.nSh);

        return intersection.material.kS
                .scale(specFactor);
    }

    /**
     * Calculates the diffuse component of the lighting at the intersection.
     * Based on Lambert's cosine law.
     *
     * @param intersection the intersection data
     * @return the diffuse reflection as a {@link Double3}
     */
    Double3 calcDiffusive(Intersection intersection){
        return intersection.material.kD.scale(Math.abs(intersection.lightDirectionDotNormal));
    }

}
