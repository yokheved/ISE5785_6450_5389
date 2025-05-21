package lighting;

import primitives.Color;

/**
 * The {@code Light} class serves as an abstract base for all types of light sources in a scene.
 * It defines the common property of light intensity, represented by a {@link Color} object.
 *
 * <p>Specific types of lights such as ambient, directional, point, or spot lights should extend this class
 * and implement their unique behavior for how light is emitted or distributed in the scene.</p>
 */
abstract class Light {
    /**
     * The intensity (color and strength) of the light.
     */
    protected final Color intensity;

    /**
     * Constructs a light with the specified intensity.
     *
     * @param intensity the color and strength of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the light.
     *
     * @return the light intensity as a {@link Color}
     */
    public Color getIntensity() {
        return intensity;
    }
}
