package fileParsing;

import geometries.*;
import lighting.AmbientLight;
import org.json.JSONArray;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A parser that reads and processes scene data from a JSON file.
 * It supports various geometry types including Sphere, Plane, Triangle, Tube,
 * Polygon, and Cylinder. The parser also handles background color and ambient light.
 */
public class JsonParser extends FileParser {
    /**
     * Constructs a JsonParser with the specified file name.
     *
     * @param filename the name of the JSON file to be parsed
     */
    public JsonParser(String filename) {
        super(filename);
    }

    @Override
    protected File openFile(String fileName) {
        String filePath = "inputFiles/" + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        return file;
    }

    @Override
    protected String extractData(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    @Override
    protected Scene analyzeData(String rawData, Scene scene) {
        JSONObject json = new JSONObject(rawData);

        if (json.has("background")) {
            JSONObject bg = json.getJSONObject("background");
            scene.setBackground(new Color(
                    bg.getInt("r"),
                    bg.getInt("g"),
                    bg.getInt("b")
            ));
        }

        // Example: Set ambient light if exists
        if (json.has("ambientLight")) {
            JSONObject alColor = json.getJSONObject("ambientLight").getJSONObject("color");
            scene.setAmbientLight(new AmbientLight(
                    new Color(alColor.getInt("r"), alColor.getInt("g"), alColor.getInt("b")))
            );
        }
        if (json.has("geometries")) {
            scene.setGeometries(parseGeometries(json));
        }

        // You can add parsing for geometries manually if needed
        return scene;
    }

    /**
     * Parses the "geometries" section of the JSON and constructs a Geometries object.
     *
     * @param json the JSON object containing geometry definitions
     * @return a Geometries object with all parsed geometries
     */
    private Geometries parseGeometries(JSONObject json) {
        Geometries geometries = new Geometries();

        JSONArray geoArray = json.getJSONArray("geometries");
        for (int i = 0; i < geoArray.length(); i++) {
            JSONObject geoObj = geoArray.getJSONObject(i);
            String type = geoObj.getString("type").toLowerCase();

            switch (type) {
                case "sphere":
                    Point center = parsePoint(geoObj.getJSONObject("center"));
                    double radius = geoObj.getDouble("radius");
                    geometries.add(new Sphere(center, radius));
                    break;

                case "plane":
                    Point q0 = parsePoint(geoObj.getJSONObject("point"));
                    Vector normal = new Vector(parsePoint(geoObj.getJSONObject("normal")));
                    geometries.add(new Plane(q0, normal));
                    break;

                case "triangle":
                    Point p0 = parsePoint(geoObj.getJSONObject("p0"));
                    Point p1 = parsePoint(geoObj.getJSONObject("p1"));
                    Point p2 = parsePoint(geoObj.getJSONObject("p2"));
                    geometries.add(new Triangle(p0, p1, p2));
                    break;

                case "tube":
                    JSONObject axisRayObj = geoObj.getJSONObject("axisRay");
                    Ray axisRay = parseRay(axisRayObj);
                    double tubeRadius = geoObj.getDouble("radius");
                    geometries.add(new Tube(axisRay, tubeRadius));
                    break;

                case "polygon":
                    JSONArray verticesArray = geoObj.getJSONArray("vertices");
                    Point[] vertices = new Point[verticesArray.length()];
                    for (int j = 0; j < verticesArray.length(); j++) {
                        vertices[j] = parsePoint(verticesArray.getJSONObject(j));
                    }
                    geometries.add(new Polygon(vertices));
                    break;

                case "cylinder":
                    JSONObject cylRayObj = geoObj.getJSONObject("axisRay");
                    Ray cylRay = parseRay(cylRayObj);
                    double cylRadius = geoObj.getDouble("radius");
                    double height = geoObj.getDouble("height");
                    geometries.add(new Cylinder(height, cylRay, cylRadius));
                    break;

                default:
                    throw new IllegalArgumentException("Unknown geometry type: " + type);
            }
        }

        return geometries;
    }

    /**
     * Parses a JSON object into a Point instance.
     *
     * @param point the JSON object containing x, y, and z coordinates
     * @return the corresponding Point object
     */
    private Point parsePoint(JSONObject point) {
        double x = point.getDouble("x");
        double y = point.getDouble("y");
        double z = point.getDouble("z");
        return new Point(x, y, z);
    }

    /**
     * Parses a JSON object into a Ray instance.
     *
     * @param rayObj the JSON object containing "head" and "direction" points
     * @return the corresponding Ray object
     */
    private Ray parseRay(JSONObject rayObj) {
        Point head = parsePoint(rayObj.getJSONObject("head"));
        Point direction = parsePoint(rayObj.getJSONObject("direction"));
        return new Ray(head, new Vector(direction));
    }

}
