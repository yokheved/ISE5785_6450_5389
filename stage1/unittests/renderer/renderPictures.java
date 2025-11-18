package renderer;

import fileParsing.FileParser;
import fileParsing.JsonParser;
import org.junit.jupiter.api.Test;

import primitives.*;
import renderer.rayTracer.RayTracerType;
import scene.Scene;

public class renderPictures {
    /** Default constructor to satisfy JavaDoc generator */
    public renderPictures() { /* to satisfy JavaDoc generator */ }
    /** Camera builder for teddy bear scene */
    private final Camera.Builder camera = Camera.getBuilder()
            .setLocation(new Point(0, 0, 40))                     // Positioned in front of the teddy bear
            .setDirection(new Point(0, 0, -1), Vector.AXIS_Y)     // Looking straight toward negative Z
            .setVpDistance(40)                                    // Distance from camera to view plane
            .setVpSize(50, 50);                                   // View plane size (width x height)

    /** Test for teddy bear scene in JSON format */
    @Test
    public void renderTeddyBearFromJson() throws CloneNotSupportedException {
        Scene scene = new Scene("Teddy Bear Scene");
        FileParser fileParser = new JsonParser("PRO7part3teddybear.json");
        scene = fileParser.getScene(scene);//.addAxes();

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("teddy bear render test");
    }


}
