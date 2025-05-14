package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

public class Camera implements Cloneable {

    public static class Builder {
        final private Camera camera = new Camera();

        public Builder setLocation(Point p) {
            camera.p0 = p;
            return this;
        }

        public Builder setDirection(Vector Vto, Vector Vup) {
            if (!isZero(Vto.dotProduct(Vup)))
                throw new IllegalArgumentException("vector VTo needs to be orthogonal");
            camera.VTo = Vto.normalize();
            camera.VUp = Vup.normalize();
            return this;
        }

        public Builder setDirection(Point pCenter, Vector Vup) {
            camera.VTo = pCenter.subtract(camera.p0).normalize();
            camera.VRight = camera.VTo.crossProduct(Vup).normalize();
            camera.VUp = camera.VRight.crossProduct(camera.VTo).normalize();
            if (!isZero(camera.VTo.dotProduct(camera.VRight)))
                throw new IllegalArgumentException("vector VTo needs to be orthogonal");
            return this;
        }

        public Builder setDirection(Point pCenter) {

            return this.setDirection(pCenter, Vector.AXIS_Y);
        }

        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0)
                throw new IllegalArgumentException("width and height needs to be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("distance need to be positive");
            camera.distance = distance;
            return this;
        }

        public Builder setResolution(double nX, double nY) {
            return this;
        }

        public Camera build() throws CloneNotSupportedException {
            String exceptionMessageProblem = "renderer data messing";
            String exceptionMassageClass = "Camera";

            if (camera.VTo == null)
                throw new MissingResourceException(exceptionMessageProblem, exceptionMassageClass, " VTo of direction ");
            if (camera.VUp == null)
                throw new MissingResourceException(exceptionMessageProblem, exceptionMassageClass, " VUp of direction ");

            if (camera.p0 == null)
                throw new MissingResourceException(exceptionMessageProblem, exceptionMassageClass, " p0 for location ");

            if (camera.width == (0d))
                throw new MissingResourceException(exceptionMessageProblem, exceptionMassageClass, " width for VPSize ");
            if (camera.height == (0d))
                throw new MissingResourceException(exceptionMessageProblem, exceptionMassageClass, " height for VPSize ");
            if (camera.distance == (0d))
                throw new MissingResourceException(exceptionMessageProblem, exceptionMassageClass, " distance for VP distanc");
            if (camera.VRight == null)
                camera.VRight = camera.VTo.crossProduct(camera.VUp).normalize();

            camera.pc = camera.p0.add(camera.VTo.scale(camera.distance));

            return (Camera) camera.clone();
        }
    }


    private Vector VTo;
    private Vector VUp;
    private Vector VRight;
    private Point p0;
    double width = 0;
    double height = 0;
    double distance = 0;
    private Point pc;


    private Camera() {
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        double ry = height / nY;
        double rx = width / nX;
        double yi = -(i - (nY - 1) / 2.0) * ry;
        double xj = (j - (nX - 1) / 2.0) * rx;

        // Calculate the point on the view plane
        Point pij = pc;

        // Add the horizontal and vertical offsets
        if (!isZero(xj)) pij = pij.add(VRight.scale(xj));
        if (!isZero(yi)) pij = pij.add(VUp.scale(yi));

        Vector vij = pij.subtract(p0).normalize();
        return new Ray(p0, vij);
    }


}
