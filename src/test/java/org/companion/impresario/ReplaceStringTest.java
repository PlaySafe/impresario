package org.companion.impresario;

import java.io.File;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class ReplaceStringTest {

    private Map<String, LabelGenerator> labelGenerators;

    public ReplaceStringTest() {
        File metaResource = new File("src/test/resources/meta_label.xml");
        File configResource = new File("src/test/resources/replace_string.xml");
        MetaData metaData = new MetaLabelFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        Assert.assertNotNull(labelGenerators);

        this.labelGenerators = labelGenerators;
    }

    @Test
    public void expectReplaceStringBySpecificKey() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("BY_SPECIFIC_KEY");

        String result = labelGenerator.labelOf(null);
        Assert.assertEquals("Hello John!", result);
    }
}
