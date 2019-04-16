package org.companion.impresario;

import data.Address;
import data.DefaultAddress;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class ExecuteLabelGeneratorErrorTest {

    private LabelGenerator labelGenerator;

    public ExecuteLabelGeneratorErrorTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/execute_error.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        Assert.assertNotNull(labelGenerators);

        LabelGenerator labelGenerator = labelGenerators.get("ERROR");
        Assert.assertNotNull(labelGenerator);
        this.labelGenerator = labelGenerator;
    }

    @Test(expected = ConditionNotMatchException.class)
    public void caseNoConditionMatches() throws ConditionNotMatchException {
        Address address = new DefaultAddress.Builder()
                .setCity(null)
                .setPostalCode(null)
                .setState(null)
                .setStreet(null)
                .setCountry(null)
                .build();

        labelGenerator.labelOf(address);
    }
}
