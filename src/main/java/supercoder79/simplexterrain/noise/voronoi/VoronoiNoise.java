package supercoder79.simplexterrain.noise.voronoi;

import supercoder79.simplexterrain.api.noise.Noise;

import java.util.Random;

public class VoronoiNoise extends Noise {

    private static final double SQRT_2 = 1.4142135623730950488;
    private static final double SQRT_3 = 1.7320508075688772935;

    private long seed;
    private short distanceMethod;


    public VoronoiNoise(long seed) {
        super(seed);
        this.seed = seed;
        this.distanceMethod = 0;
    }

    private double getDistance(double xDist, double zDist) {
        switch(distanceMethod) {
            case 0:
                return fastSqrt((xDist * xDist) + (zDist * zDist)) / SQRT_2;
            case 1:
                return xDist + zDist;
            case 2:
                return Math.pow(Math.E, Math.sqrt(xDist * xDist + zDist * zDist) / SQRT_2) / Math.E;
            default:
                return 1.0;
        }
    }

    private double getDistance(double xDist, double yDist, double zDist) {
        switch(distanceMethod) {
            case 0:
                return fastSqrt(xDist * xDist + yDist * yDist + zDist * zDist) / SQRT_3;
            case 1:
                return xDist + yDist + zDist;
            default:
                return 1.0;
        }
    }

    public short getDistanceMethod() {
        return distanceMethod;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public double sample(double x, double z) {
        int xInt = (x > .0? (int)x: (int)x - 1);
        int zInt = (z > .0? (int)z: (int)z - 1);

        double minDist = 32000000.0;

        double xCandidate = 0;
        double zCandidate = 0;

        for(int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
            for(int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {

                double xPos = xCur + valueNoise2D(xCur, zCur, seed);
                double zPos = zCur + valueNoise2D(xCur, zCur, new Random(seed).nextLong());
                double xDist = xPos - x;
                double zDist = zPos - z;
                double dist = xDist * xDist + zDist * zDist;

                if(dist < minDist) {
                    minDist = dist;
                    xCandidate = xPos;
                    zCandidate = zPos;
                }
            }
        }

        double xDist = xCandidate - x;
        double zDist = zCandidate - z;

        return getDistance(xDist, zDist);
    }

    @Override
    public double sample(double x, double y, double z) {

        int xInt = (x > .0? (int)x: (int)x - 1);
        int yInt = (y > .0? (int)y: (int)y - 1);
        int zInt = (z > .0? (int)z: (int)z - 1);

        double minDist = 32000000.0;

        double xCandidate = 0;
        double yCandidate = 0;
        double zCandidate = 0;

        Random rand = new Random(seed);

        for(int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
            for(int yCur = yInt - 2; yCur <= yInt + 2; yCur++) {
                for(int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {

                    double xPos = xCur + valueNoise3D (xCur, yCur, zCur, seed);
                    double yPos = yCur + valueNoise3D (xCur, yCur, zCur, rand.nextLong());
                    double zPos = zCur + valueNoise3D (xCur, yCur, zCur, rand.nextLong());
                    double xDist = xPos - x;
                    double yDist = yPos - y;
                    double zDist = zPos - z;
                    double dist = xDist * xDist + yDist * yDist + zDist * zDist;

                    if(dist < minDist) {
                        minDist = dist;
                        xCandidate = xPos;
                        yCandidate = yPos;
                        zCandidate = zPos;
                    }
                }
            }
        }

        double xDist = xCandidate - x;
        double yDist = yCandidate - y;
        double zDist = zCandidate - z;

        return getDistance(xDist, yDist, zDist);
    }

    public void setDistanceMethod(short distanceMethod) {
        this.distanceMethod = distanceMethod;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public static double valueNoise2D(int x, int z, long seed) {
        long n = (1619 * x + 6971 * z + 1013 * seed) & 0x7fffffff;
        n = (n >> 13) ^ n;
        return 1.0 - ((double)((n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0);
    }

    public static double valueNoise3D(int x, int y, int z, long seed) {
        long n = (1619 * x + 31337 * y + 6971 * z + 1013 * seed) & 0x7fffffff;
        n = (n >> 13) ^ n;
        return 1.0 - ((double)((n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0);
    }

}