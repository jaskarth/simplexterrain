package supercoder79.simplexterrain.api.postprocess;

/**
 * Specifies the type of a post processor.
 *
 * @author SuperCoder79
 */
public enum PostProcessorType {
    /**
     * Is executed after surface building and before the generation of features.
     */
    POST,
    /**
     * Is executed after raw generation of stone and water.
     */
    PRE
}
