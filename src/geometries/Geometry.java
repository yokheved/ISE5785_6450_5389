package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * The {@code Geometry} class serves as an abstract base class for all geometric objects.
 * It defines common properties such as emission color and material, and
 * an abstract method for retrieving the normal vector at a given point on the surface.
 *
 * <p>Each geometric object has:
 * <ul>
 *     <li>{@link Color} emission color – defines the base color emitted by the object</li>
 *     <li>{@link Material} material – defines the object's optical properties such as reflection, transparency, etc.</li>
 * </ul>
 *
 * <p>This class also provides fluent setters for chaining configuration methods.
 *
 * @author Your Name
 */
public abstract class Geometry extends Intersectable {
    protected Color emission = Color.BLACK;
    private Material material = new Material();

    /**
     * Returns the material of the geometry.
     *
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set
     * @return the geometry object itself for method chaining
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Calculates and returns the normal vector to the geometry at a given point.
     *
     * @param p the point on the geometry where the normal is calculated
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point p);

    /**
     * Returns the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return the geometry object itself for method chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
}
