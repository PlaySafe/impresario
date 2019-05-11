package org.companion.impresario;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class FunctionConditionFactory {

    private final XPathExpression xpathNestedChildFunctionTag;
    private final XPathExpression xPathNestedChildConditionTag;

    private final FunctionXMLParser functionXMLParser;
    private final FunctionBuilder functionBuilder;

    private final ConditionXMLParser conditionXMLParser;
    private final ConditionBuilder conditionBuilder;

    FunctionConditionFactory(MetaData metaData) {
        this.functionXMLParser = new FunctionXMLParser(metaData);
        this.functionBuilder = new FunctionBuilder(metaData.getMetaFunctions());

        this.conditionXMLParser = new ConditionXMLParser(metaData);
        this.conditionBuilder = new ConditionBuilder(metaData.getMetaConditions());

        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xpathNestedChildFunctionTag = xPathFactory.newXPath().compile("./Function");
            xPathNestedChildConditionTag = xPathFactory.newXPath().compile("./Condition");
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    Condition parseEntranceCondition(Node parentNode) throws XPathExpressionException {
        Node entranceCondition = (Node) xPathNestedChildConditionTag.evaluate(parentNode, XPathConstants.NODE);
        if (entranceCondition == null) {
            return null;
        }
        return parseCondition(entranceCondition);
    }

    List<Condition> parseConditions(Node conditionNode) throws XPathExpressionException {
        NodeList conditionNodes = (NodeList) xPathNestedChildConditionTag.evaluate(conditionNode, XPathConstants.NODESET);
        List<Condition> conditions = new ArrayList<>();
        for (int j = 0; j < conditionNodes.getLength(); j++) {
            Node eachCondition = conditionNodes.item(j);
            Condition condition = parseCondition(eachCondition);
            conditions.add(condition);
        }
        return conditions;
    }

    Condition parseCondition(Node conditionNode) throws XPathExpressionException {
        ConditionDefinition.Builder conditionDefBuilder = conditionXMLParser.parse(conditionNode);
        NodeList subFunctionNodes = (NodeList) xpathNestedChildFunctionTag.evaluate(conditionNode, XPathConstants.NODESET);
        int functionTagIndex = 0;
        if (!conditionDefBuilder.hasParameter1()) {
            Node subFunctionNode = subFunctionNodes.item(functionTagIndex++);
            if (subFunctionNode != null) {
                Function functionValue1 = parseFunction(subFunctionNode);
                conditionDefBuilder.setParameter1(functionValue1);
            }
        }

        if (!conditionDefBuilder.hasParameter2()) {
            Node subFunctionNode = subFunctionNodes.item(functionTagIndex);
            if (subFunctionNode != null) {
                Function functionValue2 = parseFunction(subFunctionNode);
                conditionDefBuilder.setParameter2(functionValue2);
            }
        }

        NodeList subConditionNodes = (NodeList) xPathNestedChildConditionTag.evaluate(conditionNode, XPathConstants.NODESET);
        for (int i = 0; i < subConditionNodes.getLength(); i++) {
            Node subConditionNode = subConditionNodes.item(i);
            Condition subCondition;
            if (subConditionNode.hasChildNodes()) {
                subCondition = parseCondition(subConditionNode);
            }
            else {
                ConditionDefinition.Builder subConditionDefBuilder = conditionXMLParser.parse(subConditionNode);
                ConditionDefinition subConditionDefinition = subConditionDefBuilder.build();
                subCondition = conditionBuilder.build(subConditionDefinition);
            }
            conditionDefBuilder.addPreCondition(subCondition);
        }
        ConditionDefinition mainConditionDefinition = conditionDefBuilder.build();
        return conditionBuilder.build(mainConditionDefinition);
    }

    Function parseEntranceFunction(Node parentNode) throws XPathExpressionException {
        Node rootFunction = (Node) xpathNestedChildFunctionTag.evaluate(parentNode, XPathConstants.NODE);
        if (rootFunction == null) {
            throw new IllegalArgumentException("Invalid configuration: No such executable function");
        }
        return parseFunction(rootFunction);
    }

    private Function parseFunction(Node functionNode) throws XPathExpressionException {
        FunctionDefinition.Builder functionDefBuilder = functionXMLParser.parse(functionNode);
        Node subConditionNode = (Node) xPathNestedChildConditionTag.evaluate(functionNode, XPathConstants.NODE);
        if (subConditionNode != null && subConditionNode.getNodeType() == Node.ELEMENT_NODE) {
            Condition subCondition = parseCondition(subConditionNode);
            functionDefBuilder.setPreCondition(subCondition);
        }

        NodeList subFunctionNodes = (NodeList) xpathNestedChildFunctionTag.evaluate(functionNode, XPathConstants.NODESET);
        for (int i = 0; i < subFunctionNodes.getLength(); i++) {
            Node subFunctionNode = subFunctionNodes.item(i);
            if (subFunctionNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Function subFunction;
            if (subFunctionNode.hasChildNodes()) {
                subFunction = parseFunction(subFunctionNode);
            }
            else {
                FunctionDefinition.Builder subFunctionDefBuilder = functionXMLParser.parse(subFunctionNode);
                FunctionDefinition subFunctionDefinition = subFunctionDefBuilder.build();
                subFunction = functionBuilder.build(subFunctionDefinition);
            }
            functionDefBuilder.addPreFunction(subFunction);
        }
        FunctionDefinition mainFunctionDefinition = functionDefBuilder.build();
        return functionBuilder.build(mainFunctionDefinition);
    }
}
