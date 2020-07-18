package supercoder79.simplexterrain.scripting;

import java.io.File;
import java.io.FileReader;
import java.util.Random;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.common.base.Supplier;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.gen.ChunkRandom;
import supercoder79.simplexterrain.SimplexTerrain;
import supercoder79.simplexterrain.api.noise.Noise2D;

public class SimplexScripting {
	private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	public static final Supplier<ScriptEngine> scriptEngine = () -> scriptEngineManager.getEngineByName("nashorn");
	public static final File scriptsLoc = new File(FabricLoader.getInstance().getConfigDirectory(), "simplexterrain/scripts");
	public static Random randomCache;
	public static Terrain terrain;

	public static void loadScripts(String loc) {
		System.out.println("Loading Scripts!");
		scriptsLoc.mkdirs();

		// Terrain Scripts

		if (loc.equals("default")) {
			System.out.println("Using default terrain.");
			terrain = null;
		} else {
			System.out.println("Using custom terrain script: " + loc);
			ScriptEngine engine = scriptEngine.get();

			// load script given
			try (FileReader reader = new FileReader(new File(scriptsLoc, loc))) {
				// Let scripters have access to noise generators and the config.
				Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
				bindings.put("NoiseGenerator", NoiseGenerator.class);
				bindings.put("config", SimplexTerrain.CONFIG);

				// load functions
				engine.eval(reader);
				terrain = new Terrain((Invocable) engine);
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}
	}

	public static boolean isModifierAllowed(String modifier) {
		if (terrain == null) {
			return true;
		} else {
			return terrain.isModifierAllowed(modifier);
		}
	}

	public static class Terrain implements Noise2D {
		private Terrain(Invocable engine) {
			this.engine = engine;
		}

		private final Invocable engine;
		private final Object2BooleanMap<String> allowedModifiers = new Object2BooleanArrayMap<>();

		public boolean isModifierAllowed(String name) {
			return this.allowedModifiers.computeBooleanIfAbsent(name, n -> {
				try {
					return (Boolean) engine.invokeFunction("isModifierAllowed", n);
				} catch (NoSuchMethodException e) {
					new SimplexScriptException("Missing <isModifierAllowed(noiseModifier) : boolean> function in script! Defaulting to false.", e).printStackTrace();
					return false;
				} catch (ScriptException e) {
					throw new RuntimeException(e);
				}
			});
		}

		public void setup(long seed, ChunkRandom random) {
			randomCache = random;

			try {
				engine.invokeFunction("setup", seed);
			} catch (NoSuchMethodException e) {
				throw new SimplexScriptException("Missing <setup(seed) : void> function in script!", e);
			} catch (ScriptException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public double sample(double x, double z) {
			try {
				return (Integer) engine.invokeFunction("getHeight", x, z);
			} catch (NoSuchMethodException e) {
				throw new SimplexScriptException("Missing <getHeight(x, z) : double> function in script!", e);
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
