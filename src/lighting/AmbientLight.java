package lighting;

import primitives.Color;

import static java.awt.Color.BLACK;

/**
 * Represents ambient light in a scene, which affects all objects equally
 * regardless of their position or orientation.
 */
public class AmbientLight {

    final private Color intensity;

    /**
     * A constant representing no ambient light.
     */
    public static AmbientLight NONE = new AmbientLight(new Color(BLACK));

    /**
     * Constructs an AmbientLight with the specified intensity.
     *
     * @param Ia the color intensity of the ambient light
     */
    public AmbientLight(Color Ia) {
        intensity = Ia;
    }

    /**
     * Returns the intensity of the ambient light.
     *
     * @return the color intensity
     */
    public Color getIntensity() {
        return intensity;
    }

}
