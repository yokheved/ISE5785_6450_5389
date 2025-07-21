package renderer;

import primitives.*;
import renderer.rayTracer.*;
import scene.Scene;

import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * The {@code Camera} class represents a virtual camera in 3D space, used to render images
 * of a scene by tracing rays through a view plane.
 */
public class Camera implements Cloneable {

    /**
     * Builder class for constructing {@link Camera} objects using a fluent API.
     */
    public static class Builder {
        final private Camera camera;

        public Builder() {
            camera = new Camera();
        }

        public Builder(Camera old) {
            camera = old;
        }

        /**
         * Sets the location (eye point) of the camera.
         *
         * @param p the location point
         * @return this builder
         */
        public Builder setLocation(Point p) {
            camera.p0 = p;
            return this;
        }

        /**
         * Sets the viewing direction using two orthogonal vectors.
         *
         * @param Vto the direction vector from the camera toward the view plane
         * @param Vup the up vector
         * @return this builder
         * @throws IllegalArgumentException if the vectors are not orthogonal
         */
        public Builder setDirection(Vector Vto, Vector Vup) {
            if (!isZero(Vto.dotProduct(Vup)))
                throw new IllegalArgumentException("vector VTo needs to be orthogonal");
            camera.VTo = Vto.normalize();
            camera.VUp = Vup.normalize();
            return this;
        }

        /**
         * Sets the direction based on a center point and up vector.
         *
         * @param pCenter the center point of the view plane
         * @param Vup     the up vector
         * @return this builder
         * @throws IllegalArgumentException if VTo and VRight are not orthogonal
         */
        public Builder setDirection(Point pCenter, Vector Vup) {
            camera.VTo = pCenter.subtract(camera.p0).normalize();
            camera.VRight = camera.VTo.crossProduct(Vup).normalize();
            camera.VUp = camera.VRight.crossProduct(camera.VTo).normalize();
            if (!isZero(camera.VTo.dotProduct(camera.VRight)))
                throw new IllegalArgumentException("vector VTo needs to be orthogonal");
            return this;
        }

        /**
         * Sets the direction based on a center point using default up vector (Y-axis).
         *
         * @param pCenter the center point
         * @return this builder
         */
        public Builder setDirection(Point pCenter) {
            return this.setDirection(pCenter, Vector.AXIS_Y);
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width
         * @param height the height
         * @return this builder
         * @throws IllegalArgumentException if dimensions are non-positive
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0)
                throw new IllegalArgumentException("width and height needs to be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance
         * @return this builder
         * @throws IllegalArgumentException if distance is non-positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("distance need to be positive");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the image (number of pixels).
         *
         * @param nX number of columns
         * @param nY number of rows
         * @return this builder
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Finalizes and builds the camera.
         *
         * @return the constructed camera
         * @throws CloneNotSupportedException if cloning fails
         * @throws MissingResourceException   if essential camera properties are missing
         * @throws IllegalArgumentException   if resolution is invalid
         */
        public Camera build() throws CloneNotSupportedException {
            String exceptionMessageProblem = "renderer data messing";
            String exceptionMassageClass = "Camera";

            if (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("nX or nY is positive");
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if (camera.rayTracerBase == null)
                camera.rayTracerBase = new SimpleRayTracer(null);

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

            if(camera.rayTracerBase instanceof AdvancedRayTracer advancedRayTracer) {
                //if imageEnhancements contains an Antialiasing object, set the pixel for it
                for (AdvancedRayTracer enhancement : advancedRayTracer.enhancements) {
                    if (enhancement instanceof Antialiasing antialiasing) {
                        antialiasing.setTargetArea(
                                camera.VRight, camera.VUp,
                                camera.height/ camera.nY, camera.width/ camera.nX,
                                camera.pc
                        );
                    }
                }
            }

            return (Camera) camera.clone();
        }

        /**
         * Sets the ray tracer to be used for rendering.
         *
         * @param scene         the scene
         * @param rayTracerType the type of ray tracer
         * @return this builder
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            switch (rayTracerType) {
                case SIMPLE -> camera.rayTracerBase = new SimpleRayTracer(scene);
                case ADVANCED -> camera.rayTracerBase = new AdvancedRayTracer(scene);
                default -> camera.rayTracerBase = null;
            }
            return this;
        }

        public Builder setEnhancements(AdvancedRayTracer... imageEnhancements){
            ((AdvancedRayTracer) camera.rayTracerBase).addEnhancements(imageEnhancements);
            if(camera.rayTracerBase == null || !(camera.rayTracerBase instanceof AdvancedRayTracer)) {
                throw new IllegalStateException("Ray tracer must be an instance of AdvancedRayTracer to add enhancements");
            }
            return this;
        }

        public Builder setAccelerations(Scene scene, AccelerationType... accelerations) {
            if (camera.rayTracerBase == null || !(camera.rayTracerBase instanceof AdvancedRayTracer)) {
                throw new IllegalStateException("Ray tracer must be an instance of AdvancedRayTracer to add accelerations");
            }
            for(AccelerationType acceleration : accelerations) {
                switch (acceleration) {
                    case ADAPTIVE_SUPER_SAMPLING -> {
                        List<AdvancedRayTracer> enhancements =
                                ((AdvancedRayTracer) camera.rayTracerBase).enhancements;
                        camera.rayTracerBase = new AdaptiveSuperSamplingRayTracer(scene);
                        ((AdvancedRayTracer) camera.rayTracerBase).enhancements = enhancements;
                    }
                    default -> throw new IllegalArgumentException("Unknown acceleration type: " + acceleration);
                }
            }
            return this;
        }

        /**
         * Moves the camera forward or backward along its viewing direction.
         *
         * @param delta the distance to move the camera along the viewing direction
         * @return this builder
         */
        public Builder zoom(double delta) {
            moveAndLookAt(camera.VTo.scale(delta));
            return this;
        }

        /**
         * Moves the camera by a given vector and adjusts its viewing direction to look at the center point.
         *
         * @param delta the vector by which to move the camera
         * @return this builder
         */
        public Builder moveAndLookAt(Vector delta) {
            camera.p0 = Transform.movePoints(delta, camera.p0).get(0);
            Vector newVTo = camera.pc.subtract(camera.p0).normalize();
            camera.VTo = newVTo;
            camera.VRight = camera.VTo.crossProduct(camera.VUp).normalize();
            camera.VUp = camera.VRight.crossProduct(camera.VTo).normalize();
            return this;
        }

        /**
         * Rotates the camera around its viewing direction by the specified angle.
         *
         * @param angleDegrees the angle in degrees to rotate the camera
         * @return this builder
         */
        public Builder roll(double angleDegrees) {
            // Rotate the camera's up vector around the viewing direction
            camera.VUp = Transform.rotateVectorsClockwise(
                    angleDegrees, new Ray(camera.p0, camera.VTo), camera.VUp
            ).get(0);
            // Recalculate the right vector
            camera.VRight = camera.VTo.crossProduct(camera.VUp).normalize();
            return this;
        }

    }

    /**
     * Represents the viewing direction vector of the camera.
     */
    private Vector VTo;

    /**
     * Represents the upward direction vector of the camera.
     */
    private Vector VUp;

    /**
     * Represents the rightward direction vector of the camera.
     */
    private Vector VRight;

    /**
     * Represents the position of the camera in 3D space.
     */
    private Point p0;

    /**
     * Represents the width of the view plane.
     */
    double width = 0;

    /**
     * Represents the height of the view plane.
     */
    double height = 0;

    /**
     * Represents the distance from the camera to the view plane.
     */
    double distance = 0;

    /**
     * Represents the center point of the view plane.
     */
    private Point pc;

    /**
     * Handles writing the rendered image to a file.
     */
    ImageWriter imageWriter;

    /**
     * Handles ray tracing for rendering the scene.
     */
    RayTracerBase rayTracerBase;

    /**
     * Represents the number of columns in the view plane resolution.
     */
    int nX = 1;

    /**
     * Represents the number of rows in the view plane resolution.
     */
    int nY = 1;

    private Camera() {
    }

    /**
     * Returns a new camera builder.
     *
     * @return the builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    public static Builder getBuilder(Camera oldCamera) {
        return new Builder(oldCamera);
    }

    /**
     * Constructs a ray through a specific pixel in the view plane.
     *
     * @param nX total number of columns in the view plane
     * @param nY total number of rows in the view plane
     * @param j  column index of the pixel
     * @param i  row index of the pixel
     * @return the constructed ray
     */
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

    /**
     * Renders the image by casting rays through every pixel in the view plane.
     *
     * @return the camera instance (for method chaining)
     */
    public Camera renderImage() {
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                castRay(nX, nY, j, i);
            }
        }
        return this;
    }

    /**
     * Draws a grid on the rendered image with the specified interval and color.
     *
     * @param color    the color of the grid lines
     * @param interval the spacing between grid lines
     * @return the camera instance (for method chaining)
     */
    public Camera printGrid(Color color, int interval) {
        // Draw vertical grid lines
        for (int x = 0; x < nX; x += interval) {
            for (int y = 0; y < nY; y++) {
                imageWriter.writePixel(x, y, color);
            }
        }

        // Draw horizontal grid lines
        for (int y = 0; y < nY; y += interval) {
            for (int x = 0; x < nX; x++) {
                imageWriter.writePixel(x, y, color);
            }
        }

        return this;
    }

    /**
     * Writes the rendered image to a file with the specified name.
     *
     * @param imageName the name of the file to write the image to
     * @return the camera instance (for method chaining)
     */
    public Camera writeToImage(String imageName) {
        imageWriter.writeToImage(imageName);
        return this;
    }

    /**
     * Casts a single ray through the specified pixel and colors it in the rendered image.
     *
     * @param nx     total number of columns in the view plane
     * @param ny     total number of rows in the view plane
     * @param column column index of the pixel
     * @param row    row index of the pixel
     */
    private void castRay(int nx, int ny, int column, int row) {
        Ray ray = constructRay(nx, ny, column, row);
        Color color = rayTracerBase.traceRay(ray);
        imageWriter.writePixel(column, row, color);
    }
}
