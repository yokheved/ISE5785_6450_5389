package renderer.rayTracer;

import primitives.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

public abstract class TargetAreaBase {

    int MAX_RAYS_PER_BEAM = 1;

    Point pc; //point on the plane surface of the target area
    Vector vRight, vUp, normal;
    double height, width;

    // New field in your class:
    List<Color> targetAreaGrid;

    public TargetAreaBase setTargetArea(Vector vRight, Vector vUp, double height, double width, Point pc) {
        if (vRight == null || vUp == null || height <= 0 || width <= 0 || !isZero(vRight.dotProduct(vUp))) {
            throw new IllegalArgumentException("Invalid parameters for pixel setup");
        }
        this.pc = pc;
        this.vRight = vRight;
        this.vUp = vUp;
        this.height = height;
        this.width = width;
        this.normal = vRight.crossProduct(vUp).normalize();
        this.targetAreaGrid = new ArrayList<>(MAX_RAYS_PER_BEAM);
        return this;
    }

    public TargetAreaBase(int maxRaysPerBeam) {
        if (!Util.isPowerOfTwoPlusOne(Math.sqrt(maxRaysPerBeam))) {
            throw new IllegalArgumentException("Max rays per beam must be a power of two, plus one");
        }
        MAX_RAYS_PER_BEAM = maxRaysPerBeam;
    }

    public abstract TargetAreaBase copyTargetArea(Ray ray);


    /**
     * Samples rays from the inner point of the cell, which is the intersection of the ray with the plane.
     * The rays are jittered around the corners of the subcell.
     *
     * @param depth the depth of the subcell
     * @return a list of points sampled from the inner point of the cell in this order:
     *         bottom-left, bottom-right, top-left, top-right
     */
    public List<Point> subCellSampleRaysFromInnerPoint(int depth){
        List<Point> points = new LinkedList<>();

        double cellHeight = height / depth;
        double cellWidth = width / depth;

        Point bottomLeft = getCenterSubCell(0, depth)
                .add(vRight.scale(-cellWidth/2))
                .add(vUp.scale(-cellHeight/2));

        // Bottom-left
        points.add(bottomLeft);

        // Bottom-right
        points.add(bottomLeft.add(vRight.scale(cellWidth/2)));

        // Top-left
        points.add(bottomLeft.add(vUp.scale(-cellHeight/2)));

        // Top-right
        points.add(bottomLeft.add(vRight.scale(cellWidth/2))
                .add(vUp.scale(-cellHeight/2d)));

        return points;
    }

    public abstract Point getCenterPoint(int index, int depth);

    /**
     * Inserts a color in the targetAreaGrid based on the ray's position.
     *
     * @param index   the index in targetAreaGrid to insert the color
     * @param color the color to insert
     */
    public void putColorForPoint(int index, Color color) {
        // Ensure list is initialized up to index
        while (targetAreaGrid.size() <= index) {
            targetAreaGrid.add(null);
        }
        targetAreaGrid.set(index, color);
    }

    /**
     * Retrieves the color for the given ray from targetAreaGrid.
     *
     * @param index the index in targetAreaGrid to retrieve the color from
     * @return the color if exists, or null otherwise
     */
    public Color getColorForPoint(int index) {
        if (index < targetAreaGrid.size()) {
            return targetAreaGrid.get(index);
        }
        return null;
    }

    public Point getCenterSubCell(int index, int depth) {
        int i = index % (int) Math.pow(2, depth);
        int j = index / (int) Math.pow(2, depth);
        double cellWidth = width / Math.pow(2, depth);
        double cellHeight = height / Math.pow(2, depth);
        return pc
                .add(vRight.scale((i + 0.5) * cellWidth))
                .add(vUp.scale((j + 0.5) * cellHeight));
    }


    protected List<Integer> getCornerIndices(int indexTopLeft, int depth, int maxDepth) {
        int gridSize = (int) Math.pow(2, depth - 1);
        int step = (int) Math.ceil(maxDepth / (depth * 2));
        int topLeftIndex = indexTopLeft;
        int topRightIndex = topLeftIndex + step;
        int bottomLeftIndex = topLeftIndex + step * gridSize;
        int bottomRightIndex = bottomLeftIndex + step;

        return List.of(bottomLeftIndex, bottomRightIndex, topLeftIndex, topRightIndex);
    }
}
