package org.companion.impresario;

import org.w3c.dom.Node;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Read attribute value of {@code <Condition>} using the keyword defined in {@link MetaData}
 * to create {@link ConditionDefinition.Builder}
 * </p>
 *
 * <p>
 * This class focus only the {@code <Condition>} itself, regardless the child tag, or sibling tag.
 * </p>
 */
class ConditionXMLParser {

    private final String attributeName;
    private final Map<String, Map<Integer, String>> metaConditionParameters;

    ConditionXMLParser(MetaData metaData) {
        this.attributeName = metaData.getAttributeConditionName();
        this.metaConditionParameters = metaData.getMetaConditionParameters();
    }

    /**
     * Creates a new {@link ConditionDefinition.Builder} from the attribute of {@code <Condition>}
     *
     * @param conditionNode the condition XML tag
     *
     * @return a new builder with the data corresponds to the XML configuration
     */
    ConditionDefinition.Builder parse(Node conditionNode) {
        Map<String, String> attributes = Util.allAttributes(conditionNode);
        String name = attributes.remove(attributeName);

        Map<String, String> parameters = (attributes.isEmpty())
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(attributes));

        return new ConditionDefinition.Builder()
                .setName(name)
                .setParameters(parameters)
                .setMetaParameters(metaConditionParameters.get(name));
    }
}
