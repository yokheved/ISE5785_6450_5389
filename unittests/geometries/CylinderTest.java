package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    /**
     * test method for{@link Cylinder#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Cylinder cylinder = new Cylinder(5.0, axisRay, 1.0);


        // ============ Equivalence Partitions Tests ==============
        //TC1: on the round surface
        Point p1 = new Point(1, 0, 5); // על המשטח הסיבובי
        Vector normal1 = cylinder.getNormal(p1);
        assertEquals(new Vector(1, 0, 0), normal1, "Bad normal on the side surface of the cylinder");

        //TC2: on the base 1
        Point p2 = new Point(0, 1, 0); // על הבסיס הראשון, בציר ה-Y
        Vector normal2 = cylinder.getNormal(p2);
        assertEquals(new Vector(0, 0, -1), normal2, "Bad normal on the base 1 of the cylinder");

        //TC3: on the base 2
        Point p3 = new Point(0, 1, 5); // על הבסיס השני, בציר ה-Y
        Vector normal3 = cylinder.getNormal(p3);
        assertEquals(new Vector(0, 0, 1), normal3, "Bad normal on the base 2 of the cylinder");

        
        // =============== Boundary Values Tests ==================
        // TC4: in the center of base 1
        Point p4 = new Point(0, 0, 0); // במרכז הבסיס התחתון
        Vector normal4 = cylinder.getNormal(p4);
        assertEquals(new Vector(0, 0, -1), normal4, "Bad normal at the center of base 1");

        // TC5: in the center of base 2
        Point p5 = new Point(0, 0, 5); // במרכז הבסיס העליון
        Vector normal5 = cylinder.getNormal(p5);
        assertEquals(new Vector(0, 0, 1), normal5, "Bad normal at the center of base 2");

        //TC6: on the base 1 edge
        Point p6 = new Point(1, 0, 0); // על הקצה של הבסיס התחתון
        Vector normal6 = cylinder.getNormal(p6);
        assertEquals(new Vector(1, 0, 0), normal6, "Bad normal at the edge of base 1");

        //TC7: on the base 2 edge
        Point p7 = new Point(1, 0, 5); // על הקצה של הבסיס העליון
        Vector normal7 = cylinder.getNormal(p7);
        assertEquals(new Vector(1, 0, 0), normal7, "Bad normal at the edge of base 2");
    }
}