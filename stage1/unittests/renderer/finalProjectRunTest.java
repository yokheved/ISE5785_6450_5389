package renderer;

import fileParsing.FileParser;
import fileParsing.JsonParser;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import renderer.rayTracer.RayTracerType;
import scene.Scene;

public class finalProjectRunTest {

    Scene scene;

    Camera.Builder initialCamera;

    public finalProjectRunTest() throws CloneNotSupportedException {
        scene = new Scene("Teddy Bear Scene");
        FileParser fileParser = new JsonParser("finalScene.json");
        scene = fileParser.getScene(scene);

        initialCamera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 40))                     // Positioned in front of the teddy bear
                .setDirection(new Point(0, 0, -1), Vector.AXIS_Y)     // Looking straight toward negative Z
                .setVpDistance(40)                                    // Distance from camera to view plane
                .setVpSize(50, 50)
                .setResolution(1000, 1000);

    }

    @Test
    public void testFinalImageSimple() throws CloneNotSupportedException {

        // Create a new camera using the constructor with a parameter
        Camera simpleTracerCamera = initialCamera
                .setRayTracer(scene, RayTracerType.SIMPLE).build();

        // Render a simple scene with the zoomed camera
        simpleTracerCamera.renderImage().writeToImage("finalImageSimple");

    }
}