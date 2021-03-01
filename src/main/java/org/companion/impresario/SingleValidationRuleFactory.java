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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class SingleValidationRuleFactory implements ValidationRuleFactory {

    private final XPathExpression xPathAllValidationRuleTags;
    private final XPathExpression xPathGroupAttribute;

    private final DefinitionFactory definitionFactory;
    private final FunctionConditionFactory functionConditionFactory;


    public SingleValidationRuleFactory(MetaData metaData) {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathAllValidationRuleTags = xPathFactory.newXPath().compile("/ValidationRules/ValidationRule");
            xPathGroupAttribute = xPathFactory.newXPath().compile("@group");
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        this.definitionFactory = new DefinitionFactory(metaData);
        this.functionConditionFactory = new FunctionConditionFactory(metaData);
    }


    @Override
    public Map<String, ValidationRule> compile(File file) throws IOException {
        InputStream stream = new FileInputStream(file);
        return compile(stream);
    }

    @Override
    public Map<String, ValidationRule> compile(InputStream stream) throws IOException {
        Document document = new DocumentParser().parseFrom(stream);
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
            throw new InvalidConfigurationException(e);
        }
        return validatorRuleMap;
    }

}
