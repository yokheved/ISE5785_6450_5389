package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {

    /**
     * test method for{@link ImageWriter#writeToImage(String)}
     */
    @Test
    void testWriteToImage() {
        ImageWriter imageWriter = new ImageWriter(800, 500);

        int gridX = 16;
        int gridY = 10;

        int distanceX = (int) Math.ceil((double) imageWriter.nX() / gridX);
        int distanceY = (int) Math.ceil((double) imageWriter.nY() / gridY);

// Fill background with pink
        for (int x = 0; x < imageWriter.nX(); x++) {
            for (int y = 0; y < imageWriter.nY(); y++) {
                imageWriter.writePixel(x, y, new Color(java.awt.Color.PINK));
            }
        }

// Draw vertical grid lines
        for (int gx = 0; gx <= gridX; gx++) {
            int x = gx * distanceX;
            if (x >= imageWriter.nX()) continue; // avoid overflow
            for (int y = 0; y < imageWriter.nY(); y++) {
                imageWriter.writePixel(x, y, new Color(java.awt.Color.MAGENTA));
            }
        }

// Draw horizontal grid lines
        for (int gy = 0; gy <= gridY; gy++) {
            int y = gy * distanceY;
            if (y >= imageWriter.nY()) continue;
            for (int x = 0; x < imageWriter.nX(); x++) {
                imageWriter.writePixel(x, y, new Color(java.awt.Color.MAGENTA));
            }
        }

        imageWriter.writeToImage("PRO5 pink grid");
    }
}