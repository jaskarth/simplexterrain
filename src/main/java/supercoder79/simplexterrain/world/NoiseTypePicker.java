package supercoder79.simplexterrain.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.math.Vec3d;
import supercoder79.simplexterrain.noise.gradient.OpenSimplexNoise;
import supercoder79.simplexterrain.world.noisetype.DefaultNoiseType;
import supercoder79.simplexterrain.world.noisetype.NoiseType;

import java.util.*;

public class NoiseTypePicker {
    private final OpenSimplexNoise noise1;
    private final OpenSimplexNoise noise2;
    private final OpenSimplexNoise noise3;
    private final Map<Vec3d, NoiseType> points;

    public NoiseTypePicker(Random random, List<NoiseType> types) {
        this.noise1 = new OpenSimplexNoise(random.nextLong());
        this.noise2 = new OpenSimplexNoise(random.nextLong());
        this.noise3 = new OpenSimplexNoise(random.nextLong());

        Map<Vec3d, NoiseType> points = new HashMap<>();
        for (int i = 0; i < types.size(); i++) {
            double theta = (i / (double)types.size()) * Math.PI * 2;
            points.put(new Vec3d(Math.cos(theta), Math.atan(theta - Math.PI) * 0.8, Math.sin(theta)), types.get(i));
        }

        this.points = points;
    }

    public NoiseType get(double x, double z) {
        double ax = this.noise1.sample( x / 240.0, z / 240.0);
        double ay = this.noise2.sample( x / 240.0, z / 240.0);
        double az = this.noise3.sample(x / 240.0, z / 240.0);

        Vec3d vec = new Vec3d(ax, ay, az);
        double minimumDist = Double.MAX_VALUE;
        NoiseType closest = DefaultNoiseType.INSTANCE;

        for (Map.Entry<Vec3d, NoiseType> type : this.points.entrySet()) {
            double dist = type.getKey().squaredDistanceTo(vec);
            if (dist < minimumDist) {
                minimumDist = dist;
                closest = type.getValue();
            }
        }

        return closest;
    }
}
