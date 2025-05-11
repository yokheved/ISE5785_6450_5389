package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CameraIntersectionsIntegrationTests {

    /**
     * Camera builder for the tests
     */

    private final Camera.Builder cameraBuilder = Camera.getBuilder().setVpDistance(1)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0));

    @Test
    void testVP3X3() throws CloneNotSupportedException {
        int VPResolution = 3;
        cameraBuilder.setVpSize(VPResolution, VPResolution);
        testVPs(VPResolution);
    }

    private void testVPs(int VPResolution) throws CloneNotSupportedException{

        String notNullMessage = "list of intersection points must not be null";
        String nullMessage = "list of intersection points must be null";
        String sizeRongMessage = "size of list should be ";

                //TC011: sphere r = 1 p0 = (0,0,0)
        Camera camera1 = cameraBuilder.setLocation(new Point(0, 0, 0)).build();
        Sphere sphere1 = new Sphere(new Point(0, 0, -3), 1);
        List<Point> result1 = VPFindIntersections(camera1, sphere1, VPResolution);
        assertNotNull(result1, "TC011: "+ notNullMessage);
        assertEquals(2, result1.size(), "TC011: " + sizeRongMessage + 2);

        //TC012: sphere r = 2.5 p0 = (0,0,0.5)
        Camera camera2 = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();
        Sphere sphere2 = new Sphere(new Point(0, 0, -2.5), 2.5);
        List<Point> result2 = VPFindIntersections(camera2, sphere2, VPResolution);
        assertNotNull(result2, "TC012: " + notNullMessage);
        assertEquals(18, result2.size(), "TC012: " + sizeRongMessage + 18);

        //TC013: sphere r = 2 p0 = (0,0,0.5)
        Sphere sphere3 = new Sphere(new Point(0, 0, -2), 2);
        List<Point> result3 = VPFindIntersections(camera2, sphere3, VPResolution);
        assertNotNull(result3, "TC013: " + notNullMessage);
        assertEquals(10, result3.size(), "TC013: " + sizeRongMessage + 10);

        //TC014: sphere r = 4 p0 = (0,0,0.5)
        Sphere sphere4 = new Sphere(new Point(0, 0, -1), 4);
        List<Point> result4 = VPFindIntersections(camera2, sphere4, VPResolution);
        assertNotNull(result4, "TC014: " + notNullMessage);
        assertEquals(9, result4.size(), "TC014: " + sizeRongMessage + 9);

        //TC015: sphere r = 0.5 p0 = (0,0,0.5)
        Sphere sphere5 = new Sphere(new Point(0, 0, 1), 0.5);
        List<Point> result5 = VPFindIntersections(camera2, sphere5, VPResolution);
        assertNull(result5, "TC015: " + nullMessage);

        //TC021: plane n = vup
        Plane plane1 = new Plane(new Point(0, 0, -5), new Vector(0,1,0));
        List<Point> result6 = VPFindIntersections(camera1, plane1, VPResolution);
        assertNotNull(result6, "TC021: " + notNullMessage);
        assertEquals(9, result6.size(), "TC021:  " + sizeRongMessage + 9);

        //TC022: plane n = (0,1,1)
        Plane plane2 = new Plane(new Point(0, 0, -5), new Vector(0,1,1));
        List<Point> result7 = VPFindIntersections(camera1, plane2, VPResolution);
        assertNotNull(result7, "TC022: " + notNullMessage);
        assertEquals(9, result7.size(), "TC022: " + sizeRongMessage + 9);

        //TC023: plane n = (0,0.1,1)
        Plane plane3 = new Plane(new Point(0, 0, -5), new Vector(0,0.1,1));
        List<Point> result8 = VPFindIntersections(camera1, plane3, VPResolution);
        assertNotNull(result8, "TC023: " + notNullMessage);
        assertEquals(6, result8.size(), "TC023: " + sizeRongMessage + 6);

        //TC031: size of 1 px
        Triangle triangle1 = new Triangle(new Point(0, 0.5, -2),
                new Point(0.5, -0.5, -2), new Point(-0.5, -0.5, -2));
        List<Point> result9 = VPFindIntersections(camera1, triangle1, VPResolution);
        assertNotNull(result9, "TC031: " + notNullMessage);
        assertEquals(1, result9.size(), "TC031: " + sizeRongMessage + 1);

        //TC032: size of 2 px
        Triangle triangle2 = new Triangle(new Point(0, 2, -2),
                new Point(0.5, -0.5, -2), new Point(-0.5, -0.5, -2));
        List<Point> result10 = VPFindIntersections(camera1, triangle2, VPResolution);
        assertNotNull(result10, "TC032: " + notNullMessage);
        assertEquals(2, result10.size(), "TC032: " + sizeRongMessage + 2);



    }

    private List<Point> VPFindIntersections(Camera camera, Intersectable intersect, int VPResolution){
        List<Point> result = null;
        for (int i = 0; i < VPResolution; i++) {
            for (int j = 0; j < VPResolution; j++) {
                Ray ray1 = camera.constructRay(VPResolution, VPResolution, i, j);
                List<Point> resultPixel = intersect.findIntersections(ray1);
                if(resultPixel != null)
                    if(result == null)  result = new LinkedList<>();
                    result.addAll(resultPixel);
            }
        }
        return result;
    }

}
