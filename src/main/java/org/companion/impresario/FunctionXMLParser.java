package org.companion.impresario;

import org.w3c.dom.Node;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Read attribute value of {@code <Function>} using the keyword defined in {@link MetaData}
 * to create {@link FunctionDefinition.Builder}
 * </p>
 *
 * <p>
 * This class focus only the {@code <Function>} itself, regardless the child tag, or sibling tag.
 * </p>
 */
class FunctionXMLParser {

    private final String attributeName;
    private final Map<String, Map<Integer, String>> metaFunctionParameters;

    FunctionXMLParser(MetaData metaData) {
        this.attributeName = metaData.getAttributeFunctionName();
        this.metaFunctionParameters = metaData.getMetaFunctionParameters();
    }

    /**
     * Creates a new {@link FunctionDefinition.Builder} from the attribute of {@code <Function>}
     *
     * @param functionNode the function XML tag
     *
     * @return a new builder with the data corresponds to the XML configuration
     */
    FunctionDefinition.Builder parse(Node functionNode) {
        Map<String, String> attributes = Util.allAttributes(functionNode);
        String name = attributes.remove(attributeName);

        Map<String, String> parameters = (attributes.isEmpty())
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(attributes));

        return new FunctionDefinition.Builder()
                .setName(name)
                .setParameters(parameters)
                .setMetaParameters(metaFunctionParameters.getOrDefault(name, Collections.emptyMap()));
    }

}
