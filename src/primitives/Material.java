package primitives;

/**
 * Represents the optical material properties of a surface for lighting calculations.
 * <p>
 * A material defines how an object reacts to different types of light components,
 * such as ambient, diffuse, and specular reflections, as well as shininess for highlights.
 */
public class Material {

    /** Ambient reflection coefficient (Ka) */
    public Double3 kA = Double3.ONE;

    /** Specular reflection coefficient (Ks) */
    public Double3 kS = Double3.ZERO;

    /** Diffuse reflection coefficient (Kd) */
    public Double3 kD = Double3.ZERO;

    /** Transparency coefficient (Kt) */
    public Double3 kT = Double3.ZERO;

    /** Reflection coefficient (Kr) */
    public Double3 kR = Double3.ZERO;

    /** Shininess coefficient for specular reflection */
    public double nSh = 0;

    /**
     * Sets the ambient reflection coefficient (Ka).
     *
     * @param kA the ambient reflection coefficient as a {@link Double3}
     * @return this material instance (for method chaining)
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient (Ka) uniformly.
     *
     * @param ka the ambient reflection coefficient as a scalar value
     * @return this material instance (for method chaining)
     */
    public Material setKA(double ka) {
        kA = new Double3(ka);
        return this;
    }

    /**
     * Sets the specular reflection coefficient (Ks).
     *
     * @param kS the specular coefficient as a {@link Double3}
     * @return this material instance (for method chaining)
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient (Ks) uniformly.
     *
     * @param kS the specular coefficient as a scalar value
     * @return this material instance (for method chaining)
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient (Kd).
     *
     * @param kd the diffuse coefficient as a {@link Double3}
     * @return this material instance (for method chaining)
     */
    public Material setKD(Double3 kd) {
        this.kD = kd;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient (Kd) uniformly.
     *
     * @param kd the diffuse coefficient as a scalar value
     * @return this material instance (for method chaining)
     */
    public Material setKD(double kd) {
        this.kD = new Double3(kd);
        return this;
    }

    /**
     * Sets the shininess coefficient for specular highlights.
     *
     * @param nSh the shininess factor
     * @return this material instance (for method chaining)
     */
    public Material setnSh(double nSh) {
        this.nSh = nSh;
        return this;
    }

    /**
     * Sets the reflection coefficient (Kr).
     *
     * @param kR the reflection coefficient as a {@link Double3}
     * @return this material instance (for method chaining)
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient (Kr) uniformly.
     *
     * @param kR the reflection coefficient as a scalar value
     * @return this material instance (for method chaining)
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets the transparency coefficient (Kt).
     *
     * @param kT the transparency coefficient as a {@link Double3}
     * @return this material instance (for method chaining)
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency coefficient (Kt) uniformly.
     *
     * @param kT the transparency coefficient as a scalar value
     * @return this material instance (for method chaining)
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }
}

