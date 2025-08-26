package renderer.rayTracer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AdvancedRayTracer extends RayTracerBase {
    public List<AdvancedRayTracer> enhancements = new LinkedList<>();

    TargetAreaBase targetAreaBase;

    public AdvancedRayTracer setRayConstructor(TargetAreaBase rayConstructor) {
        this.targetAreaBase = rayConstructor;
        return this;
    }

    /**
     * Constructs a new AdvancedRayTracer with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public AdvancedRayTracer(Scene scene) {
        super(scene);
    }

    public AdvancedRayTracer setTargetArea(Vector vRight, Vector vUp, double height, double width, Point onSurface) {
        if (targetAreaBase == null || height <= 0 || width <= 0 || vRight.dotProduct(vUp) != 0) {
            throw new IllegalStateException("Beam constructor is not set");
        }
        targetAreaBase.setTargetArea(vRight, vUp, height, width, onSurface);
        return this;
    }

    public void addEnhancements(AdvancedRayTracer... enhancements) {
        this.enhancements.addAll(Arrays.asList(enhancements));
    }

    @Override
    public Color traceRay(Ray ray) {
        //if enhancements contains an object of class Antialiasing, use it to trace the ray
        for (AdvancedRayTracer enhancement : enhancements) {
            if (enhancement instanceof Antialiasing antialiasing) {
                antialiasing.targetAreaBase = antialiasing.copyTargetArea(ray);
                return superSampling(ray, antialiasing, 1, 0);
            }
        }
        return super.traceRay(ray);
    }

    protected Color superSampling(Ray ray, AdvancedRayTracer advanced, int depth, int topLeftIndex) {
        // Calculate the color of the first ray
        double maxDepth = Math.sqrt(advanced.getMaxRaysPerBeam())-1;
        // Base case: if too few rays, return scaled color
        if (depth >= maxDepth) {
            Color subCellColor = advanced.calculateColor(ray);
            return subCellColor.scale(1.0 / depth * depth);
        }
        List<Ray> rays = advanced.subCellSampleRaysFromInnerPoint(depth,ray.getHead());
        return superSamplingRecusiveCall(rays, advanced, depth, topLeftIndex, maxDepth);
    }

    protected Color superSamplingRecusiveCall(
            List<Ray> rays, AdvancedRayTracer advanced, int depth, int topLeftIndex, double maxDepth
    ) {
        List<Integer> cornerIndices = getCornerIndices(topLeftIndex, depth, (int) maxDepth, advanced);
        int topRightIndex = cornerIndices.get(3);
        int bottomLeftIndex = cornerIndices.get(0);
        int bottomRightIndex = cornerIndices.get(1);
        Point head = rays.get(0).getHead();

        Ray topLeftRay = advanced.getCenterRay(topLeftIndex, depth, head);
        Ray topRightRay = advanced.getCenterRay(topRightIndex, depth, head);
        Ray bottomLeftRay = advanced.getCenterRay(bottomLeftIndex, depth, head);
        Ray bottomRightRay = advanced.getCenterRay(bottomRightIndex, depth, head);

        Color bottomLeftColor = superSampling(bottomLeftRay, advanced, depth + 1, bottomLeftIndex);
        Color bottomRightColor = superSampling(bottomRightRay, advanced, depth + 1, bottomRightIndex);
        Color topLeftColor = superSampling(topLeftRay, advanced, depth + 1, topLeftIndex);
        Color topRightColor = superSampling(topRightRay, advanced, depth + 1, topRightIndex);

        // Return summed color of the four quadrants
        return topLeftColor
                .add(topRightColor)
                .add(bottomLeftColor)
                .add(bottomRightColor)
                .scale(0.25);
    }

    protected Color calculateColor(Ray ray) {
        throw new UnsupportedOperationException("This method should be overridden in subclasses");
    }


    protected Color getColor(Ray ray, int index, AdvancedRayTracer advanced) {
        Color subCellColor = targetAreaBase.getColorForPoint(index);
        if (subCellColor == null) {
            subCellColor = advanced.calculateColor(ray);
            targetAreaBase.putColorForPoint(index, subCellColor);
        }
        return subCellColor;
    }

    protected List<Integer> getCornerIndices(int indexTopLeft, int depth, int maxDepth, AdvancedRayTracer advanced) {
        return advanced.targetAreaBase.getCornerIndices(indexTopLeft, depth, maxDepth);
    }

    protected Ray getCenterRay(int indexTopLeft, int depth, Point head) {
        return new Ray(head, targetAreaBase.getCenterPoint(indexTopLeft, depth));
    }

    protected Point getCenterSubCell(int index, int depth) {
        return targetAreaBase.getCenterSubCell(index, depth);
    }

    protected List<Ray> subCellSampleRaysFromInnerPoint(int depth, Point head) {
        List<Point> points = targetAreaBase.subCellSampleRaysFromInnerPoint(depth);
        List<Ray> rays = new LinkedList<>();
        for(Point point : points) {
            rays.add(new Ray(head, point));
        }
        return rays;
    }

    protected int getMaxRaysPerBeam() {
        return targetAreaBase.MAX_RAYS_PER_BEAM;
    }

    protected TargetAreaBase copyTargetArea(Ray ray) {
        return targetAreaBase.copyTargetArea(ray);
    }

}
