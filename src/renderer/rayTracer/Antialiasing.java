package renderer.rayTracer;

import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;


public class Antialiasing extends AdvancedRayTracer{

    public Antialiasing(Scene scene) {
        super(scene);
    }

    @Override
    public Color calculateColor(Ray ray) {
        return traceRay(ray);
    }
}
