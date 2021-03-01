package org.companion.impresario;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Parses all definition XML tag corresponds to the XML configuration spec.
 * Whenever there is a properties format in the definition, it will be replace by the value of the properties.
 * </p>
 */
class DefinitionsXMLParser {

    private final String tagDefineItem;
    private final String attributeDefinitionName;
    private final String attributeDefinitionItemKey;
    private final String attributeDefinitionItemValue;

    public DefinitionsXMLParser(MetaData metaData) {
        this.tagDefineItem = metaData.getDefinitionTagItem();
        this.attributeDefinitionName = metaData.getAttributeDefinitionName();
        this.attributeDefinitionItemKey = metaData.getAttributeDefinitionItemKey();
        this.attributeDefinitionItemValue = metaData.getAttributeDefinitionItemValue();
    }

    /**
     * Parses attributes of a single node to the group of definition name, definition key, and value
     *
     * @param definitionsNode the definitions XML tag
     *
     * @return a new Map between (definition name -> (definition key -> definition value)) corresponds to the XML configuration
     */
    Map<String, Map<String, Object>> parse(Node definitionsNode) {
        Map<String, Map<String, Object>> defineNameAndValue = new HashMap<>(1);
        String definitionName = definitionsNode.getAttributes().getNamedItem(attributeDefinitionName).getTextContent();
        Map<String, Object> items = parseItem(definitionsNode);
        defineNameAndValue.put(definitionName, items);
        return defineNameAndValue;
    }

    /**
     * Parses all attributes of all <b><i>Item</i></b> tag under the definition node.
     *
     * @param definitionsNode
     *
     * @return a new map between key and value attribute of all items
     */
    private Map<String, Object> parseItem(Node definitionsNode) {
        NodeList itemTags = ((Element) definitionsNode).getElementsByTagName(tagDefineItem);
        Map<String, Object> items = new LinkedHashMap<>(itemTags.getLength());
        for (int i = 0; i < itemTags.getLength(); i++) {
            Node eachItem = itemTags.item(i);
            NamedNodeMap attributes = eachItem.getAttributes();
            String key = attributes.getNamedItem(attributeDefinitionItemKey).getTextContent();
            String value = attributes.getNamedItem(attributeDefinitionItemValue).getTextContent();
            items.put(getValueOf(key), getValueOf(value));
        }
        return items;
    }

    /**
     * @param text the configuration text
     *
     * @return the value of properties if text's format refers to properties, otherwise the text itself
     */
    private String getValueOf(String text) {
        if (VariableReflector.isProperties(text)) {
            String propertiesKey = VariableReflector.reflectPropertiesOf(text);
            return System.getProperty(propertiesKey);
        }
        return text;
    }
}
