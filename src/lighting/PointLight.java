package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a point light source that emits light in all directions from a specific position in space.
 * <p>
 * The intensity of the light decreases with distance, according to attenuation factors.
 */
public class PointLight extends Light implements LightSource {
    private final Point position;
    private double kC = 1, kL = 0, kQ = 0;

    /**
     * Constructs a point light with the specified intensity and position.
     *
     * @param intensity the color and strength of the light
     * @param position  the position of the light in the scene
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor (kC).
     *
     * @param kC the constant attenuation coefficient
     * @return this light instance for method chaining
     */
    public PointLight setKC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor (kL).
     *
     * @param kL the linear attenuation coefficient
     * @return this light instance for method chaining
     */
    public PointLight setKL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor (kQ).
     *
     * @param kq the quadratic attenuation coefficient
     * @return this light instance for method chaining
     */
    public PointLight setKQ(double kq) {
        this.kQ = kq;
        return this;
    }


    @Override
    public Color getIntensity(Point p) {
        double distance = position.distance(p);
        double factors = kC + kL * distance + kQ * distance * distance;
        return getIntensity().scale(1 / factors);
    }


    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }
}
