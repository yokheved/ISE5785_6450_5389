package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source, such as sunlight,
 * that has a fixed direction and uniform intensity across the scene.
 * The light does not originate from a point but from infinity in a specific direction.
 */
public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    /**
     * Constructs a directional light with the specified intensity and direction.
     *
     * @param intensity the color and strength of the light
     * @param direction the direction vector of the light rays (normalized internally)
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return getIntensity();
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point){
        return Double.POSITIVE_INFINITY;
    }
}
