package org.companion.impresario;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    private final XPathExpression xPathAllLabelTags;
    private final XPathExpression xPathGroupAttribute;

    private final DefinitionFactory definitionFactory;

    private final FunctionConditionFactory functionConditionFactory;


    public LabelGeneratorFactory(MetaData metaData) {
        this.definitionFactory = new DefinitionFactory(metaData);

        this.functionConditionFactory = new FunctionConditionFactory(metaData);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathAllLabelTags = xPathFactory.newXPath().compile("//Label");
            xPathGroupAttribute = xPathFactory.newXPath().compile("./@group");
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param xmlFile the xml configuration
     * @return a map between group and the generator of the particular group
     *
     * @throws IOException if any IO errors occur.
     */
    public Map<String, LabelGenerator> compile(File xmlFile) throws IOException {
        InputStream xmlStream = new FileInputStream(xmlFile);
        return compile(xmlStream);
    }

    /**
     * @param xmlStream the xml stream configuration
     * @return a map between group and the generator of the particular group
     *
     * @throws IOException if any IO errors occur.
     */
    public Map<String, LabelGenerator> compile(InputStream xmlStream) throws IOException {
        Document document = new ConfigurationXMLParser().parseFrom(xmlStream);
        try {
            NodeList labels = (NodeList) xPathAllLabelTags.evaluate(document, XPathConstants.NODESET);
            Map<String, List<LabelGenerator>> generatorsMap = new HashMap<>();
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
            throw new IllegalArgumentException(e);
        }
    }


}
