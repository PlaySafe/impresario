package org.companion.impresario;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * A general interface for ValidationRule compilation. The implementation can accept various file format depends on its design.
 */
public interface ValidationRuleFactory {

    /**
     * Compiles all configurations to the ready-to-use data structure.
     * The result will be a map between validation group and executable
     * validation rule that correspond to the configuration
     *
     * @param file the validation rule configuration file
     * @return a map between group and executable validation rule
     *
     * @throws IOException if any problem about IO occur
     */
    Map<String, ValidationRule> compile(File file) throws IOException;


    /**
     * Compiles all configurations to the ready-to-use data structure.
     * The result will be a map between validation group and executable
     * validation rule that correspond to the configuration
     *
     * @param stream an input stream of configuration file
     * @return a map between group and executable validation rule
     *
     * @throws IOException if any problem about IO occur
     */
    Map<String, ValidationRule> compile(InputStream stream) throws IOException;

}
