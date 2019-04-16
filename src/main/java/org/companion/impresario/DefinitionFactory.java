package org.companion.impresario;

import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class DefinitionFactory {

    /**
     * Xpath to find the all definition tags.
     */
    private final XPathExpression xPathMultipleDefinitionTag;

    private final DefinitionsXMLParser definitionXmlParser;


    public DefinitionFactory(MetaData metaData) {
        this.definitionXmlParser = new DefinitionsXMLParser(metaData);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathMultipleDefinitionTag = xPathFactory.newXPath().compile("./Definitions/Definition");
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Parses the configuration to the group of definition name, definition key, and value regardless the order
     *
     * @param parentNode the closed parent node of configuration
     * @return a new Map between (definition name -> (definition key -> definition value)) corresponds to the configuration
     *
     * @throws XPathExpressionException
     */
    Map<String, Map<String, Object>> parseDefinition(Node parentNode) throws XPathExpressionException {
        NodeList definitionNodes = (NodeList) xPathMultipleDefinitionTag.evaluate(parentNode, XPathConstants.NODESET);
        int totalNodes = definitionNodes.getLength();
        Map<String, Map<String, Object>> allDefinitions = new HashMap<>(totalNodes);
        for (int i = 0; i < totalNodes; i++) {
            Map<String, Map<String, Object>> eachDefinition = definitionXmlParser.parse(definitionNodes.item(i));
            allDefinitions.putAll(eachDefinition);
        }
        return allDefinitions;
    }
}
