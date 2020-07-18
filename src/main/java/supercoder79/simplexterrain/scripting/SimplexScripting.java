package supercoder79.simplexterrain.scripting;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.common.base.Supplier;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;

public class SimplexScripting {
	private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	public static final Supplier<ScriptEngine> scriptEngine = () -> scriptEngineManager.getEngineByName("nashorn");
	public static final File scriptsLoc = new File(FabricLoader.getInstance().getConfigDirectory(), "simplexterrain/scripts");
	public static Random randomCache;
	public static List<Terrain> terrain = new ArrayList<>();

	public static void loadScripts(String[] algs) {
		System.out.println("Loading Scripts!");
		scriptsLoc.mkdirs();

		DefaultScripts.create(scriptsLoc);

		// Terrain Scripts

		for (String loc : algs) {
			System.out.println("Loading custom terrain script: " + loc);
			ScriptEngine engine = scriptEngine.get();

			// load script given
			try (FileReader reader = new FileReader(new File(scriptsLoc, loc))) {
				// Let scripts have access to noise generators and noise math
				engine.eval("var NoiseGenerator = Java.type(\"supercoder79.simplexterrain.scripting.NoiseGenerator\");");
				engine.eval("var NoiseMath = Java.type(\"supercoder79.simplexterrain.noise.NoiseMath\");");

				// Let scripts have access to the configs
				Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
				bindings.put("config", SimplexTerrain.CONFIG);
				bindings.put("mountainConfig", SimplexTerrain.MOUNTAIN_CONFIG);
				bindings.put("ridgesConfig", SimplexTerrain.RIDGES_CONFIG);
				bindings.put("detailConfig", SimplexTerrain.DETAIL_CONFIG);

				// load functions
				engine.eval(reader);
				terrain.add(new Terrain((Invocable) engine));
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}

		debugNoise();
	}

	private static void debugNoise() {
		ChunkRandom cr = new ChunkRandom(490);
		for (Terrain t : terrain) {
			t.init(490, cr);
		}

		for (int i = 0; i < 10; ++i) {
			System.out.println("Sample " + i);
			double currentHeight = 0;
			for (Terrain t : terrain) {
				System.out.println(currentHeight = t.sample(40 + 30 * i, 200, currentHeight));
			}
		}
	}

	public static class Terrain {
		private Terrain(Invocable engine) {
			this.engine = engine;
		}

		private final Invocable engine;

		public void init(long seed, ChunkRandom random) {
			randomCache = random;

			try {
				engine.invokeFunction("init", seed);
			} catch (NoSuchMethodException e) {
				throw new SimplexScriptException("Missing <init(seed) : void> function in script!", e);
			} catch (ScriptException e) {
				throw new RuntimeException(e);
			}
		}

		public double sample(double x, double z, double currentHeight) {
			try {
				return (Double) engine.invokeFunction("getHeight", x, z, currentHeight);
			} catch (NoSuchMethodException e) {
				throw new SimplexScriptException("Missing <getHeight(x, z, currentHeight) : double> function in script!", e);
			} catch (ScriptException e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class SimplexScriptException extends RuntimeException {
		private static final long serialVersionUID = 665662872594934663L;

		SimplexScriptException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
}
