package renderer;

import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class CameraIntersectionsIntegrationTests {

    /**
     * Camera builder for the tests
     */

    private final Camera.Builder cameraBuilder = Camera.getBuilder().setVpDistance(1)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0));

    @Test
    void testVP3X3() throws CloneNotSupportedException {
        int VPResolution = 3;
        //TC01: sphere r = 1
        Camera camera1 = cameraBuilder.setLocation(new Point(0, 0, 0)).build();
        Sphere sphere1 = new Sphere(new Point(0, 0, -3), 1);
        for (int i = 0; i < VPResolution; i++) {
            for (int j = 0; j < VPResolution; j++) {
                 Ray ray1 = camera1.constructRay(VPResolution,VPResolution,i,j);

            }
        }

        sphere1.findIntersections(camera1.constructRay())


    }


}
