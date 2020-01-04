package supercoder79.simplexterrain.api;

/**
 * A function that produces a result for a given xz coordinate.
 *
 * @author Valoeghese
 */
@FunctionalInterface
public interface Coordinate2Function<T> {
	T apply(int x, int z);
}
