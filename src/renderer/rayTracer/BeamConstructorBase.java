package renderer.rayTracer;

import primitives.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

public abstract class BeamConstructorBase {

    int MAX_RAYS_PER_BEAM = 1;

    Point pc; //point on the plane surface of the target area
    Vector vRight, vUp, normal;
    double height, width;

    // New field in your class:
    List<Color> targetAreaGrid;

    public BeamConstructorBase setTargetArea(Vector vRight, Vector vUp, double height, double width, Point pc) {
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

    public BeamConstructorBase(int maxRaysPerBeam) {
        if (!Util.isPowerOfTwoPlusOne(Math.sqrt(maxRaysPerBeam))) {
            throw new IllegalArgumentException("Max rays per beam must be a power of two, plus one");
        }
        MAX_RAYS_PER_BEAM = maxRaysPerBeam;
    }

    public abstract BeamConstructorBase copyTargetArea(Ray ray);


    /**
     * Samples rays from the inner point of the cell, which is the intersection of the ray with the plane.
     * The rays are jittered around the corners of the subcell.
     *
     * @param depth the depth of the subcell
     * @param head   the head of the ray from which the rays will be sampled
     * @return a list of rays sampled from the inner point of the cell in this order:
     *         bottom-left, bottom-right, top-left, top-right
     */
    public List<Ray> subCellSampleRaysFromInnerPoint(int depth, Point head){
        List<Point> points = new LinkedList<>();

        double cellHeight = height / depth;
        double cellWidth = width / depth;

        Point bottomLeft = pc
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

        return constructRaysFromPoints(points, head);
    }

    protected List<Ray> constructRaysFromPoints(List<Point> points, Point head) {
        List<Ray> rays = new LinkedList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            // Calculate the direction vector from the ray's head to the point on the blackboard
            Vector direction = p.subtract(head);
            // Create a new ray with the ray's head and the calculated direction
            rays.add(new Ray(head, direction));
        }
        return rays;
    }

    public abstract Ray getCenterRay(int index, int depth, Point head);

    /**
     * Inserts a color in the targetAreaGrid based on the ray's position.
     *
     * @param index   the index in targetAreaGrid to insert the color
     * @param color the color to insert
     */
    public void putColorForRay(int index, Color color) {
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
    public Color getColorForRay(int index) {
        if (index < targetAreaGrid.size()) {
            return targetAreaGrid.get(index);
        }
        return null;
    }
}
