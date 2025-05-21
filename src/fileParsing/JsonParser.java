package fileParsing;

import geometries.*;
import lighting.AmbientLight;
import org.json.JSONArray;
import primitives.*;
import scene.Scene;

import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A parser that reads and processes scene data from a JSON file.
 * It supports various geometry types including Sphere, Plane, Triangle, Tube,
 * Polygon, and Cylinder. The parser also handles background color and ambient light.
 *
 * <p>This class implements the Template Method design pattern by extending {@link FileParser},
 * providing specific implementations for JSON files.</p>
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

    /**
     * Opens a JSON file from the "inputFiles" directory.
     *
     * @param fileName the name of the file to open
     * @return the opened File object
     * @throws IllegalArgumentException if the file does not exist
     */
    @Override
    protected File openFile(String fileName) {
        String filePath = "inputFiles/" + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        return file;
    }

    /**
     * Validates the structure and required fields of the JSON file.
     *
     * @param file the JSON file to validate
     * @return true if valid, false otherwise
     */
    @Override
    protected boolean validateFile(File file) {
        if (!file.getName().toLowerCase().endsWith(".json")) {
            System.err.println("Invalid file type. Expected a .json file.");
            return false;
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(content);

            if (!json.has("geometries")) {
                System.err.println("Missing required field: geometries");
                return false;
            }

            if (json.has("background")) {
                JSONObject bg = json.getJSONObject("background");
                if (!(bg.has("r") && bg.has("g") && bg.has("b"))) {
                    System.err.println("Background color must include r, g, b components.");
                    return false;
                }
            }

            if (json.has("ambientLight")) {
                JSONObject al = json.getJSONObject("ambientLight");
                if (!al.has("color") || !al.getJSONObject("color").has("r")) {
                    System.err.println("Invalid ambientLight format.");
                    return false;
                }
            }

            return true;

        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Invalid JSON structure: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts raw JSON content as a string from the given file.
     *
     * @param file the file to read
     * @return the raw JSON content
     */
    @Override
    protected String extractData(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    /**
     * Parses the raw JSON data and populates the Scene object accordingly.
     *
     * @param rawData the raw JSON string
     * @param scene the Scene object to populate
     * @return the populated Scene object
     */
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

        if (json.has("ambientLight")) {
            JSONObject alColor = json.getJSONObject("ambientLight").getJSONObject("color");
            scene.setAmbientLight(new AmbientLight(
                    new Color(alColor.getInt("r"), alColor.getInt("g"), alColor.getInt("b")))
            );
        }

        if (json.has("geometries")) {
            scene.setGeometries(parseGeometries(json));
        }

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
            Geometry geometry;

            switch (type) {
                case "sphere":
                    Point center = parsePoint(geoObj.getJSONObject("center"));
                    double radius = geoObj.getDouble("radius");
                    geometry = new Sphere(center, radius);
                    break;

                case "plane":
                    Point q0 = parsePoint(geoObj.getJSONObject("point"));
                    Vector normal = new Vector(parsePoint(geoObj.getJSONObject("normal")));
                    geometry = new Plane(q0, normal);
                    break;

                case "triangle":
                    Point p0 = parsePoint(geoObj.getJSONObject("p0"));
                    Point p1 = parsePoint(geoObj.getJSONObject("p1"));
                    Point p2 = parsePoint(geoObj.getJSONObject("p2"));
                    geometry = new Triangle(p0, p1, p2);
                    break;

                case "tube":
                    Ray axisRay = parseRay(geoObj.getJSONObject("axisRay"));
                    double tubeRadius = geoObj.getDouble("radius");
                    geometry = new Tube(axisRay, tubeRadius);
                    break;

                case "polygon":
                    JSONArray verticesArray = geoObj.getJSONArray("vertices");
                    Point[] vertices = new Point[verticesArray.length()];
                    for (int j = 0; j < verticesArray.length(); j++) {
                        vertices[j] = parsePoint(verticesArray.getJSONObject(j));
                    }
                    geometry = new Polygon(vertices);
                    break;

                case "cylinder":
                    Ray cylRay = parseRay(geoObj.getJSONObject("axisRay"));
                    double cylRadius = geoObj.getDouble("radius");
                    double height = geoObj.getDouble("height");
                    geometry = new Cylinder(height, cylRay, cylRadius);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown geometry type: " + type);
            }

            if (geoObj.has("material")) {
                geometry.setMaterial(parseMaterial(geoObj.getJSONObject("material")));
            }

            geometries.add(geometry);
        }

        return geometries;
    }

    /**
     * Parses a JSON object into a Material instance.
     *
     * @param material the JSON object describing material properties
     * @return the Material object
     */
    private Material parseMaterial(JSONObject material) {
        JSONObject ka = material.getJSONObject("ka");
        Double3 kaD3;

        if (ka.has("x")) {
            kaD3 = parseDouble3(ka);
        } else {
            kaD3 = new Double3(ka.getDouble("d"));
        }

        return new Material().setKa(kaD3);
    }

    /**
     * Parses a JSON object into a Double3.
     *
     * @param double3 the JSON object with x, y, and z
     * @return the Double3 object
     */
    private Double3 parseDouble3(JSONObject double3) {
        double x = double3.getDouble("x");
        double y = double3.getDouble("y");
        double z = double3.getDouble("z");
        return new Double3(x, y, z);
    }

    /**
     * Parses a JSON object into a Point instance.
     *
     * @param point the JSON object containing x, y, and z coordinates
     * @return the corresponding Point object
     */
    private Point parsePoint(JSONObject point) {
        return new Point(parseDouble3(point));
    }

    /**
     * Parses a JSON object into a Ray instance.
     *
     * @param rayObj the JSON object containing "head" and "direction"
     * @return the corresponding Ray object
     */
    private Ray parseRay(JSONObject rayObj) {
        Point head = parsePoint(rayObj.getJSONObject("head"));
        Point direction = parsePoint(rayObj.getJSONObject("direction"));
        return new Ray(head, new Vector(direction));
    }
}
