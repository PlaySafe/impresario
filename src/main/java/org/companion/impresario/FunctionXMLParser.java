package org.companion.impresario;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Parses the attribute of function XML tag corresponds to the XML configuration spec.
 * This class focus only the tag itself, regardless the child tag, or sibling tag.
 */
class FunctionXMLParser {

    private final String attributeName;
    private final String attributeParam1;
    private final String attributeParam2;

    public FunctionXMLParser(MetaData metaData) {
        this.attributeName = metaData.getAttributeFunctionName();
        this.attributeParam1 = metaData.getAttributeFunctionParameter1();
        this.attributeParam2 = metaData.getAttributeFunctionParameter2();
    }

    /**
     * Parses all attributes of a function node
     *
     * @param functionNode
     * @return a builder with data of the function
     */
    FunctionDefinition.Builder parse(Node functionNode) {
        Element element = ((Element) functionNode);
        String logic = element.getAttribute(attributeName);
        String param1 = element.hasAttribute(attributeParam1) ? element.getAttribute(attributeParam1) : null;
        String param2 = element.hasAttribute(attributeParam2) ? element.getAttribute(attributeParam2) : null;
        return new FunctionDefinition.Builder()
                .setParameter1(param1)
                .setParameter2(param2)
                .setLogic(logic);
    }
}
