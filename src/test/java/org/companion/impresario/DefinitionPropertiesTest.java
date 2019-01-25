package org.companion.impresario;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class DefinitionPropertiesTest {

    @Test
    public void expectDefinitionReplacePropertiesValue() throws ConditionNotMatchException, IOException {
        String propertiesKey = "definition.properties";
        String propertiesValue = "DEF_PROPS";
        System.setProperty(propertiesKey, propertiesValue);

        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/definition_properties.xml");
        MetaData metaData = new MetaLabelFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);

        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        LabelGenerator labelGenerator = labelGenerators.get("DEFINITION");

        String result = labelGenerator.labelOf(null);
        Assert.assertEquals(propertiesValue, result);
        System.clearProperty(propertiesKey);
    }
}
