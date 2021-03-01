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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleEquationFactory implements EquationFactory {

    private final XPathExpression xPathAllLabelTags;
    private final XPathExpression xPathGroupAttribute;

    private final DefinitionFactory definitionFactory;
    private final FunctionConditionFactory functionConditionFactory;

    public SingleEquationFactory(MetaData metaData) {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathAllLabelTags = xPathFactory.newXPath().compile("/Equations/Equation");
            xPathGroupAttribute = xPathFactory.newXPath().compile("./@group");
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        this.definitionFactory = new DefinitionFactory(metaData);
        this.functionConditionFactory = new FunctionConditionFactory(metaData);
    }

    @Override
    public Map<String, Equation> compile(File file) throws IOException {
        InputStream stream = new FileInputStream(file);
        return compile(stream);
    }

    @Override
    public Map<String, Equation> compile(InputStream stream) throws IOException {
        Document document = new DocumentParser().parseFrom(stream);
        try {
            NodeList labels = (NodeList) xPathAllLabelTags.evaluate(document, XPathConstants.NODESET);
            Map<String, List<Equation>> generatorsMap = new HashMap<>();
            for (int i = 0; i < labels.getLength(); i++) {
                Node eachLabel = labels.item(i);
                String group = xPathGroupAttribute.evaluate(eachLabel);
                Condition entranceCondition = functionConditionFactory.parseEntranceCondition(eachLabel);
                Map<String, Map<String, Object>> definitions = definitionFactory.parseDefinition(eachLabel);
                Function function = functionConditionFactory.parseEntranceFunction(eachLabel);
                DefaultEquation equation = new DefaultEquation(entranceCondition, function, definitions);

                List<Equation> generators = generatorsMap.remove(group);
                if (generators == null) {
                    generators = new ArrayList<>();
                }
                generators.add(equation);
                generatorsMap.put(group, generators);
            }
            Map<String, Equation> equationMap = new HashMap<>();
            for (Map.Entry<String, List<Equation>> entry : generatorsMap.entrySet()) {
                equationMap.put(entry.getKey(), new GroupEquation(entry.getValue()));
            }
            return Collections.unmodifiableMap(new HashMap<>(equationMap));
        }
        catch (XPathExpressionException e) {
            throw new InvalidConfigurationException(e);
        }
    }
}
