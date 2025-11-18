package renderer.rayTracer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class AdaptiveSuperSamplingRayTracer extends AdvancedRayTracer {
    public AdaptiveSuperSamplingRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    protected Color superSampling(Ray ray, AdvancedRayTracer advanced, int depth, int topLeftIndex) {
        // Calculate the color of the first ray
        double maxDepth = Math.sqrt(advanced.getMaxRaysPerBeam()) - 1;
        // Base case: if too few rays, return scaled color
        if (depth >= maxDepth) {
            Color subCellColor = advanced.calculateColor(ray);
            return subCellColor.scale(1.0 / depth * depth);
        }
        List<Ray> rays = advanced.subCellSampleRaysFromInnerPoint(depth, ray.getHead());
        Color subCellColor = advanced.getColor(rays.get(0), topLeftIndex, advanced);
        boolean allEqual = true;
        List<Integer> indices = getCornerIndices(topLeftIndex, depth, (int) maxDepth, advanced);
        for (int i = 1; i < rays.size(); i++) {
            int index = indices.get(i);
            Color iColor = advanced.getColor(rays.get(i), index, advanced);
            if (!(subCellColor.getRgb().subtract(iColor.getRgb())).abs().lowerThan(MIN_CALC_COLOR_K)) {
                allEqual = false;
                break;
            }
        }
        if (!allEqual) {
            return superSamplingRecusiveCall(rays, advanced, depth, topLeftIndex, maxDepth);
        }
        return advanced.calculateColor(ray)
                .scale(1.0 / depth * depth);
    }
}
