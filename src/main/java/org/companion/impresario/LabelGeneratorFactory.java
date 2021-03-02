package org.companion.impresario;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * A general interface for label compilation. The implementation can accept various file format depends on its design.
 * </p>
 */
public interface LabelGeneratorFactory {

    /**
     * Creates a new ready-to-use data structure from configuration file
     *
     * @param file the configuration
     *
     * @return a map between group and the generator of the particular group
     *
     * @throws IOException if any IO errors occur.
     */
    Map<String, LabelGenerator> compile(File file) throws IOException;

    /**
     * Creates a new ready-to-use data structure from input stream
     *
     * @param stream the stream configuration
     *
     * @return a map between group and the generator of the particular group
     *
     * @throws IOException if any IO errors occur.
     */
    Map<String, LabelGenerator> compile(InputStream stream) throws IOException;

}
