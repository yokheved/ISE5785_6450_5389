package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Util;
import primitives.Vector;

public class SpotLight extends PointLight{
    private final Vector direction;
    private double beam = 1;

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
    public SpotLight setKC(double kC) {
        super.setKC(kC);
        return this;
    }

    @Override
    public SpotLight setKL(double kL) {
        super.setKL(kL);
        return this;
    }

    @Override
    public SpotLight setKQ(double kq) {
        super.setKQ(kq);
        return this;
    }

    @Override
    public Color getIntensity(Point p){
        double projection = Util.alignZero(direction.dotProduct(getL(p)));
        if (projection <= 0) return Color.BLACK; //check if the point is in the beam
        // calculate intensity to take the beam size into account
        double angle = Math.max(0d, Math.pow(projection, beam));
        return super.getIntensity(p).scale(angle);
    }

    /**
     * Sets the narrowness factor of the spotlight beam.
     * <p>
     * This factor controls how tightly focused the spotlight is around its central direction.
     * Higher values result in a narrower, more concentrated beam with a sharper falloff
     * from the center. A value of {@code 1} applies no narrowing.
     *
     * @param beam the narrowing exponent (must be greater than zero)
     * @return this spotlight instance for method chaining
     * @throws IllegalArgumentException if the beam value is not greater than zero
     */
    public SpotLight setNarrowBeam(double beam) {
        if(Util.alignZero(beam) <= 0)
            throw new IllegalArgumentException("beam must be greater than zero");
        this.beam = beam;
        return this;
    }

}
