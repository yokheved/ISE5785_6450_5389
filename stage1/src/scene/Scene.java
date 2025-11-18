package scene;

import geometries.Geometries;
import geometries.Intersectable;
import geometries.Tube;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.Camera;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a 3D scene that includes geometric objects, lighting, and background settings.
 * <p>
 * A scene aggregates all the components required for rendering, such as background color,
 * ambient light, geometries, and additional light sources.
 */
public class Scene {

    /** The name of the scene (used for identification or debugging) */
    public String name;

    /** The background color of the scene (default is black) */
    public Color background = new Color(java.awt.Color.BLACK);

    /** The ambient light of the scene, illuminating all objects uniformly */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /** The collection of geometries present in the scene */
    public Geometries geometries = new Geometries();

    /** The list of light sources in the scene */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructs a new {@code Scene} with the specified name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the desired background color
     * @return this scene instance (for method chaining)
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light source
     * @return this scene instance (for method chaining)
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Replaces the geometries in the scene with the given collection.
     *
     * @param geometries the geometries to set
     * @return this scene instance (for method chaining)
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Adds one or more light sources to the scene.
     *
     * @param lights the light sources to add
     * @return this scene instance (for method chaining)
     */
    public Scene setLights(LightSource... lights) {
        this.lights.addAll(Arrays.stream(lights).toList());
        return this;
    }
}
