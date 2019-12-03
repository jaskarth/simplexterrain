package supercoder79.simplexterrain.api;

/**
 * Any object that's able to retrieve a height from a heightmap.
 *
 * @author Valoeghese
 */
public interface Heightmap {
    int getHeight(int x, int z);

    Heightmap NONE = (x, z) -> 0;
}
