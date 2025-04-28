package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{

    private final List<Intersectable> geometries = new LinkedList<>();

    public Geometries() {
    }

    public Geometries(Intersectable... geometries){
        this.add(geometries);
    }

    public void add(Intersectable... geometries){
        this.geometries.addAll(Arrays.asList(geometries));
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;
        for (Intersectable geometry : geometries) {
            List<Point> gResult = geometry.findIntersections(ray);
            if (gResult != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(gResult);
            }
        }
        return result;
    }
}
