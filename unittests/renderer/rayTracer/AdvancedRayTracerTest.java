package renderer.rayTracer;

import fileParsing.FileParser;
import fileParsing.JsonParser;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import renderer.AccelerationType;
import renderer.Camera;
import scene.Scene;

class AdvancedRayTracerTest {

    Vector vTo = new Vector(0, -0.447, -0.894).normalize();
    Vector vRight = vTo.crossProduct(new Vector(0,1,0)).normalize();
    Vector vUp = vRight.crossProduct(vTo).normalize();

    Camera.Builder cameraBuilder = Camera.getBuilder()
            .setLocation(new Point(0, 20, 20))
            .setDirection(vTo, vUp)
            .setVpDistance(50)
            .setVpSize(40, 40)
            .setResolution(800, 800);

    //test function for Antialiasing
    @Test
    void testAntialiasingPhoto() throws CloneNotSupportedException {
        //read a json file with scene data and build camera, and render to a photo
        Scene scene = new Scene("Test Scene");
        FileParser fileParser = new JsonParser("antialiasingTest.json");
        scene = fileParser.getScene(scene);

        Camera camera = cameraBuilder
                .setRayTracer(scene,RayTracerType.SIMPLE)
                .build();

        camera.renderImage().writeToImage("antialiasingTest-SimpleRayTracer");
    }

    @Test
    void testAntialiasingEnhancement() throws CloneNotSupportedException {
        //read a json file with scene data and build camera, and render to a photo
        Scene scene = new Scene("Test Scene");
        FileParser fileParser = new JsonParser("antialiasingTest.json");
        fileParser.getScene(scene);

        Camera cameraEnhancement = cameraBuilder
                .setRayTracer(scene,RayTracerType.ADVANCED)
                .setEnhancements(
                        new Antialiasing(scene).setRayConstructor(
                                new JitteredTargetArea(81)
                        )
                )
                .build();

        cameraEnhancement.renderImage().writeToImage("antialiasingTest-antialising");
        //max rays 25 -> 1 min 25 sec, 2 min 43 sec, 2 min 53 sec, 1 min 18 sec
    }

    @Test
    void testAntialiasingAccelerationADAPTIVE() throws CloneNotSupportedException {
        //read a json file with scene data and build camera, and render to a photo
        Scene scene = new Scene("Test Scene");
        FileParser fileParser = new JsonParser("antialiasingTest.json");
        fileParser.getScene(scene);

        Camera cameraAcceleration = cameraBuilder
                .setRayTracer(scene,RayTracerType.ADVANCED)
                .setAccelerations(scene, AccelerationType.ADAPTIVE_SUPER_SAMPLING)
                .setEnhancements(new Antialiasing(scene).setRayConstructor(
                        new JitteredTargetArea(81)
                ))
                .build();

        cameraAcceleration.renderImage().writeToImage("antialiasingTest-adaptiveSuperSampling");
        //max rays 25 -> 3 min 39 sec, 1 min 11 sec, 30 sec 773 ms
    }

    @Test
    void testAntialiasingAccelerationTHREADS() throws CloneNotSupportedException {
        //read a json file with scene data and build camera, and render to a photo
        Scene scene = new Scene("Test Scene");
        FileParser fileParser = new JsonParser("antialiasingTest.json");
        fileParser.getScene(scene);

        Camera cameraAcceleration = cameraBuilder
                .setRayTracer(scene,RayTracerType.ADVANCED)
                .setAccelerations(scene, AccelerationType.THREADS)
                .setEnhancements(new Antialiasing(scene).setRayConstructor(
                        new JitteredTargetArea(25)
                ))
                .build();

        cameraAcceleration.renderImage().writeToImage("antialiasingTest-threads");
    }
}