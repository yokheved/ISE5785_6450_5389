package renderer;

public enum AccelerationType {

    /**
     * Uses a bounding volume hierarchy (BVH) for acceleration.
     */
    BVH,

    /**
     * uses a thread for each pixel in the image.
     */
    THREADS,

    /**
     * Does not track all Rays in a Beam, but averages the prominent ones .
     */
    ADAPTIVE_SUPER_SAMPLING
}
