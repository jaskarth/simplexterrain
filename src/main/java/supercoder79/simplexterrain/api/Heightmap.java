package supercoder79.simplexterrain.api;

public interface Heightmap {
    int getHeight(int x, int z);

    Heightmap NONE = (x, z) -> 0;
}
