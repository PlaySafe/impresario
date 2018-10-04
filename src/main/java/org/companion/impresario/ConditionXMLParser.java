package org.companion.impresario;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Parses the attribute of condition XML tag corresponds to the XML configuration spec.
 * This class focus only the tag itself, regardless the child tag, or sibling tag.
 */
class ConditionXMLParser {

    private final String attributeName;
    private final String attributeParameter1;
    private final String attributeParameter2;

    public ConditionXMLParser(MetaData metaData) {
        this.attributeName = metaData.getAttributeConditionName();
        this.attributeParameter1 = metaData.getAttributeConditionParameter1();
        this.attributeParameter2 = metaData.getAttributeConditionParameter2();
    }

    /**
     * @param conditionNode the condition XML tag
     * @return a new builder with the data corresponds to the XML configuration
     */
    ConditionDefinition.Builder parse(Node conditionNode) {
        Element element = (Element) conditionNode;
        String logic = element.getAttribute(attributeName);
        String value1 = element.hasAttribute(attributeParameter1) ? element.getAttribute(attributeParameter1) : null;
        String value2 = element.hasAttribute(attributeParameter2) ? element.getAttribute(attributeParameter2) : null;

        Function value1Function = (value1 != null) ? createReturnValueFunction(value1) : null;
        Function value2Function = (value2 != null) ? createReturnValueFunction(value2) : null;

        return new ConditionDefinition.Builder()
                .setLogic(logic)
                .setValue1(value1Function)
                .setValue2(value2Function);
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
