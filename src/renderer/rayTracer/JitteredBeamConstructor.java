package renderer.rayTracer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

public class JitteredBeamConstructor extends BeamConstructorBase {

    public JitteredBeamConstructor(int maxRays) {
        super(maxRays);
    }

    @Override
    public BeamConstructorBase copyTargetArea(Ray ray) {
        JitteredBeamConstructor copy = new JitteredBeamConstructor(MAX_RAYS_PER_BEAM);
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
    public Ray getCenterRay(int indexTopLeft, int depth, Point head) {
        int gridSize = (int) Math.pow(2, depth - 1);
        double cellWidth = width / gridSize;
        double cellHeight = height / gridSize;
        Vector jitterX = null;
        Vector jitterY = null;
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
        return constructRaysFromPoints(List.of(jitteredPoint), head).get(0);
    }

}
