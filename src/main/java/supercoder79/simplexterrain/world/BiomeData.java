package supercoder79.simplexterrain.world;

/**
 * Extra data for biome generation at a specific x/z coordinate.
 * This can be used to set specific biomes, force generation, and do other tasks.
 */
public class BiomeData {
    private int height = 0;
    private boolean river = false;
    private boolean forcedLowlands = false;

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setRiver() {
        this.river = true;
    }

    public boolean isRiver() {
        return river;
    }

    public boolean isForcedLowlands() {
        return forcedLowlands;
    }

    public void setForcedLowlands() {
        this.forcedLowlands = true;
    }
}
