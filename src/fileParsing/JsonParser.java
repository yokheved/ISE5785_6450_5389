package fileParsing;

import geometries.*;
import lighting.AmbientLight;
import org.json.JSONArray;
import primitives.*;
import scene.Scene;
import lighting.*;

import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
                    new Color(alColor.getInt("r"), alColor.getInt("g"), alColor.getInt("b"))
            ));
        }

        if(json.has("lights")){
            JSONArray lights = json.getJSONArray("lights");
            scene.setLights(parseLights(lights));
        }

        if (json.has("geometries")) {
            scene.setGeometries(parseGeometries(json));
        }

        return scene;
    }

    /**
     * Parses the "geometries" section of the JSON and constructs a {@link Geometries} object.
     *
     * @param json the JSON object containing geometry definitions
     * @return a {@link Geometries} object with all parsed geometries
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

                case "geometries":
                    Geometries geometries1 = parseGeometries(geoObj);
                    geometries.add(geometries1);
                    return geometries;

                default:
                    throw new IllegalArgumentException("Unknown geometry type: " + type);
            }

            if (geoObj.has("material")) {
                geometry.setMaterial(parseMaterial(geoObj.getJSONObject("material")));
            }

            if (geoObj.has("emission")) {
                JSONObject emissionObj = geoObj.getJSONObject("emission");
                Color emission = new Color(
                        emissionObj.getInt("r"),
                        emissionObj.getInt("g"),
                        emissionObj.getInt("b")
                );
                geometry.setEmission(emission);
            }

            geometries.add(geometry);
        }

        return geometries;
    }

    /**
     * Parses a JSON object representing material properties into a {@link Material} instance.
     *
     * @param material the JSON object describing material properties
     * @return the constructed {@link Material} object
     */
    private Material parseMaterial(JSONObject material) {
        Material mat = new Material();

        if (material.has("ka")) {
            JSONObject ka = material.getJSONObject("ka");
            Double3 kaD3 = ka.has("x") ? parseDouble3(ka) : new Double3(ka.getDouble("d"));
            mat.setKA(kaD3);
        }

        if (material.has("kd")) {
            JSONObject kd = material.getJSONObject("kd");
            Double3 kdD3 = kd.has("x") ? parseDouble3(kd) : new Double3(kd.getDouble("d"));
            mat.setKD(kdD3);
        }

        if (material.has("ks")) {
            JSONObject ks = material.getJSONObject("ks");
            Double3 ksD3 = ks.has("x") ? parseDouble3(ks) : new Double3(ks.getDouble("d"));
            mat.setKS(ksD3);
        }

        if (material.has("nSh")) {
            mat.setShininess(material.getDouble("nSh"));
        }

        if (material.has("kt")) {
            JSONObject kt = material.getJSONObject("kt");
            Double3 ktD3 = kt.has("x") ? parseDouble3(kt) : new Double3(kt.getDouble("d"));
            mat.setKT(ktD3);
        }

        if (material.has("kr")) {
            JSONObject kr = material.getJSONObject("kr");
            Double3 krD3 = kr.has("x") ? parseDouble3(kr) : new Double3(kr.getDouble("d"));
            mat.setKR(krD3);
        }


        return mat;
    }

    /**
     * Parses a JSON object with x, y, and z fields into a {@link Double3} instance.
     *
     * @param double3 the JSON object representing a Double3 vector
     * @return the parsed {@link Double3} value
     */
    private Double3 parseDouble3(JSONObject double3) {
        double x = double3.getDouble("x");
        double y = double3.getDouble("y");
        double z = double3.getDouble("z");
        return new Double3(x, y, z);
    }

    /**
     * Parses a JSON object with x, y, and z fields into a {@link Point} instance.
     *
     * @param point the JSON object representing a 3D point
     * @return the parsed {@link Point}
     */
    private Point parsePoint(JSONObject point) {
        return new Point(parseDouble3(point));
    }

    /**
     * Parses a JSON object representing a ray with "head" and "direction" fields.
     *
     * @param rayObj the JSON object containing ray information
     * @return the constructed {@link Ray}
     */
    private Ray parseRay(JSONObject rayObj) {
        Point head = parsePoint(rayObj.getJSONObject("head"));
        Point direction = parsePoint(rayObj.getJSONObject("direction"));
        return new Ray(head, new Vector(direction));
    }

    /**
     * Parses a JSON array of light sources into an array of {@link LightSource} objects.
     *
     * @param lightsArray the JSON array representing light definitions
     * @return an array of {@link LightSource} objects
     */
    private LightSource[] parseLights(JSONArray lightsArray) {
        List<LightSource> lights = new ArrayList<>();

        for (int i = 0; i < lightsArray.length(); i++) {
            JSONObject lightObj = lightsArray.getJSONObject(i);
            String type = lightObj.getString("type").toLowerCase();
            Color intensity = parseColor(lightObj.getJSONObject("intensity"));

            switch (type) {
                case "point":
                    Point position = parsePoint(lightObj.getJSONObject("position"));
                    lights.add(new PointLight(intensity, position));
                    break;

                case "spot":
                    Point spotPosition = parsePoint(lightObj.getJSONObject("position"));
                    Vector direction = new Vector(parsePoint(lightObj.getJSONObject("direction")));
                    SpotLight spot = new SpotLight(intensity, spotPosition, direction);
                    if (lightObj.has("beamWidth")) {
                        spot.setNarrowBeam(lightObj.getDouble("beamWidth"));
                    }
                    lights.add(spot);
                    break;

                case "directional":
                    Vector dir = new Vector(parsePoint(lightObj.getJSONObject("direction")));
                    lights.add(new DirectionalLight(intensity, dir));
                    break;

                default:
                    throw new IllegalArgumentException("Unknown light type: " + type);
            }
        }

        return lights.toArray(new LightSource[0]);
    }

    /**
     * Parses a JSON object with "r", "g", and "b" fields into a {@link Color} object.
     *
     * @param colorObj the JSON object representing RGB color
     * @return the parsed {@link Color}
     */
    private Color parseColor(JSONObject colorObj) {
        int r = colorObj.getInt("r");
        int g = colorObj.getInt("g");
        int b = colorObj.getInt("b");
        return new Color(r, g, b);
    }
}
