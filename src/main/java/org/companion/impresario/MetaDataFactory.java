package org.companion.impresario;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Reads meta data from XML configuration and crete a new {@link MetaData} to be used as references.
 * The meta data controls the syntax of configuration, the available implementation allow to execute,
 * and also the meaning of the function and condition name.
 * </p>
 */
public class MetaDataFactory {

    private final XPathExpression xPathFunctionAttributeName;

    private final XPathExpression xPathDefinitionItemTag;
    private final XPathExpression xPathAttributeDefinitionName;
    private final XPathExpression xPathAttributeDefinitionItemKey;
    private final XPathExpression xPathAttributeDefinitionItemValue;

    private final XPathExpression xPathMetaFunctionTag;
    private final XPathExpression xPathMetaConditionTag;
    private final XPathExpression xPathNameAttribute;
    private final XPathExpression xPathClassAttribute;
    private final XPathExpression xpathParameter;

    private final XPathExpression xPathConditionAttributeName;

    public MetaDataFactory() {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathFunctionAttributeName = xPathFactory.newXPath().compile("/Meta/Functions/@reference-to-name");

            xPathDefinitionItemTag = xPathFactory.newXPath().compile("/Meta/Definition/@reference-item-tag");
            xPathAttributeDefinitionName = xPathFactory.newXPath().compile("/Meta/Definition/@reference-to-name");
            xPathAttributeDefinitionItemKey = xPathFactory.newXPath().compile("/Meta/Definition/@reference-item-key");
            xPathAttributeDefinitionItemValue = xPathFactory.newXPath().compile("/Meta/Definition/@reference-item-value");

            xPathMetaFunctionTag = xPathFactory.newXPath().compile("/Meta//Function");
            xPathMetaConditionTag = xPathFactory.newXPath().compile("/Meta//Condition");
            xPathNameAttribute = xPathFactory.newXPath().compile("./@name");
            xPathClassAttribute = xPathFactory.newXPath().compile("./@class");
            xpathParameter = xPathFactory.newXPath().compile("./Parameter/@name");

            xPathConditionAttributeName = xPathFactory.newXPath().compile("/Meta/Conditions/@reference-to-name");
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new {@link MetaData} from XML configuration
     *
     * @param xmlFile the xml file of meta data
     *
     * @return a new MetaData of all defined meta data
     *
     * @throws IOException if any IO errors occur.
     */
    public MetaData compile(File xmlFile) throws IOException {
        InputStream xmlStream = new FileInputStream(xmlFile);
        return compile(xmlStream);
    }

    /**
     * Creates a new {@link MetaData} from XML configuration
     *
     * @param xmlStream an input stream of meta data xml
     *
     * @return a new MetaData of all defined meta data
     *
     * @throws IOException if any IO errors occur.
     */
    public MetaData compile(InputStream xmlStream) throws IOException {
        Document document = new DocumentParser().parseFrom(xmlStream);
        try {
            NodeList functionNodes = (NodeList) xPathMetaFunctionTag.evaluate(document, XPathConstants.NODESET);
            NodeList conditionNodes = (NodeList) xPathMetaConditionTag.evaluate(document, XPathConstants.NODESET);

            String definitionItemTag = xPathDefinitionItemTag.evaluate(document);
            String attributeDefinitionName = xPathAttributeDefinitionName.evaluate(document);
            String attributeDefinitionItemKey = xPathAttributeDefinitionItemKey.evaluate(document);
            String attributeDefinitionItemValue = xPathAttributeDefinitionItemValue.evaluate(document);

            String attributeFunctionName = xPathFunctionAttributeName.evaluate(document);
            String attributeConditionName = xPathConditionAttributeName.evaluate(document);

            Map<String, String> metaFunctions = parseMeta(functionNodes);
            Map<String, Map<Integer, String>> metaFunctionParameters = parseParameters(functionNodes);
            Map<String, String> metaConditions = parseMeta(conditionNodes);
            Map<String, Map<Integer, String>> metaConditionParameters = parseParameters(conditionNodes);
            return new DefaultMetaData.Builder()
                    .setDefinitionItemTag(definitionItemTag)
                    .setAttributeDefinitionName(attributeDefinitionName)
                    .setAttributeDefinitionItemKey(attributeDefinitionItemKey)
                    .setAttributeDefinitionItemValue(attributeDefinitionItemValue)
                    .setAttributeFunctionName(attributeFunctionName)
                    .setAttributeConditionName(attributeConditionName)
                    .setMetaFunction(metaFunctions)
                    .setMetaFunctionParameters(metaFunctionParameters)
                    .setMetaCondition(metaConditions)
                    .setMetaConditionParameters(metaConditionParameters)
                    .build();
        }
        catch (XPathExpressionException | ClassNotFoundException e) {
            throw new InvalidConfigurationException(e);
        }
    }


    /**
     * Parses the meta data between <b><i>name</i></b> and <b><i>implementation class</i></b>
     *
     * @param metaNodes the target node to parse meta data
     *
     * @return a new map between name and implementation class
     *
     * @throws XPathExpressionException if meta data misconfiguration occurs
     */
    private Map<String, String> parseMeta(NodeList metaNodes) throws XPathExpressionException {
        int totalNodes = metaNodes.getLength();
        Map<String, String> metaMap = new HashMap<>(totalNodes);
        for (int i = 0; i < totalNodes; i++) {
            Node eachMeta = metaNodes.item(i);
            String name = xPathNameAttribute.evaluate(eachMeta);
            String className = xPathClassAttribute.evaluate(eachMeta);
            metaMap.put(name, className);
        }
        return Collections.unmodifiableMap(metaMap);
    }

    /**
     * Parses the meta data between name and its parameter's name in order.
     *
     * @param metaNodes the target node to parse meta data
     *
     * @return a new map between name and its parameter's name in order.
     *
     * @throws XPathExpressionException if meta data misconfiguration occurs
     */
    private Map<String, Map<Integer, String>> parseParameters(NodeList metaNodes) throws XPathExpressionException {
        int totalNodes = metaNodes.getLength();
        Map<String, Map<Integer, String>> parametersMap = new HashMap<>(totalNodes);
        for (int i = 0; i < totalNodes; i++) {
            Node eachMeta = metaNodes.item(i);
            String name = xPathNameAttribute.evaluate(eachMeta);
            NodeList parameterNodes = (NodeList) xpathParameter.evaluate(eachMeta, XPathConstants.NODESET);
            Map<Integer, String> parameterNames = new LinkedHashMap<>();
            for (int x = 0; x < parameterNodes.getLength(); x++) {
                Node node = parameterNodes.item(x);
                String parameterName = node.getNodeValue();
                parameterNames.put(x, parameterName);
            }
            parametersMap.put(name, Collections.unmodifiableMap(parameterNames));
        }
        return Collections.unmodifiableMap(parametersMap);
    }

}
