package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase {


    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersectionPoints = scene.geometries.findIntersections(ray);
        if(intersectionPoints == null)
            return scene.background;
        Point closest = ray.findClosestPoint(intersectionPoints);
        return calcColor(closest);
    }

    private Color calcColor(Point point){
        return scene.ambientLight.getIntensity();
    }

}
