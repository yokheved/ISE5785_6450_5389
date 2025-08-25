package renderer.rayTracer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;


public class JitteredTargetArea extends TargetAreaBase {

    public JitteredTargetArea(int maxRays) {
        super(maxRays);
    }

    @Override
    public TargetAreaBase copyTargetArea(Ray ray) {
        JitteredTargetArea copy = new JitteredTargetArea(MAX_RAYS_PER_BEAM);
        double denominator = normal.dotProduct(ray.getDirection());
        if (Util.isZero(denominator)) {
            throw new IllegalArgumentException("Ray is parallel to the target area");
        }
        double t = normal.dotProduct(pc.subtract(ray.getHead())) / denominator;
        Point pc = ray.getPoint(t);
        copy.setTargetArea(vRight, vUp, height, width, pc);
        return copy;
    }

    @Override
    public Point getCenterPoint(int indexTopLeft, int depth) {
        int gridSize = (int) Math.pow(2, depth - 1);
        double cellWidth = width / gridSize;
        double cellHeight = height / gridSize;
        Vector jitterX = null;
        Vector jitterY = null;
        Point pc = getCenterSubCell(indexTopLeft, depth);
        do{
            double randomX = Util.random(-cellWidth / 2, cellWidth / 2);
            try{jitterX = vRight.scale(randomX);}
            catch(Exception e){}
        }while(jitterX==null);
        do{
            double randomY = Util.random(-cellHeight / 2, cellHeight / 2);
            try{jitterY = vUp.scale(randomY);}
            catch(Exception e){}
        }while(jitterY==null);

        Point jitteredPoint = pc.add(jitterX).add(jitterY);
        return jitteredPoint;
    }

}
