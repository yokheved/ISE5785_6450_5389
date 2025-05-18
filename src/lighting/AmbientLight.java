package lighting;

import primitives.Color;

import static java.awt.Color.BLACK;

public class AmbientLight {

    final private Color intensity;
    public static AmbientLight NONE = new AmbientLight(new Color(BLACK));

    public AmbientLight(Color Ia) {
        intensity = Ia;
    }

    public Color getIntensity() {
        return intensity;
    }

}
