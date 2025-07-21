package renderer.rayTracer;

import geometries.Intersectable;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

/**
 * Abstract base class for ray tracing algorithms.
 * Responsible for tracing a single ray through a scene and computing its color.
 */
public abstract class RayTracerBase {
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;
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
     * Traces a given ray through the scene to find its closest intersection.
     * If an intersection is found, returns the computed color at that point.
     * Otherwise, returns the scene's background color.
     *
     * @param ray the ray to trace
     * @return the computed color for the ray
     */
    public Color traceRay(Ray ray) {
        Intersectable.Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return scene.background;
        }
        return calcColor(intersection, ray);
    }

    /**
     * Calculates the total color at a given intersection, including ambient light
     * and all local and global lighting effects.
     *
     * @param intersection the intersection point
     * @param ray          the incoming ray
     * @return the final color at the intersection
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray) {
        return scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().kA)
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K, ray));
    }

    /**
     * Recursively computes the color at an intersection with control over depth and intensity.
     *
     * @param intersection the intersection point
     * @param level        current recursion depth
     * @param k            current accumulated reflection/refraction coefficient
     * @param ray          the ray that caused the intersection
     * @return the computed color
     */
    private Color calcColor(Intersectable.Intersection intersection, int level, Double3 k, Ray ray) {
        boolean lighted = preprocessIntersection(intersection, ray.getDirection());
        if (!lighted) {
            return Color.BLACK;
        }
        Color color = calcColorLocalEffects(intersection, k);
        return level == 1 ? color
                : color.add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Prepares intersection data for lighting calculations.
     * Sets normalized ray direction, geometry normal, and dot product.
     *
     * @param intersection    the intersection to prepare
     * @param intersectingRay the incoming ray direction
     * @return true if the geometry faces the ray, false otherwise
     */
    public Boolean preprocessIntersection(Intersectable.Intersection intersection, Vector intersectingRay) {
        intersection.rayDirection = intersectingRay.normalize();
        intersection.geometryNormal = intersection.geometry.getNormal(intersection.point);
        intersection.directionDotNormal = Util.alignZero(
                intersection.rayDirection.dotProduct(intersection.geometryNormal)
        );
        return intersection.directionDotNormal != 0;
    }

    /**
     * Initializes light-related direction values at the intersection,
     * including the dot product used for shading.
     *
     * @param intersection the intersection being evaluated
     * @param light        the light source
     * @return true if the light contributes to shading (same side), false otherwise
     */
    public Boolean setLightSource(Intersectable.Intersection intersection, LightSource light) {
        intersection.lightSource = light;
        intersection.lightDirection = light.getL(intersection.point);
        intersection.lightDirectionDotNormal = Util.alignZero(
                intersection.lightDirection.dotProduct(intersection.geometryNormal)
        );
        return intersection.lightDirectionDotNormal * intersection.directionDotNormal > 0;
    }

    /**
     * Calculates the local lighting effects (diffuse and specular) at a point.
     *
     * @param intersection the intersection point
     * @return the combined color from all visible light sources
     */
    Color calcColorLocalEffects(Intersectable.Intersection intersection, Double3 k) {
        Color result = intersection.geometry.getEmission();
        for (LightSource light : scene.lights) {
            boolean isLit = setLightSource(intersection, light);
            if (isLit /*&& unshaded(intersection)*/) {
                Double3 ktr = transparency(intersection);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color il = intersection.lightSource.getIntensity(intersection.point).scale(ktr);
                    result = result.add(il.scale(
                            calcDiffusive(intersection).add(calcSpecular(intersection))
                    ));
                }
            }
        }
        return result;
    }

    /**
     * Calculates the global lighting effects (reflection and transparency)
     * at an intersection recursively.
     *
     * @param intersection the intersection point
     * @param level        recursion depth
     * @param k            current intensity coefficient
     * @return the combined global lighting color
     */
    private Color calcGlobalEffects(Intersectable.Intersection intersection, int level, Double3 k) {
        Ray reflectRay = constructReflectedRay(intersection);
        Color reflected = calcGlobalEffect(reflectRay, level, k, intersection.material.kR);
        Ray refractRay = constructRefractedRay(intersection);
        Color refracted = calcGlobalEffect(refractRay, level, k, intersection.material.kT);
        return reflected.add(refracted);
    }

    /**
     * Calculates the color contribution from a single global effect (reflection or refraction).
     *
     * @param ray   the secondary ray (reflected or refracted)
     * @param level current recursion level
     * @param k     accumulated coefficient
     * @param kx    material coefficient (Kr or Kt)
     * @return the color contribution
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersectable.Intersection closest = findClosestIntersection(ray);
        return closest == null ? scene.background : calcColor(closest, level - 1, kkx, ray).scale(kx);
    }

    /**
     * Calculates the specular lighting component using the Phong reflection model.
     *
     * @param intersection the intersection data
     * @return the specular reflection as a {@link Double3}
     */
    Double3 calcSpecular(Intersectable.Intersection intersection) {
        Vector L = intersection.lightDirection;
        Vector N = intersection.geometryNormal;
        Vector V = intersection.rayDirection;
        Vector R = L.subtract(N.scale(2 * L.dotProduct(N))); // Reflected light vector

        double vrDot = Math.max(0, V.scale(-1).dotProduct(R));
        double specFactor = Math.pow(vrDot, intersection.material.nSh);

        return intersection.material.kS.scale(specFactor);
    }

    /**
     * Calculates the diffuse lighting component using Lambert's cosine law.
     *
     * @param intersection the intersection data
     * @return the diffuse reflection as a {@link Double3}
     */
    Double3 calcDiffusive(Intersectable.Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lightDirectionDotNormal));
    }

    /**
     * Determines whether the intersection point is in shadow with respect to the light source.
     *
     * @param intersection the intersection data
     * @return true if the point is unshaded (not blocked), false if a blocker exists
     */
    private boolean unshaded(Intersectable.Intersection intersection) {
        Vector L = intersection.lightDirection.scale(-1);
        double epsSign = intersection.lightDirectionDotNormal < 0 ? 1 : -1;
        Vector eps = intersection.geometryNormal.scale(DELTA * epsSign);
        Ray ray = new Ray(intersection.point, L, intersection.geometryNormal);
        List<Intersectable.Intersection> intersections = scene.geometries
                .calculateIntersections(ray, intersection.lightSource.getDistance(intersection.point));
        if (intersections == null) return true;
        intersections.removeIf(i -> !i.material.kT.lowerThan(MIN_CALC_COLOR_K));
        return intersections.isEmpty();
    }

    /**
     * Calculates the transparency factor (ktr) at a given intersection point.
     * <p>
     * This method casts a shadow ray from the intersection point toward the light source
     * and checks for other geometries that may partially block the light. The transparency
     * factor is computed by multiplying the transparency coefficients (kT) of all obstructing
     * geometries along the ray path that are closer to the light source than the original intersection.
     * If the accumulated transparency falls below a minimum threshold, full blockage (black) is returned.
     *
     * @param intersection the intersection point for which to calculate transparency
     * @return the accumulated transparency factor as a {@link Double3}, or {@link Color#BLACK}.getRgb() if blocked
     */
    private Double3 transparency(Intersectable.Intersection intersection) {
        Double3 ktr = Double3.ONE;
        Vector L = intersection.lightDirection.scale(-1);
        double lightDistance = intersection.lightSource.getDistance(intersection.point);
        Ray ray = new Ray(intersection.point, L, intersection.geometryNormal);
        List<Intersectable.Intersection> intersections = scene.geometries
                .calculateIntersections(ray, lightDistance);
        if (intersections == null)
            return Double3.ONE;
        for (Intersectable.Intersection i : intersections) {
            double pointDistanceI = intersection.point.distance(i.point);
            if (pointDistanceI < lightDistance) {
                ktr = ktr.product(i.material.kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                    return Color.BLACK.getRgb();
                }
            }
        }
        return ktr;
    }

    /**
     * Constructs a reflection ray from the given intersection.
     *
     * @param intersection the intersection point
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersectable.Intersection intersection) {
        Vector V = intersection.rayDirection;
        Vector N = intersection.geometryNormal;
        Vector R = V.subtract(N.scale(2 * V.dotProduct(N)));
        return new Ray(intersection.point, R, N);
    }

    /**
     * Constructs a refraction ray from the given intersection (same direction as incoming ray).
     *
     * @param intersection the intersection point
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersectable.Intersection intersection) {
        Vector L = intersection.rayDirection;
        return new Ray(intersection.point, L, intersection.geometryNormal);
    }

    /**
     * Finds the closest intersection between a ray and the scene geometries.
     *
     * @param ray the ray to test
     * @return the closest intersection, or {@code null} if none found
     */
    protected Intersectable.Intersection findClosestIntersection(Ray ray) {
        List<Intersectable.Intersection> intersections = scene.geometries.calculateIntersections(ray);
        return ray.findClosestIntersection(intersections);
    }
}
