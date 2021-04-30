package supercoder79.simplexterrain.world.blend;

public class GatheredPoint<T> {
    private double x, z;
    private int hash;
    private T tag;
    
    public GatheredPoint(double x, double z, int hash) {
        this.x = x;
        this.z = z;
        this.hash = hash;
    }
    
    public double getX() {
        return x;
    }
    
    public double getZ() {
        return z;
    }
    
    public double getHash() {
        return hash;
    }
    
    public T getTag() {
        return tag;
    }
    
    public void setTag(T tag) {
        this.tag = tag;
    }
}