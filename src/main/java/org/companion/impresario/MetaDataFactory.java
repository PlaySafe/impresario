package org.companion.impresario;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Reads meta data from XML configuration and crete a new {@link MetaData} to be used as references.
 * The meta data controls the syntax of configuration, the available implementation allow to execute,
 * and also the meaning of the function and condition name.
 */
public class MetaDataFactory {

    private final XPathExpression xPathFunctionAttributeName;
    private final XPathExpression xPathFunctionAttributeParameter1;
    private final XPathExpression xPathFunctionAttributeParameter2;

    private final XPathExpression xPathDefinitionItemTag;
    private final XPathExpression xPathAttributeDefinitionName;
    private final XPathExpression xPathAttributeDefinitionItemKey;
    private final XPathExpression xPathAttributeDefinitionItemValue;

    private final XPathExpression xPathMetaFunctionTag;
    private final XPathExpression xPathMetaConditionTag;
    private final XPathExpression xPathNameAttribute;
    private final XPathExpression xPathClassAttribute;

    private final XPathExpression xPathConditionAttributeName;
    private final XPathExpression xPathConditionAttributeParameter1;
    private final XPathExpression xPathConditionAttributeParameter2;

    public MetaDataFactory() {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathFunctionAttributeName = xPathFactory.newXPath().compile("/Meta/FunctionAttribute/@reference-to-name");
            xPathFunctionAttributeParameter1 = xPathFactory.newXPath().compile("/Meta/FunctionAttribute/@reference-to-parameter1");
            xPathFunctionAttributeParameter2 = xPathFactory.newXPath().compile("/Meta/FunctionAttribute/@reference-to-parameter2");

            xPathDefinitionItemTag = xPathFactory.newXPath().compile("/Meta/Definition/@reference-item-tag");
            xPathAttributeDefinitionName = xPathFactory.newXPath().compile("/Meta/Definition/@reference-to-name");
            xPathAttributeDefinitionItemKey = xPathFactory.newXPath().compile("/Meta/Definition/@reference-item-key");
            xPathAttributeDefinitionItemValue = xPathFactory.newXPath().compile("/Meta/Definition/@reference-item-value");

            xPathMetaFunctionTag = xPathFactory.newXPath().compile("/Meta//Function");
            xPathMetaConditionTag = xPathFactory.newXPath().compile("/Meta//Condition");
            xPathNameAttribute = xPathFactory.newXPath().compile("./@name");
            xPathClassAttribute = xPathFactory.newXPath().compile("./@class");

            xPathConditionAttributeName = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@reference-to-name");
            xPathConditionAttributeParameter1 = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@reference-to-parameter1");
            xPathConditionAttributeParameter2 = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@reference-to-parameter2");
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Creates a new {@link MetaData} from XML configuration
     *
     * @param xmlFile the xml file of meta data
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
     * @return a new MetaData of all defined meta data
     *
     * @throws IOException if any IO errors occur.
     */
    public MetaData compile(InputStream xmlStream) throws IOException {
        Document document = new ConfigurationXMLParser().parseFrom(xmlStream);
        try {
            NodeList functionNodes = (NodeList) xPathMetaFunctionTag.evaluate(document, XPathConstants.NODESET);
            NodeList conditionNodes = (NodeList) xPathMetaConditionTag.evaluate(document, XPathConstants.NODESET);

            String definitionItemTag = xPathDefinitionItemTag.evaluate(document);
            String attributeDefinitionName = xPathAttributeDefinitionName.evaluate(document);
            String attributeDefinitionItemKey = xPathAttributeDefinitionItemKey.evaluate(document);
            String attributeDefinitionItemValue = xPathAttributeDefinitionItemValue.evaluate(document);

            String attributeFunctionName = xPathFunctionAttributeName.evaluate(document);
            String attributeFunctionParameter1 = xPathFunctionAttributeParameter1.evaluate(document);
            String attributeFunctionParameter2 = xPathFunctionAttributeParameter2.evaluate(document);

            String attributeConditionName = xPathConditionAttributeName.evaluate(document);
            String attributeConditionParameter1 = xPathConditionAttributeParameter1.evaluate(document);
            String attributeConditionParameter2 = xPathConditionAttributeParameter2.evaluate(document);


            Map<String, String> metaFunctions = parseMeta(functionNodes);
            Map<String, String> metaConditions = parseMeta(conditionNodes);
            return new DefaultMetaData.Builder()
                    .setDefinitionItemTag(definitionItemTag)
                    .setAttributeDefinitionName(attributeDefinitionName)
                    .setAttributeDefinitionItemKey(attributeDefinitionItemKey)
                    .setAttributeDefinitionItemValue(attributeDefinitionItemValue)
                    .setAttributeFunctionName(attributeFunctionName)
                    .setAttributeFunctionParameter1(attributeFunctionParameter1)
                    .setAttributeFunctionParameter2(attributeFunctionParameter2)
                    .setAttributeConditionName(attributeConditionName)
                    .setAttributeConditionParameter1(attributeConditionParameter1)
                    .setAttributeConditionParameter2(attributeConditionParameter2)
                    .setMetaFunction(metaFunctions)
                    .setMetaCondition(metaConditions)
                    .build();
        }
        catch (XPathExpressionException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * Parses the meta data between <b><i>name</i></b> and <b><i>implementation class</i></b>
     *
     * @param metaNodes the target node to parse meta data
     * @return a new map between name and implementation class
     *
     * @throws XPathExpressionException
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

}
