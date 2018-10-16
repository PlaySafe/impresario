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

public class LabelGeneratorFactory {

    /**
     * Xpath to find all Label tags
     */
    private final XPathExpression xPathAllLabelTags;

    /**
     * Xpath to find the root pre-condition which is allow to execute function
     */
    private final XPathExpression xPathEntranceConditionTag;

    /**
     * Xpath to find the all definition tags.
     */
    private final XPathExpression xPathMultipleDefinitionTag;

    /**
     * Xpath to find the nested child function of the current tag.
     */
    private final XPathExpression xpathNestedChildFunctionTag;

    /**
     * Xpath to find the nested child condition of the current tag.
     */
    private final XPathExpression xPathNestedChildConditionTag;

    /**
     * Xpath to find the group attribute of the current tag;
     */
    private final XPathExpression xPathGroupAttribute;

    private final DefinitionsXMLParser definitionXmlParser;

    private final FunctionXMLParser functionXMLParser;
    private final FunctionBuilder functionBuilder;

    private final ConditionXMLParser conditionXMLParser;
    private final ConditionBuilder conditionBuilder;


    {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathAllLabelTags = xPathFactory.newXPath().compile("//Label");
            xPathEntranceConditionTag = xPathFactory.newXPath().compile("./Condition");
            xPathMultipleDefinitionTag = xPathFactory.newXPath().compile("./Definitions/Definition");
            xpathNestedChildFunctionTag = xPathFactory.newXPath().compile("./Function");
            xPathNestedChildConditionTag = xPathFactory.newXPath().compile("./Condition");
            xPathGroupAttribute = xPathFactory.newXPath().compile("./@group");
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public LabelGeneratorFactory(MetaData metaData) {
        this.definitionXmlParser = new DefinitionsXMLParser(metaData);

        this.functionXMLParser = new FunctionXMLParser(metaData);
        this.functionBuilder = new FunctionBuilder(metaData.getMetaFunctions());

        this.conditionXMLParser = new ConditionXMLParser(metaData);
        this.conditionBuilder = new ConditionBuilder(metaData.getMetaConditions());
    }

    /**
     * @param xmlFile the file instance reference to the xml file
     * @return a map between group and the generator of the particular group
     *
     * @throws IOException if any IO errors occur.
     */
    public Map<String, LabelGenerator> compile(File xmlFile) throws IOException {
        Document document = new ConfigurationXMLParser().parseFrom(xmlFile);
        Map<String, LabelGenerator> labelGeneratorMap = new HashMap<>();
        try {
            NodeList labels = (NodeList) xPathAllLabelTags.evaluate(document, XPathConstants.NODESET);
            Map<String, List<LabelGenerator>> generatorsMap = new HashMap<>();
            for (int i = 0; i < labels.getLength(); i++) {
                Node eachLabel = labels.item(i);
                String group = xPathGroupAttribute.evaluate(eachLabel);
                Condition entranceCondition = parseEntranceCondition(eachLabel);
                Map<String, Map<String, Object>> definitions = parseDefinition(eachLabel);
                Function function = parseEntranceFunction(eachLabel);
                DefaultLabelGenerator labelGenerator = new DefaultLabelGenerator(entranceCondition, function, definitions);

                List<LabelGenerator> generators = generatorsMap.remove(group);
                if (generators == null) {
                    generators = new ArrayList<>();
                }
                generators.add(labelGenerator);
                generatorsMap.put(group, generators);
            }
            for (Map.Entry<String, List<LabelGenerator>> entry : generatorsMap.entrySet()) {
                labelGeneratorMap.put(entry.getKey(), new GroupLabelGenerator(entry.getValue()));
            }
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
        return labelGeneratorMap;
    }

    /**
     * Parses the configuration to the group of definition name, definition key, and value regardless the order
     *
     * @param parentNode the closed parent node of configuration
     * @return a new Map between (definition name -> (definition key -> definition value)) corresponds to the configuration
     *
     * @throws XPathExpressionException
     */
    private Map<String, Map<String, Object>> parseDefinition(Node parentNode) throws XPathExpressionException {
        NodeList definitionNodes = (NodeList) xPathMultipleDefinitionTag.evaluate(parentNode, XPathConstants.NODESET);
        int totalNodes = definitionNodes.getLength();
        Map<String, Map<String, Object>> allDefinitions = new HashMap<>(totalNodes);
        for (int i = 0; i < totalNodes; i++) {
            Map<String, Map<String, Object>> eachDefinition = definitionXmlParser.parse(definitionNodes.item(i));
            allDefinitions.putAll(eachDefinition);
        }
        return allDefinitions;
    }

    private Condition parseEntranceCondition(Node parentNode) throws XPathExpressionException {
        Node entranceCondition = (Node) xPathEntranceConditionTag.evaluate(parentNode, XPathConstants.NODE);
        if (entranceCondition == null) {
            return null;
        }
        return parseCondition(entranceCondition);
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

    private Function parseEntranceFunction(Node parentNode) throws XPathExpressionException {
        Node rootFunction = (Node) xpathNestedChildFunctionTag.evaluate(parentNode, XPathConstants.NODE);
        if (rootFunction == null) {
            throw new IllegalArgumentException("Invalid configuration: There is no executable function");
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
