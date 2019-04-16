package org.companion.impresario;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ValidatorFactory {

    /**
     * Xpath to find all ValidationRule tags
     */
    private final XPathExpression xPathAllValidationRuleTags;

    /**
     * Xpath to find all condition tags
     */
    private final XPathExpression xPathAllConditionTags;

    /**
     * Xpath to find the nested child function of the current tag.
     */
    private final XPathExpression xpathNestedChildFunctionTag;

    /**
     * Xpath to find the group attribute of the current tag;
     */
    private final XPathExpression xPathGroupAttribute;

    private final DefinitionFactory definitionFactory;

    private final FunctionXMLParser functionXMLParser;
    private final FunctionBuilder functionBuilder;

    private final ConditionXMLParser conditionXMLParser;
    private final ConditionBuilder conditionBuilder;


    public ValidatorFactory(MetaData metaData) {
        this.definitionFactory = new DefinitionFactory(metaData);

        this.functionXMLParser = new FunctionXMLParser(metaData);
        this.functionBuilder = new FunctionBuilder(metaData.getMetaFunctions());

        this.conditionXMLParser = new ConditionXMLParser(metaData);
        this.conditionBuilder = new ConditionBuilder(metaData.getMetaConditions());

        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathAllValidationRuleTags = xPathFactory.newXPath().compile("//ValidationRule");
            xPathAllConditionTags = xPathFactory.newXPath().compile("./Condition");
            xpathNestedChildFunctionTag = xPathFactory.newXPath().compile("./Function");
            xPathGroupAttribute = xPathFactory.newXPath().compile("@group");
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public Map<String, ValidationRule> compile(File xmlFile) throws IOException {
        Document document = new ConfigurationXMLParser().parseFrom(xmlFile);
        Map<String, ValidationRule> validatorRuleMap = new HashMap<>();
        try {
            NodeList validationRuleNodes = (NodeList) xPathAllValidationRuleTags.evaluate(document, XPathConstants.NODESET);
            Map<String, List<ValidationRule>> validationRulesMap = new HashMap<>();
            for (int i = 0; i < validationRuleNodes.getLength(); i++) {
                Node eachRule = validationRuleNodes.item(i);
                String group = xPathGroupAttribute.evaluate(eachRule);
                Map<String, Map<String, Object>> definitions = definitionFactory.parseDefinition(eachRule);

                List<Condition> conditions = parseConditions(eachRule);
                ValidationRule validationRule = new DefaultValidationRule(conditions, definitions);

                List<ValidationRule> validationRules = validationRulesMap.remove(group);
                if (validationRules == null) {
                    validationRules = new ArrayList<>();
                }
                validationRules.add(validationRule);
                validationRulesMap.put(group, validationRules);
            }
            for (Map.Entry<String, List<ValidationRule>> entry : validationRulesMap.entrySet()) {
                validatorRuleMap.put(entry.getKey(), new GroupValidationRule(entry.getValue()));
            }
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
        return validatorRuleMap;
    }

    private List<Condition> parseConditions(Node validationRule) throws XPathExpressionException {
        NodeList conditionNodes = (NodeList) xPathAllConditionTags.evaluate(validationRule, XPathConstants.NODESET);
        List<Condition> conditions = new ArrayList<>();
        for (int j = 0; j < conditionNodes.getLength(); j++) {
            Node eachCondition = conditionNodes.item(j);
            Condition condition = parseCondition(eachCondition);
            conditions.add(condition);
        }
        return conditions;
    }

    private Condition parseCondition(Node conditionNode) throws XPathExpressionException {
        ConditionDefinition.Builder conditionDefBuilder = conditionXMLParser.parse(conditionNode);
        NodeList subFunctionNodes = (NodeList) xpathNestedChildFunctionTag.evaluate(conditionNode, XPathConstants.NODESET);
        int functionTagIndex = 0;
        if (!conditionDefBuilder.hasValue1()) {
            Node subFunctionNode = subFunctionNodes.item(functionTagIndex++);
            if (subFunctionNode != null) {
                Function functionValue1 = parseFunction(subFunctionNode);
                conditionDefBuilder.setValue1(functionValue1);
            }
        }

        if (!conditionDefBuilder.hasValue2()) {
            Node subFunctionNode = subFunctionNodes.item(functionTagIndex);
            if (subFunctionNode != null) {
                Function functionValue2 = parseFunction(subFunctionNode);
                conditionDefBuilder.setValue2(functionValue2);
            }
        }
        ConditionDefinition conditionDefinition = conditionDefBuilder.build();
        return conditionBuilder.build(conditionDefinition);
    }

    private Function parseFunction(Node parentNode) throws XPathExpressionException {
        FunctionDefinition.Builder functionDefBuilder = functionXMLParser.parse(parentNode);
        NodeList subFunctionNodes = (NodeList) xpathNestedChildFunctionTag.evaluate(parentNode, XPathConstants.NODESET);
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
        FunctionDefinition functionDefinition = functionDefBuilder.build();
        return functionBuilder.build(functionDefinition);
    }
}
