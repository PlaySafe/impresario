package org.companion.impresario;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Read attribute value of {@code <Function>} using the keyword defined in {@link MetaData}
 * to create {@link FunctionDefinition.Builder}
 * This class focus only the {@code <Function>} itself, regardless the child tag, or sibling tag.
 */
class FunctionXMLParser {

    private final String attributeName;
    private final String attributeParam1;
    private final String attributeParam2;

    FunctionXMLParser(MetaData metaData) {
        this.attributeName = metaData.getAttributeFunctionName();
        this.attributeParam1 = metaData.getAttributeFunctionParameter1();
        this.attributeParam2 = metaData.getAttributeFunctionParameter2();
    }

    /**
     * Creates a new {@link FunctionDefinition.Builder} from the attribute of {@code <Function>}
     *
     * @param functionNode the function XML tag
     * @return a new builder with the data corresponds to the XML configuration
     */
    FunctionDefinition.Builder parse(Node functionNode) {
        Element element = ((Element) functionNode);
        String name = element.getAttribute(attributeName);
        String param1 = element.hasAttribute(attributeParam1) ? element.getAttribute(attributeParam1) : null;
        String param2 = element.hasAttribute(attributeParam2) ? element.getAttribute(attributeParam2) : null;
        return new FunctionDefinition.Builder()
                .setParameter1(param1)
                .setParameter2(param2)
                .setName(name);
    }
}
