package org.companion.impresario;

import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AmbiguousParameterTest {

    private final LabelGeneratorFactory labelGeneratorFactory;
    private final Map<String, String> ambiguousConfigPaths = new HashMap<String, String>() {{
        put("src/test/resources/ambiguous_parameter_condition.xml", "Expect duplicate condition attribute and result-as");
        put("src/test/resources/ambiguous_parameter_function.xml", "Expect duplicate function attribute and result-as");
    }};

    public AmbiguousParameterTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);
        Assert.assertNotNull(labelGeneratorFactory);
        this.labelGeneratorFactory = labelGeneratorFactory;
    }

    @Test
    public void expectExceptionIfAttributeDuplicateResultAs() throws IOException {
        Assert.assertNotEquals(0, ambiguousConfigPaths.size());
        for (Map.Entry<String, String> each : ambiguousConfigPaths.entrySet()) {
            Assert.assertThrows(InvalidConfigurationException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    labelGeneratorFactory.compile(new File(each.getKey()));
                }
            });
        }
    }

}
