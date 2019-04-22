package org.companion.impresario;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Read attribute value of {@code <Condition>} using the keyword defined in {@link MetaData}
 * to create {@link ConditionDefinition.Builder}
 * This class focus only the {@code <Condition>} itself, regardless the child tag, or sibling tag.
 */
class ConditionXMLParser {

    private final String attributeName;
    private final String attributeParameter1;
    private final String attributeParameter2;

    ConditionXMLParser(MetaData metaData) {
        this.attributeName = metaData.getAttributeConditionName();
        this.attributeParameter1 = metaData.getAttributeConditionParameter1();
        this.attributeParameter2 = metaData.getAttributeConditionParameter2();
    }

    /**
     * Creates a new {@link ConditionDefinition.Builder} from the attribute of {@code <Condition>}
     *
     * @param conditionNode the condition XML tag
     * @return a new builder with the data corresponds to the XML configuration
     */
    ConditionDefinition.Builder parse(Node conditionNode) {
        Element element = (Element) conditionNode;
        String name = element.getAttribute(attributeName);
        String parameter1 = element.hasAttribute(attributeParameter1) ? element.getAttribute(attributeParameter1) : null;
        String parameter2 = element.hasAttribute(attributeParameter2) ? element.getAttribute(attributeParameter2) : null;

        Function parameter1Function = (parameter1 != null) ? createReturnValueFunction(parameter1) : null;
        Function parameter2Function = (parameter2 != null) ? createReturnValueFunction(parameter2) : null;

        return new ConditionDefinition.Builder()
                .setName(name)
                .setParameter1(parameter1Function)
                .setParameter2(parameter2Function);
    }

    private Function createReturnValueFunction(final String value) {
        return new Function() {
            @Override
            public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
                return value;
            }
        };
    }
}
