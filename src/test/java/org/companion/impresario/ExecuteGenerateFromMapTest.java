package org.companion.impresario;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExecuteGenerateFromMapTest {

    @Test
    public void canGetValueFromMap() throws ConditionNotMatchException, IOException {
        Map<String, Object> data = new HashMap<String, Object>() {{
            put("data1", "XYZ");
            put("data2", "ABC");
        }};

        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/execute_data_from_map.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);

        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        LabelGenerator labelGenerator = labelGenerators.get("DATA_FROM_MAP");
        Assert.assertEquals("XYZABC", labelGenerator.labelOf(data));
    }
}
