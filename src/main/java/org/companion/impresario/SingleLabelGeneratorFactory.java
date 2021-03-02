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
public class SingleLabelGeneratorFactory implements LabelGeneratorFactory {

    private final XPathExpression xPathAllLabelTags;
    private final XPathExpression xPathGroupAttribute;

    private final DefinitionFactory definitionFactory;
    private final FunctionConditionFactory functionConditionFactory;


    public SingleLabelGeneratorFactory(MetaData metaData) {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathAllLabelTags = xPathFactory.newXPath().compile("/Labels/Label");
            xPathGroupAttribute = xPathFactory.newXPath().compile("./@group");
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        this.definitionFactory = new DefinitionFactory(metaData);
        this.functionConditionFactory = new FunctionConditionFactory(metaData);
    }

    @Override
    public Map<String, LabelGenerator> compile(File file) throws IOException {
        InputStream stream = new FileInputStream(file);
        return compile(stream);
    }

    @Override
    public Map<String, LabelGenerator> compile(InputStream stream) throws IOException {
        Document document = new DocumentParser().parseFrom(stream);
        try {
            NodeList labels = (NodeList) xPathAllLabelTags.evaluate(document, XPathConstants.NODESET);
            Map<String, List<LabelGenerator>> generatorsMap = new HashMap<>(labels.getLength());
            for (int i = 0; i < labels.getLength(); i++) {
                Node eachLabel = labels.item(i);
                String group = xPathGroupAttribute.evaluate(eachLabel);
                Condition entranceCondition = functionConditionFactory.parseEntranceCondition(eachLabel);
                Map<String, Map<String, Object>> definitions = definitionFactory.parseDefinition(eachLabel);
                Function function = functionConditionFactory.parseEntranceFunction(eachLabel);
                DefaultLabelGenerator labelGenerator = new DefaultLabelGenerator(entranceCondition, function, definitions);

                List<LabelGenerator> generators = generatorsMap.remove(group);
                if (generators == null) {
                    generators = new ArrayList<>();
                }
                generators.add(labelGenerator);
                generatorsMap.put(group, generators);
            }
            Map<String, LabelGenerator> labelGeneratorMap = new HashMap<>();
            for (Map.Entry<String, List<LabelGenerator>> entry : generatorsMap.entrySet()) {
                labelGeneratorMap.put(entry.getKey(), new GroupLabelGenerator(entry.getValue()));
            }
            return Collections.unmodifiableMap(new HashMap<>(labelGeneratorMap));
        }
        catch (XPathExpressionException e) {
            throw new InvalidConfigurationException(e);
        }
    }

}
