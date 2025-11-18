package lighting;

import primitives.Color;

import static java.awt.Color.BLACK;

/**
 * Represents ambient light in a scene, which affects all objects equally
 * regardless of their position or orientation.
 */
public class AmbientLight extends Light {

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
        super(Ia);
    }

}
