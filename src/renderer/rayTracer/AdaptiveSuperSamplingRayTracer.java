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
        double maxDepth = Math.sqrt(advanced.beamConstructor.MAX_RAYS_PER_BEAM) - 1;
        // Base case: if too few rays, return scaled color
        if (depth >= maxDepth) {
            Color subCellColor = advanced.calculateColor(ray);
            return subCellColor.scale(1.0 / depth * depth);
        }
        List<Ray> rays = advanced.beamConstructor.subCellSampleRaysFromInnerPoint(depth, ray.getHead());
        Color subCellColor = getColor(rays.get(0), topLeftIndex, advanced);
        boolean allEqual = true;
        List<Integer> indices = getCornerIndices(topLeftIndex, depth, (int) maxDepth);
        for (int i = 1; i < rays.size(); i++) {
            int index = indices.get(i);
            if (!subCellColor.equals(getColor(rays.get(i), index, advanced))) {
                allEqual = false;
            }
        }
        if (!allEqual) {
            return superSamplingRecusiveCall(rays, advanced, depth, topLeftIndex, maxDepth);
        }
        return advanced.calculateColor(advanced.beamConstructor.getCenterRay(topLeftIndex, depth, ray.getHead()))
                .scale(1.0 / depth * depth);
    }


}
