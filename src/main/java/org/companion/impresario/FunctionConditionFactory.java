package org.companion.impresario;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A factory used to create a ready-to-use structure of function and condition follow the configuration.
 * An {@link InvalidConfigurationException} might be thrown if provide an invalid configuration.
 * </p>
 */
class FunctionConditionFactory {

    private final XPathExpression xpathNestedChildFunctionTag;
    private final XPathExpression xPathNestedChildConditionTag;
    private final XPathExpression xPathResultAs;

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
            xPathResultAs = xPathFactory.newXPath().compile("./@result-as");
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException(e);
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
        for (int i = 0; i < conditionNodes.getLength(); i++) {
            Node eachCondition = conditionNodes.item(i);
            Condition condition = parseCondition(eachCondition);
            conditions.add(condition);
        }
        return conditions;
    }

    Condition parseCondition(Node conditionNode) throws XPathExpressionException {
        ConditionDefinition.Builder conditionDefBuilder = conditionXMLParser.parse(conditionNode);
        NodeList subFunctionNodes = (NodeList) xpathNestedChildFunctionTag.evaluate(conditionNode, XPathConstants.NODESET);
        Map<String, List<Function>> subFunctions = new HashMap<>(subFunctionNodes.getLength());
        for (int i = 0; i < subFunctionNodes.getLength(); i++) {
            Node subFunctionNode = subFunctionNodes.item(i);
            String resultAs = xPathResultAs.evaluate(subFunctionNode);
            Function subFunction;
            if (subFunctionNode.hasChildNodes()) {
                subFunction = parseFunction(subFunctionNode);
            }
            else {
                FunctionDefinition.Builder subFunctionDefBuilder = functionXMLParser.parse(subFunctionNode);
                FunctionDefinition subFunctionDefinition = subFunctionDefBuilder.build();
                subFunction = functionBuilder.build(subFunctionDefinition);
            }
            List<Function> functions = subFunctions.remove(resultAs);
            if (functions == null) {
                functions = new ArrayList<>();
            }
            functions.add(subFunction);
            subFunctions.put(resultAs, functions);
        }
        conditionDefBuilder.setPreFunctions(subFunctions);

        NodeList subConditionNodes = (NodeList) xPathNestedChildConditionTag.evaluate(conditionNode, XPathConstants.NODESET);
        Map<String, List<Condition>> subConditions = new HashMap<>(subConditionNodes.getLength());
        for (int i = 0; i < subConditionNodes.getLength(); i++) {
            Node subConditionNode = subConditionNodes.item(i);
            if (subConditionNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String resultAs = xPathResultAs.evaluate(subConditionNode);
            Condition subCondition;
            if (subConditionNode.hasChildNodes()) {
                subCondition = parseCondition(subConditionNode);
            }
            else {
                ConditionDefinition.Builder subConditionDefBuilder = conditionXMLParser.parse(subConditionNode);
                ConditionDefinition subConditionDefinition = subConditionDefBuilder.build();
                subCondition = conditionBuilder.build(subConditionDefinition);
            }
            List<Condition> conditions = subConditions.remove(resultAs);
            if (conditions == null) {
                conditions = new ArrayList<>();
            }
            conditions.add(subCondition);
            subConditions.put(resultAs, conditions);
        }
        conditionDefBuilder.setPreConditions(subConditions);
        ConditionDefinition mainConditionDefinition = conditionDefBuilder.build();
        return conditionBuilder.build(mainConditionDefinition);
    }

    Function parseEntranceFunction(Node parentNode) throws XPathExpressionException {
        Node rootFunction = (Node) xpathNestedChildFunctionTag.evaluate(parentNode, XPathConstants.NODE);
        if (rootFunction == null) {
            throw new InvalidConfigurationException("Invalid configuration: No such executable function");
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
        Map<String, List<Function>> subFunctions = new HashMap<>(subFunctionNodes.getLength());
        for (int i = 0; i < subFunctionNodes.getLength(); i++) {
            Node subFunctionNode = subFunctionNodes.item(i);
            if (subFunctionNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String resultAs = xPathResultAs.evaluate(subFunctionNode);
            Function subFunction;
            if (subFunctionNode.hasChildNodes()) {
                subFunction = parseFunction(subFunctionNode);
            }
            else {
                FunctionDefinition.Builder subFunctionDefBuilder = functionXMLParser.parse(subFunctionNode);
                FunctionDefinition subFunctionDefinition = subFunctionDefBuilder.build();
                subFunction = functionBuilder.build(subFunctionDefinition);
            }
            List<Function> functions = subFunctions.remove(resultAs);
            if (functions == null) {
                functions = new ArrayList<>();
            }
            functions.add(subFunction);
            subFunctions.put(resultAs, functions);
        }
        functionDefBuilder.setPreFunction(subFunctions);
        FunctionDefinition mainFunctionDefinition = functionDefBuilder.build();
        return functionBuilder.build(mainFunctionDefinition);
    }
}
