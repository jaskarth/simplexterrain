package supercoder79.simplexterrain.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

public class DefaultScripts {
	public static void create(File scriptsLoc) {
		// Yes it writes these every time
		// If people want to actually mess with terrain gen they should go create their own files
		// Thanks, Valo.

		create(scriptsLoc, "baseShape.js", file -> {
			file.println("var baseShapeNoise;");
			file.println();
			file.println("function init(seed) {");
			file.println("  baseShapeNoise = new NoiseGenerator(config.noiseGenerator, config.mainOctaveAmount, config.mainFrequency, config.mainAmplitudeHigh, config.mainAmplitudeLow);");
			file.println("}");
			file.println();
			file.println("function getHeight(x, z, currentHeight) {");
			file.println("  return baseShapeNoise.sample(x, z);");
			file.println("}");
		});

		create(scriptsLoc, "mountains.js", file -> {
			file.println("var mountainNoise;");
			file.println();
			file.println("function init(seed) {");
			file.println("  mountainNoise = new NoiseGenerator(config.noiseGenerator, mountainConfig.octaves, mountainConfig.frequency, mountainConfig.amplitudeHigh, mountainConfig.amplitudeLow);");
			file.println("}");
			file.println();
			file.println("function getHeight(x, z, currentHeight) {");
			file.println("  return currentHeight + Math.max(mountainNoise.sample(x, z), 0);");
			file.println("}");
		});

		create(scriptsLoc, "ridges.js", file -> {
			file.println("var ridgedNoise;");
			file.println();
			file.println("function init(seed) {");
			file.println("  ridgedNoise = new NoiseGenerator(config.noiseGenerator, ridgesConfig.octaves, ridgesConfig.frequency, 1, 1);");
			file.println("}");
			file.println();
			file.println("function getHeight(x, z, currentHeight) {");
			file.println("  return currentHeight + (1 - Math.abs(ridgedNoise.sample(x, z)) * ridgesConfig.amplitude);");
			file.println("}");
		});

		create(scriptsLoc, "details.js", file -> {
			file.println("var detailNoise;");
			file.println();
			file.println("function init(seed) {");
			file.println("  detailNoise = new NoiseGenerator(config.noiseGenerator, detailConfig.octaves, detailConfig.frequency, detailConfig.amplitudeHigh, detailConfig.amplitudeLow);");
			file.println("}");
			file.println();
			file.println("function getHeight(x, z, currentHeight) {");
			file.println("  return currentHeight + detailNoise.sample(x, z);");
			file.println("}");
		});

		create(scriptsLoc, "sigmoid.js", file -> {
			file.println("function init(seed) {");
			file.println("}");
			file.println();
			file.println("function getHeight(x, z, currentHeight) {");
			file.println("  return NoiseMath.sigmoid(currentHeight)");
			file.println("}");
		});
	}

	private static void create(File scriptsLoc, String name, Consumer<PrintWriter> writerConsumer) {
		File file = new File(scriptsLoc, name);

		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		try (PrintWriter writer = new PrintWriter(file)) {
			writerConsumer.accept(writer);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
