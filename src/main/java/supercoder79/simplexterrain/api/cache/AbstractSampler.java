package supercoder79.simplexterrain.api.cache;

import supercoder79.simplexterrain.api.noise.OctaveNoiseSampler;

public abstract class AbstractSampler {

    protected final OctaveNoiseSampler sampler;

    public AbstractSampler(OctaveNoiseSampler sampler) {
        this.sampler = sampler;
    }

    public abstract double sample(int x, int z);

    public abstract double sampleCustom(int x, int z, double samplingFrequency, double amplitude, int octaves);
}
