package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight{
    private final Vector direction;

    /**
     * Constructs a light with the specified intensity.
     *
     * @param intensity the color and strength of the light
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public PointLight setKC(double kC) {
        super.setKC(kC);
        return this;
    }

    @Override
    public PointLight setKL(double kL) {
        super.setKL(kL);
        return this;
    }

    @Override
    public PointLight setKq(double kq) {
        super.setKq(kq);
        return this;
    }

    @Override
    public Color getIntensity(Point p){
        double angle = Math.max(0d, direction.dotProduct(getL(p)));
        return super.getIntensity(p).scale(angle);
    }

}
