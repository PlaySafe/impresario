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
     * Xpath to find the group attribute of the current tag;
     */
    private final XPathExpression xPathGroupAttribute;

    private final DefinitionFactory definitionFactory;

    private final FunctionConditionFactory functionConditionFactory;


    public ValidatorFactory(MetaData metaData) {
        this.definitionFactory = new DefinitionFactory(metaData);

        this.functionConditionFactory = new FunctionConditionFactory(metaData);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathAllValidationRuleTags = xPathFactory.newXPath().compile("//ValidationRule");
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

                List<Condition> conditions = functionConditionFactory.parseConditions(eachRule);
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


}
