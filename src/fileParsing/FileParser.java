package fileParsing;

import scene.Scene;
import java.io.File;

/**
 * An abstract base class for parsing files into a {@link Scene} object.
 * Subclasses must implement methods to open a file, extract its raw data,
 * and analyze the data to populate a {@link Scene}.
 */
public abstract class FileParser {
    final private String fileName;

    /**
     * Constructs a FileParser with the specified file name.
     *
     * @param fileName the name of the file to be parsed
     */
    public FileParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parses the file and populates the given {@link Scene} object.
     *
     * @param scene the scene to populate with data from the file
     * @return the populated scene
     */
    public Scene getScene(Scene scene){
        File file = openFile(fileName);
        if(!validateFile(file))
            throw new IllegalArgumentException("Invalid JSON file format: " + file.getName());
        String rawData = extractData(file);
        analyzeData(rawData, scene);
        return scene;
    }

    /**
     * Opens the file with the given name.
     *
     * @param fileName the name of the file to open
     * @return the opened file
     */
    protected abstract File openFile(String fileName);

    /**
     * Extracts raw data from the given file.
     *
     * @param file the file from which to extract data
     * @return the raw data as a string
     */

    protected abstract String extractData(File file);

    /**
     * Validates the JSON file format before parsing.
     * <p>
     * This method checks:
     * <ul>
     *     <li>That the file exists and is not empty</li>
     *     <li>That the file has a .<format> extension</li>
     *     <li>That the content is valid format</li>
     *     <li>That required fields (like "geometries") are present</li>
     * </ul>
     *
     * @param file the file to validate
     * @return true if the file is valid and ready for parsing; false otherwise
     */
    protected abstract boolean validateFile(File file);

    /**
     * Analyzes the raw data and updates the given {@link Scene} object accordingly.
     *
     * @param rawData the raw data to analyze
     * @param scene the scene to update with the analyzed data
     * @return the updated scene
     */
    protected abstract Scene analyzeData(String rawData, Scene scene);
}

