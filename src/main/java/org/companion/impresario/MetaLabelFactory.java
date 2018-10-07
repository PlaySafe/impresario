package org.companion.impresario;

import java.io.File;
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

public class MetaLabelFactory {

    private final XPathExpression xPathFunctionName;
    private final XPathExpression xPathFunctionParameter;

    private final XPathExpression xPathDefinitionItemTag;
    private final XPathExpression xPathAttributeDefinitionName;
    private final XPathExpression xPathAttributeDefinitionItemKey;
    private final XPathExpression xPathAttributeDefinitionItemValue;

    private final XPathExpression xPathMetaFunctionTag;
    private final XPathExpression xPathMetaConditionTag;
    private final XPathExpression xPathNameAttribute;
    private final XPathExpression xPathClassAttribute;

    private final XPathExpression xPathConditionName;
    private final XPathExpression xPathConditionParameter1;
    private final XPathExpression xPathConditionParameter2;

    public MetaLabelFactory() {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathFunctionName = xPathFactory.newXPath().compile("/Meta/FunctionAttribute/@name");
            xPathFunctionParameter = xPathFactory.newXPath().compile("/Meta/FunctionAttribute/@parameter");

            xPathDefinitionItemTag = xPathFactory.newXPath().compile("/Meta/Definition/@item-tag");
            xPathAttributeDefinitionName = xPathFactory.newXPath().compile("/Meta/Definition/@name");
            xPathAttributeDefinitionItemKey = xPathFactory.newXPath().compile("/Meta/Definition/@item-key");
            xPathAttributeDefinitionItemValue = xPathFactory.newXPath().compile("/Meta/Definition/@item-value");

            xPathMetaFunctionTag = xPathFactory.newXPath().compile("/Meta//Function");
            xPathMetaConditionTag = xPathFactory.newXPath().compile("/Meta//Condition");
            xPathNameAttribute = xPathFactory.newXPath().compile("./@name");
            xPathClassAttribute = xPathFactory.newXPath().compile("./@class");

            xPathConditionName = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@name");
            xPathConditionParameter1 = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@parameter1");
            xPathConditionParameter2 = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@parameter2");
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param xmlFile the xml file of meta data
     * @return a new MetaData of all defined meta data
     */
    public MetaData compile(File xmlFile) {
        Document document = new ConfigurationXMLParser().parseFrom(xmlFile);
        try {
            NodeList functionNodes = (NodeList) xPathMetaFunctionTag.evaluate(document, XPathConstants.NODESET);
            NodeList conditionNodes = (NodeList) xPathMetaConditionTag.evaluate(document, XPathConstants.NODESET);

            String definitionItemTag = xPathDefinitionItemTag.evaluate(document);
            String attributeDefinitionName = xPathAttributeDefinitionName.evaluate(document);
            String attributeDefinitionItemKey = xPathAttributeDefinitionItemKey.evaluate(document);
            String attributeDefinitionItemValue = xPathAttributeDefinitionItemValue.evaluate(document);

            String attributeFunctionName = xPathFunctionName.evaluate(document);
            String attributeFunctionParameter = xPathFunctionParameter.evaluate(document);

            String attributeConditionName = xPathConditionName.evaluate(document);
            String attributeConditionParameter1 = xPathConditionParameter1.evaluate(document);
            String attributeConditionParameter2 = xPathConditionParameter2.evaluate(document);


            Map<String, String> metaFunctions = parseMeta(functionNodes);
            Map<String, String> metaConditions = parseMeta(conditionNodes);
            return new DefaultMetaData.Builder()
                    .setDefinitionItemTag(definitionItemTag)
                    .setAttributeDefinitionName(attributeDefinitionName)
                    .setAttributeDefinitionItemKey(attributeDefinitionItemKey)
                    .setAttributeDefinitionItemValue(attributeDefinitionItemValue)
                    .setAttributeFunctionName(attributeFunctionName)
                    .setAttributeFunctionParameter(attributeFunctionParameter)
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
