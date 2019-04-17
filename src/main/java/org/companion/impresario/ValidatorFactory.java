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

/**
 * <p>
 * The compilation of configuration requires both {@link MetaData} as the references, and
 * XML configuration file as a configuration that correspond to the references
 * </p>
 *
 * <p>
 * The compilation will read configuration using keyword and/or references from meta data,
 * then create a new ready-to-use data structure correspond to the configuration
 * </p>
 */
public class ValidatorFactory {

    private final XPathExpression xPathAllValidationRuleTags;

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


    /**
     * Compiles all configurations to the ready-to-use data structure.
     * The result will be a map between validation group and executable
     * validation rule that correspond to the configuration
     *
     * @param xmlFile the validation rule XML configuration file
     * @return a map between group and executable validation rule
     *
     * @throws IOException if any problem about IO occur
     */
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
