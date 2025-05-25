package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a light source in a 3D scene.
 * <p>
 * Light sources define how light is emitted into the scene, allowing for the calculation
 * of lighting effects such as shading and shadows at specific points.
 */
public interface LightSource {

    /**
     * Returns the intensity (color and strength) of the light at a given point in space.
     *
     * @param p the point where the intensity is being evaluated
     * @return the light's color intensity at the given point
     */
    Color getIntensity(Point p);

    /**
     * Returns a normalized vector pointing from the light source to the given point.
     * <p>
     * This vector is used for lighting calculations such as diffuse and specular reflections.
     *
     * @param p the point in the scene
     * @return the normalized direction vector from the light source to the point
     */
    Vector getL(Point p);
    /**
     *calculates the distance between a point
     * in the scene to the position of the light source
     *
     * @param point the point in the scene
     * @return the distance between the point and the light source
     */
    double getDistance(Point point);
}
