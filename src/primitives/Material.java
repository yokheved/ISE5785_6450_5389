package primitives;

/**
 * The {@code Material} class represents the optical properties of a surface.
 * Currently, it supports setting the ambient reflection coefficient {@code Ka}.
 *
 * <p>This class is intended to be expanded with additional material properties
 * such as diffuse and specular coefficients, reflection, transparency, shininess, etc.</p>
 */
public class Material {
    /**
     * Ambient reflection coefficient.
     */
    public Double3 Ka = Double3.ONE;

    /**
     * Sets the ambient reflection coefficient.
     *
     * @param ka the ambient coefficient as a {@link Double3}
     * @return this material object for method chaining
     */
    public Material setKa(Double3 ka) {
        Ka = ka;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient.
     *
     * @param ka the ambient coefficient as a scalar value (uniform in all directions)
     * @return this material object for method chaining
     */
    public Material setKa(double ka) {
        Ka = new Double3(ka);
        return this;
    }
}
