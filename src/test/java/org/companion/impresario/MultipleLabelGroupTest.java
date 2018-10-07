package org.companion.impresario;

import java.io.File;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class MultipleLabelGroupTest {

    @Test
    public void expectResultOfTheFirstGroupIfNoCondition() throws ConditionNotMatchException {
        File metaResource = new File("src/test/resources/meta_label.xml");
        File configResource = new File("src/test/resources/multiple_labels_same_group.xml");
        MetaData metaData = new MetaLabelFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);

        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        LabelGenerator labelGenerator = labelGenerators.get("LABEL1");

        String result = labelGenerator.labelOf(null);
        Assert.assertEquals("LabelGroup1", result);
    }

    @Test
    public void expectResultOfTheFirstExecutableGroupIfHasCondition() throws ConditionNotMatchException {
        File metaResource = new File("src/test/resources/meta_label.xml");
        File configResource = new File("src/test/resources/multiple_labels_same_group_with_condition.xml");
        MetaData metaData = new MetaLabelFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);

        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        LabelGenerator labelGenerator = labelGenerators.get("LABEL1");

        String result = labelGenerator.labelOf(null);
        Assert.assertEquals("LabelGroup5", result);
    }
}
