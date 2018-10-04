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

    private final XPathFactory xPathFactory;
    private final XPathExpression xPathMetaFunctionTag;
    private final XPathExpression xPathMetaConditionTag;
    private final XPathExpression xPathNameAttribute;
    private final XPathExpression xPathClassAttribute;
    private final XPathExpression xPathConditionName;
    private final XPathExpression xPathConditionParameter1;
    private final XPathExpression xPathConditionParameter2;
    private final XPathExpression xPathFunctionName;
    private final XPathExpression xPathFunctionTarget;
    private final XPathExpression xPathFunctionParameter;

    public MetaLabelFactory() {
        xPathFactory = XPathFactory.newInstance();
        try {
            xPathMetaFunctionTag = xPathFactory.newXPath().compile("/Meta//Function");
            xPathMetaConditionTag = xPathFactory.newXPath().compile("/Meta//Condition");
            xPathNameAttribute = xPathFactory.newXPath().compile("./@name");
            xPathClassAttribute = xPathFactory.newXPath().compile("./@class");
            xPathConditionName = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@name");
            xPathConditionParameter1 = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@parameter1");
            xPathConditionParameter2 = xPathFactory.newXPath().compile("/Meta/ConditionAttribute/@parameter2");
            xPathFunctionName = xPathFactory.newXPath().compile("/Meta/FunctionAttribute/@name");
            xPathFunctionTarget = xPathFactory.newXPath().compile("/Meta/FunctionAttribute/@target");
            xPathFunctionParameter = xPathFactory.newXPath().compile("/Meta/FunctionAttribute/@parameter");
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
            String attributeConditionName = xPathConditionName.evaluate(document);
            String attributeConditionParameter1 = xPathConditionParameter1.evaluate(document);
            String attributeConditionParameter2 = xPathConditionParameter2.evaluate(document);
            String attributeFunctionName = xPathFunctionName.evaluate(document);
            String attributeFunctionTarget = xPathFunctionTarget.evaluate(document);
            String attributeFunctionParameter = xPathFunctionParameter.evaluate(document);

            Map<String, String> metaFunctions = parseMeta(functionNodes);
            Map<String, String> metaConditions = parseMeta(conditionNodes);
            return new DefaultMetaData.Builder()
                    .setAttributeConditionName(attributeConditionName)
                    .setAttributeConditionParameter1(attributeConditionParameter1)
                    .setAttributeConditionParameter2(attributeConditionParameter2)
                    .setAttributeFunctionName(attributeFunctionName)
                    .setAttributeFunctionTarget(attributeFunctionTarget)
                    .setAttributeFunctionParameter(attributeFunctionParameter)
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
