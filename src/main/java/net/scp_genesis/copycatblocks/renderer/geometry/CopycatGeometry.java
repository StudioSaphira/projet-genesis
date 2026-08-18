package net.scp_genesis.copycatblocks.renderer.geometry;

/**
 * Represents the geometry used by Copycat Blocks.
 *
 * <p>This class defines the physical shape of a Copycat Block independently
 * of its appearance.</p>
 *
 * <p>For version 0.1.x, only the Cube geometry will be supported.
 * Future versions will extend this system with Slabs, Stairs,
 * Panels, Slopes and other custom shapes.</p>
 */
public final class CopycatGeometry {

    /**
     * The geometry type.
     */
    public enum Shape {

        /**
         * Standard full cube.
         */
        CUBE

        // Future:
        // SLAB,
        // STAIRS,
        // PANEL,
        // SLOPE,
        // ...
    }

    /**
     * Geometry shape.
     */
    private final Shape shape;

    /**
     * Creates a new Copycat geometry.
     *
     * @param shape the geometry shape
     */
    public CopycatGeometry(Shape shape) {
        this.shape = shape;
    }

    /**
     * Returns the geometry shape.
     *
     * @return the geometry shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Returns whether this geometry represents a full cube.
     *
     * @return true if this geometry is a cube
     */
    public boolean isCube() {
        return shape == Shape.CUBE;
    }

}