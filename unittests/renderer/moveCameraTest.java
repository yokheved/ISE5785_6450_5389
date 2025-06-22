package renderer;

import fileParsing.FileParser;
import fileParsing.JsonParser;
import geometries.Geometries;
import geometries.Sphere;
import lighting.AmbientLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class moveCameraTest {

    Scene scene;

    final Camera initialCamera;

    public moveCameraTest() throws CloneNotSupportedException {
        scene = new Scene("Teddy Bear Scene");
        FileParser fileParser = new JsonParser("PRO7part3teddybear.json");
        scene = fileParser.getScene(scene);

        initialCamera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 40))                     // Positioned in front of the teddy bear
                .setDirection(new Point(0, 0, -1), Vector.AXIS_Y)     // Looking straight toward negative Z
                .setVpDistance(40)                                    // Distance from camera to view plane
                .setVpSize(50, 50)
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build();                                   // View plane size (width x height)

    }

    @Test
    public void testZoom() throws CloneNotSupportedException {

        // Create a new camera using the constructor with a parameter
        Camera zoomedCamera = new Camera.Builder(initialCamera).zoom(5).build();

        // Render a simple scene with the zoomed camera
        zoomedCamera.renderImage().writeToImage("zoomedCameraTest.png");

    }

    @Test
    public void testMoveAndLookAt() throws CloneNotSupportedException {
        // Create a new camera using the constructor with a parameter
        Camera movedCamera = new Camera.Builder(initialCamera).moveAndLookAt(new Vector(1, 1, 1)).build();

        // Render a simple scene with the moved camera
        movedCamera.renderImage().writeToImage("movedCameraTest");
    }

    @Test
    public void testRoll() throws CloneNotSupportedException {
        // Create a new camera using the constructor with a parameter
        Camera rolledCamera = new Camera.Builder(initialCamera).roll(Math.PI / 2).build();

        // Render a simple scene with the rolled camera
        rolledCamera.renderImage().writeToImage("rolledCameraTest");

    }

    @Test
    public void testRoll180Degrees() throws CloneNotSupportedException {
        // Create a new camera using the constructor with a parameter
        Camera rolledCamera = new Camera.Builder(initialCamera).roll(180).build();

        // Render a simple scene with the rolled camera
        rolledCamera.renderImage().writeToImage("rolledCamera180DegreesTest");

        // Comment: The image will show the scene flipped vertically due to the 180-degree roll.
    }

    @Test
    public void testMoveAndLookAtWithZoom() throws CloneNotSupportedException {
        // Create a new camera using the constructor with a parameter
        Camera movedAndZoomedCamera = new Camera.Builder(initialCamera)
                .moveAndLookAt(new Vector(1, 5, 3))
                .zoom(20)
                .roll(30)
                .build();

        // Render a simple scene with the moved and zoomed camera
        movedAndZoomedCamera.renderImage().writeToImage("movedAndZoomedCameraTest");
    }
}
