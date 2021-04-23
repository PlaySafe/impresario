package org.companion.impresario;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class BigDecimalEqualsTest {

    private final Map<String, LabelGenerator> labelGenerators;

    public BigDecimalEqualsTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/big_decimal_equality.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        Assert.assertNotNull(labelGenerators);

        this.labelGenerators = labelGenerators;
    }

    @Test
    public void expectEqualBigDecimalWhenSameValueButDifferentPrecision() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("BIG_DECIMAL_EQUALS");
        Assert.assertNotNull(labelGenerator);
        Assert.assertEquals("EQUAL", labelGenerator.labelOf(null));
    }
}
